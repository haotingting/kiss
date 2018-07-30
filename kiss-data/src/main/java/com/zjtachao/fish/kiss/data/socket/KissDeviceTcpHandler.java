/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceStartRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceStatusRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceStopRo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.data.WaterRedis;
import com.zjtachao.fish.water.common.tool.WaterSpringBootUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;

/**
 * tcp服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissDeviceTcpHandler extends SimpleChannelInboundHandler<String> {

    /** 日志 **/
    private static final Logger log = LoggerFactory.getLogger(KissDeviceTcpHandler.class);

    /** Redis **/
    @Autowired
    public WaterRedis waterRedis;



    /**
     * 读取数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        log.info("接收数据："+msg);
        if(null != msg && !"".equals(msg)
                && (msg.contains("cmd") || msg.contains("ack"))
                && msg.contains("{")){
            if(!msg.endsWith("}")){
                msg = msg + "}";
            }
            log.info("转换后："+msg);
            if(msg.contains("}{")){
                String[] msgArray = msg.split("}\\{");
                if(null != msgArray && msgArray.length > 0 ){
                    for(String subMsg : msgArray){
                        subMsg = subMsg.replaceAll("\r|\n", "");
                        if(!subMsg.startsWith("{")){
                            subMsg = "{"+subMsg;
                        }
                        if(!subMsg.endsWith("}")){
                            subMsg = subMsg + "}";
                        }
                        log.info("subMsg:"+subMsg);
                        cmdHandle(ctx , subMsg);
                    }
                }
            }else {
                msg = msg.replaceAll("\r|\n", "");
                cmdHandle(ctx , msg);
            }

        }else {
            log.info("未能处理的消息："+msg);
        }
    }

    /**
     * 处理数据
     * @param ctx
     * @param msg
     */
    private void cmdHandle(ChannelHandlerContext ctx, String msg) {
        if(null != msg && !"".equals(msg)
                && ((msg.contains("{") && msg.contains("}")) )) {
            try {
                //去除括号之前的数据
                msg = msg.substring(msg.indexOf("{"));
                JSONObject jsonObject = JSON.parseObject(msg);
                if(jsonObject.containsKey("cmd")){
                    if(jsonObject.getString("cmd").equals("gds")){
                        KissDeviceStatusRo statusRo = JSON.parseObject(msg , KissDeviceStatusRo.class);
                        if(null != statusRo && null != statusRo.getMessageId() && null != statusRo.getErr()){
                            WaterRedis waterRedis = WaterSpringBootUtil.getBean("waterRedis" , WaterRedis.class);
                            //发送数据
                            WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
                            KissDeviceRo deviceRo = new KissDeviceRo();
                            deviceRo.setMessageId(statusRo.getMessageId());
                            deviceRo.setCmd("QUERY_DEVICE");
                            long time = statusRo.getErr().intValue();
                            deviceRo.setRemainderTime(time);
                            deviceRo.setSiteCode(statusRo.getSiteCode());

                            if(statusRo.getErr().intValue() > 0){
                                deviceRo.setDeviceStatus(KissCommonContext.DeviceContext.WORKING.getCode());
                                //判断用户是否是当前用户
                                String currentUser = waterRedis.queryString(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+statusRo.getDid());
                                if(null != currentUser && !"".equals(currentUser) && currentUser.equals(statusRo.getKissToken())){
                                    deviceRo.setCurrentUser(1);
                                    //添加缓存
                                    waterRedis.set(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+statusRo.getDid() , statusRo.getKissToken() , time);
                                }else {
                                    //添加缓存
                                    waterRedis.set(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+statusRo.getDid() , "unknown" , time);
                                }

                                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                result.setMsg("查询成功");
                            }else if(statusRo.getErr().intValue() == 0){
                                deviceRo.setDeviceStatus(KissCommonContext.DeviceContext.ONLINE.getCode());
                                result.setRst(deviceRo);
                                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                result.setMsg("查询成功");
                            }else {
                                result.setMsg("该设备不在线");
                            }
                            result.setRst(deviceRo);

                            //查询messgeid
                            String messageId = waterRedis.get(KissCommonConstant.KISS_COMMON_USER_WEBSOCKET_SESSION_PREFIX+statusRo.getKissToken());
                            log.info("查询到的message为："+messageId);
                            if(null == messageId || "".equals(messageId)){
                                messageId = statusRo.getMessageId();
                            }

                            if(KissDeviceMap.contain(messageId)){
                                KissDeviceMap.get(messageId).sendMessage(JSON.toJSONString(result));
                            }

                        }
                    }else if(jsonObject.getString("cmd").equals("dc1")){
                        KissDeviceStartRo startRo = JSON.parseObject(msg , KissDeviceStartRo.class);
                        if(null != startRo && null != startRo.getMessageId() && null != startRo.getErr()){
                            WaterRedis waterRedis = WaterSpringBootUtil.getBean("waterRedis" , WaterRedis.class);
                            //发送数据
                            WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
                            KissDeviceRo deviceRo = new KissDeviceRo();
                            deviceRo.setMessageId(startRo.getMessageId());
                            if(startRo.getErr().intValue() == 0){
                                deviceRo.setCmd("START_DEVICE");
                                deviceRo.setDeviceSerialNumber(startRo.getDid());
                                long time = startRo.getPrd();
                                deviceRo.setRemainderTime(time);
                                //添加缓存（设备剩余）
                                waterRedis.set(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+startRo.getDid() , startRo.getKissToken() , time);
                                //用户自己剩余时间缓存
                                waterRedis.set(KissCommonConstant.KISS_COMMON_DEVICE_TIME_SURPLUS_PREFIX+startRo.getKissToken() , startRo.getDid() , time);
                                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                result.setMsg("开启成功");
                            }else {
                                result.setMsg("该设备不在线");
                            }
                            result.setRst(deviceRo);

                            //查询messgeid
                            String messageId = waterRedis.get(KissCommonConstant.KISS_COMMON_USER_WEBSOCKET_SESSION_PREFIX+startRo.getKissToken());
                            log.info("查询到的message为："+messageId);
                            if(null == messageId || "".equals(messageId)){
                                messageId = startRo.getMessageId();
                            }

                            if(KissDeviceMap.contain(messageId)){
                                KissDeviceMap.get(messageId).sendMessage(JSON.toJSONString(result));
                            }

                        }
                    }else if(jsonObject.getString("cmd").equals("dc2")){
                        KissDeviceStopRo stopRo = JSON.parseObject(msg , KissDeviceStopRo.class);
                        if(null != stopRo && null != stopRo.getMessageId() && null != stopRo.getErr()){
                            WaterRedis waterRedis = WaterSpringBootUtil.getBean("waterRedis" , WaterRedis.class);
                            //发送数据
                            WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
                            KissDeviceRo deviceRo = new KissDeviceRo();
                            deviceRo.setMessageId(stopRo.getMessageId());
                            if(stopRo.getErr().intValue() == 0){
                                //删除缓存
                                //不删除，用于恢复使用
                                //waterRedis.delete(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+stopRo.getDid());



                                deviceRo.setCmd("STOP_DEVICE");
                                deviceRo.setDeviceSerialNumber(stopRo.getDid());
                                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                result.setMsg("停止成功");
                            }else {
                                result.setMsg("该设备不在线");
                            }
                            result.setRst(deviceRo);

                            //查询messgeid
                            String messageId = waterRedis.get(KissCommonConstant.KISS_COMMON_USER_WEBSOCKET_SESSION_PREFIX+stopRo.getKissToken());
                            log.info("查询到的message为："+messageId);
                            if(null == messageId || "".equals(messageId)){
                                messageId = stopRo.getMessageId();
                            }

                            if(KissDeviceMap.contain(messageId)){
                                KissDeviceMap.get(messageId).sendMessage(JSON.toJSONString(result));
                            }

                        }
                    }
                }
            } catch (Exception ex) {
                log.error("接收数据出错", ex);
            }
        }
    }


    /**
     * 状态连接上
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
        String host = address.getHostString();
        if(host.equals("localhost")){
            host = "127.0.0.1";
        }
        KissDeviceTcpMap.add(host+":"+address.getPort(),ctx.channel());
        log.info("连接上服务器："+host+":"+address.getPort());
        super.channelActive(ctx);
    }

    /**
     * 状态异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("出现异常" , cause);
        InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
        String host = address.getHostString();
        if(host.equals("localhost")){
            host = "127.0.0.1";
        }
        KissDeviceTcpMap.remove(host+":"+address.getPort());
        log.info("断开服务器："+host+":"+address.getPort());
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    /**
     * 状态断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接");
        InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
        String host = address.getHostString();
        if(host.equals("localhost")){
            host = "127.0.0.1";
        }
        KissDeviceTcpMap.remove(host+":"+address.getPort());
        log.info("连接上服务器："+host+":"+address.getPort());
        super.channelInactive(ctx);
    }
}
