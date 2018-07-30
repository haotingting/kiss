/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.netty.tcp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjtachao.fish.kiss.common.bean.ro.*;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissNettyContext;
import com.zjtachao.fish.kiss.netty.bean.*;
import com.zjtachao.fish.water.common.data.WaterRedis;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * kiss handler
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Component
@Qualifier("kissServerHandler")
@Sharable
public class KissServerHandler extends SimpleChannelInboundHandler<String>  {

    /** 日志 **/
    private static final Logger log = LoggerFactory.getLogger(KissServerHandler.class);

    /** Redis **/
    @Autowired
    public WaterRedis waterRedis;

    /** 地址 **/
    @Value("${fish.websocket.address}")
    private String address;



    /**
     * 读取连接的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg)
            throws Exception {
        log.info("client msg:"+msg);
        String clientIdToLong= ctx.channel().id().asLongText();
        log.info("client long id:"+clientIdToLong);
        //String clientIdToShort= ctx.channel().id().asShortText();
        //log.info("client short id:"+clientIdToShort);
        if(msg.indexOf("exit")!=-1){
            //close
            ctx.channel().close();
        }else{
            //send to client
            cmdHandle(msg , clientIdToLong , ctx.channel());
        }

    }




    /**
     * 处理请求
     * @param msg
     * @return
     */
    private void cmdHandle(String msg , String clientId , Channel channel){
        if(null != msg && !"".equals(msg)
                && (msg.contains("cmd") || msg.contains("ack"))
                && msg.contains("{")){
            if(!msg.endsWith("}")){
                msg = msg + "}";
            }
            if(msg.contains("}{")){
                String[] msgArray = msg.split("}\\{");
                if(null != msgArray && msgArray.length > 0 ){
                    try {
                        for(String subMsg : msgArray){
                            subMsg = subMsg.replaceAll("\r|\n", "");
                            if(!subMsg.startsWith("{")){
                                subMsg = "{"+subMsg;
                            }
                            if(!subMsg.endsWith("}")){
                                subMsg = subMsg + "}";
                            }
                            log.info("subMsg:"+subMsg);
                            handleReceiveMsg(subMsg , clientId , channel);
                            //先尝试休眠
                            TimeUnit.MILLISECONDS.sleep(200l);
                        }
                    }catch (Exception ex){
                        log.error("处理多条json数据异常："+msg , ex);
                    }
                }
            }else {
                handleReceiveMsg(msg , clientId , channel);
            }

        }else {
            log.info("未能处理的消息："+msg);
        }
    }


    private void handleReceiveMsg(String msg , String clientId , Channel channel){
        boolean flag = false;
        String result = null;
        try{
            //去除掉干扰
            msg = msg.substring(msg.indexOf("{"));
            log.info("执行msg:"+msg);
            JSONObject jsonObject = JSON.parseObject(msg);
            if(jsonObject.containsKey("cmd")){
                if(jsonObject.getString("cmd").equals("log5") || jsonObject.getString("cmd").equals("log4")){
                    //登录
                    KissLoginResponse response = new KissLoginResponse();
                    response.setAck(jsonObject.getString("cmd"));
                    try{
                        boolean cmdflag = true;
                        if(!jsonObject.containsKey("did")){
                            cmdflag = false;
                            msg = "lack did";
                            response.setErr(KissNettyContext.CommonResponseContext.LACK_PARAM.getCode());
                        }

                        if(cmdflag){
                            boolean resdisFlag = true;
                            String did = jsonObject.getString("did");

                            //设置入设备
                            KissChannelBean channelBean = KissChannelMap.get(clientId);
                            channelBean.setDid(did);
                            KissChannelMap.add(clientId , channelBean);
                            KissDeviceMap.add(did,clientId);

                            KissDeviceSyncRo syncRo = new KissDeviceSyncRo();
                            //did
                            syncRo.setDid(did);
                            syncRo.setErr(KissNettyContext.CommonResponseContext.NORMAL.getCode());
                            syncRo.setClientId(clientId);
                            syncRo.setAddress(address);

                            if(resdisFlag){
                                response.setErr(KissNettyContext.CommonResponseContext.NORMAL.getCode());
                                if(did.equals("a1708110739")){
                                    response.setPar(16843);
                                }else {
                                    response.setPar(17883);
                                }
                                response.setPar1(17);
                                response.setPar2(34);
                                response.setPar3(51);
                                response.setPar4(68);
                                response.setPar5(85);
                                response.setPar6(4);
                                response.setPar7(5);
                                flag = true;
                            }else {
                                response.setErr(KissNettyContext.CommonResponseContext.SERVER_ERROR.getCode());
                            }

                            //写入redis
                            writeToCache("login" , did , syncRo , clientId);
                        }
                    }catch (Exception ex){
                        log.error("登录出错:"+msg,ex);
                        response.setErr(KissNettyContext.CommonResponseContext.SERVER_ERROR.getCode());
                    }
                    result = JSON.toJSONString(response);
                }else if(jsonObject.getString("cmd").equals("sync")){
                    //设备同步
                    KissSyncResponse response = new KissSyncResponse();
                    response.setAck("sync");
                    try{
                        boolean cmdflag = true;
                        if(!jsonObject.containsKey("did")){
                            cmdflag = false;
                            msg = "lack did";
                            response.setErr(KissNettyContext.CommonResponseContext.LACK_PARAM.getCode());
                        }
                        if(!jsonObject.containsKey("err")){
                            cmdflag = false;
                            msg = "lack err";
                            response.setErr(KissNettyContext.CommonResponseContext.LACK_PARAM.getCode());
                        }
                        if(cmdflag){
                            log.info("状态同步");
                            //查询数据是否存在
                            String did = jsonObject.getString("did");
                            //设置入设备
                            KissChannelBean channelBean = KissChannelMap.get(clientId);
                            channelBean.setDid(did);
                            KissChannelMap.add(clientId , channelBean);
                            KissDeviceMap.add(did,clientId);

                            //直接写入
                            KissDeviceSyncRo syncRo = new KissDeviceSyncRo();
                            syncRo.setDid(did);
                            syncRo.setErr(KissNettyContext.CommonResponseContext.NORMAL.getCode());
                            syncRo.setClientId(clientId);
                            syncRo.setAddress(address);
                            response.setErr(KissNettyContext.CommonResponseContext.NORMAL.getCode());

                            //写入redis
                            writeToCache("sync" , did , syncRo , clientId);
                            flag = true;
                        }
                    }catch (Exception ex){
                        log.error("同步出错:"+msg , ex);
                        response.setErr(KissNettyContext.CommonResponseContext.SERVER_ERROR.getCode());
                    }
                    result = JSON.toJSONString(response);
                }else if(jsonObject.getString("cmd").equals("gds")){
                    //设备状态
                    try{
                        boolean cmdflag = false;
                        //查询设备连接是否在本服务器上
                        if(jsonObject.containsKey("did")){
                            String did = jsonObject.getString("did");
                            if(KissDeviceMap.contain(did)){
                                String deviceChannelId = KissDeviceMap.get(did);
                                if(null != deviceChannelId && !"".equals(deviceChannelId)
                                        && KissChannelMap.contain(deviceChannelId)){
                                    if(KissChannelMap.get(deviceChannelId).getChannel().isActive()){
                                        cmdflag = true;
                                        KissDeviceStatusRo statusRo = JSON.parseObject(msg , KissDeviceStatusRo.class);
                                        statusRo.setChannelId(clientId);
                                        //数据存入redis
                                        waterRedis.set(KissCommonConstant.KISS_COMMON_DEVICE_GDS_PREFIX+deviceChannelId ,JSON.toJSONString(statusRo) , 60);
                                        //发送消息
                                        KissGdsRequest gdsRequest = new KissGdsRequest();
                                        Channel sendChannel = KissChannelMap.get(deviceChannelId).getChannel();
                                        if(null != sendChannel && sendChannel.isActive()){
                                            sendChannel.writeAndFlush(JSON.toJSONString(gdsRequest));
                                            log.info("发送成功");
                                            result = "success";
                                        }else {
                                            KissChannelMap.remove(deviceChannelId);
                                            sendChannel.close();
                                            log.info("断开连接，剩余连接数："+KissChannelMap.size());
                                        }
                                    }

                                }
                            }
                        }
                        if(!cmdflag){
                            log.info("不在此节点上");
                        }
                        log.info(msg);
                    }catch (Exception ex){
                        log.error("同步出错:"+msg , ex);
                    }
                }else if(jsonObject.getString("cmd").equals("dc1")){
                    //开启设备
                    try{
                        boolean cmdflag = false;
                        //查询设备连接是否在本服务器上
                        if(jsonObject.containsKey("did")){
                            String did = jsonObject.getString("did");
                            if(KissDeviceMap.contain(did)){
                                String deviceChannelId = KissDeviceMap.get(did);
                                if(null != deviceChannelId && !"".equals(deviceChannelId)
                                        && KissChannelMap.contain(deviceChannelId)){
                                    if(KissChannelMap.get(deviceChannelId).getChannel().isActive()){
                                        KissDeviceStartRo startRo = JSON.parseObject(msg , KissDeviceStartRo.class);
                                        if(startRo.getPrd() >0 ){
                                            Channel sendChannel = KissChannelMap.get(deviceChannelId).getChannel();
                                            if(null != sendChannel && sendChannel.isActive()){
                                                cmdflag = true;
                                                startRo.setChannelId(clientId);
                                                //数据存入redis
                                                waterRedis.set(KissCommonConstant.KISS_COMMON_DEVICE_START_PREFIX+deviceChannelId ,JSON.toJSONString(startRo) , 60);
                                                //发送消息
                                                KissStartRequest starRequest = new KissStartRequest();
                                                starRequest.setPrd(startRo.getPrd());
                                                sendChannel.writeAndFlush(JSON.toJSONString(starRequest));
                                                log.info("发送成功");
                                                result = "success";
                                            }else {
                                                KissChannelMap.remove(deviceChannelId);
                                                sendChannel.close();
                                                log.info("断开连接，剩余连接数："+KissChannelMap.size());
                                            }

                                        }else {
                                            log.info("开启时间小于等于0");
                                        }
                                    }else {
                                        log.info("渠道连接断开");
                                    }

                                }else {
                                    log.info("设备map不包含channel");
                                }
                            }else {
                                log.info("map中不包含did");
                            }
                        }else {
                            log.info("未找到did");
                        }
                        if(!cmdflag){
                            log.info("不在此节点上");
                        }
                        log.info(msg);
                    }catch (Exception ex){
                        log.error("同步出错:"+msg , ex);
                    }
                }else if(jsonObject.getString("cmd").equals("dc2")){
                    //停止设备
                    try{
                        boolean cmdflag = false;
                        //查询设备连接是否在本服务器上
                        if(jsonObject.containsKey("did")){
                            String did = jsonObject.getString("did");
                            if(KissDeviceMap.contain(did)){
                                String deviceChannelId = KissDeviceMap.get(did);
                                if(null != deviceChannelId && !"".equals(deviceChannelId)
                                        && KissChannelMap.contain(deviceChannelId)){
                                    Channel sendChannel = KissChannelMap.get(deviceChannelId).getChannel();
                                    if(null != sendChannel && sendChannel.isActive()){
                                        KissDeviceStopRo stopRo = JSON.parseObject(msg , KissDeviceStopRo.class);
                                        cmdflag = true;
                                        stopRo.setChannelId(clientId);
                                        //数据存入redis
                                        waterRedis.set(KissCommonConstant.KISS_COMMON_DEVICE_STOP_PREFIX+deviceChannelId ,JSON.toJSONString(stopRo) , 60);
                                        //发送消息
                                        KissStopRequest stopRequest = new KissStopRequest();
                                        KissChannelMap.get(deviceChannelId).getChannel().writeAndFlush(JSON.toJSONString(stopRequest));
                                        log.info("发送成功");
                                        result = "success";
                                    }else {
                                        KissChannelMap.remove(deviceChannelId);
                                        sendChannel.close();
                                        log.info("断开连接，剩余连接数："+KissChannelMap.size());
                                    }

                                }
                            }
                        }
                        if(!cmdflag){
                            log.info("不在此节点上");
                        }
                        log.info(msg);
                    }catch (Exception ex){
                        log.error("同步出错:"+msg , ex);
                    }
                }else if(jsonObject.getString("cmd").equals("csync1")){
                    //停止设备
                    try{
                        KissCsync1Response request = new KissCsync1Response();
                        result = JSON.toJSONString(request);
                    }catch (Exception ex){
                        log.error("同步出错:"+msg , ex);
                    }
                }else if(jsonObject.getString("cmd").equals("kiss")){
                    //停止设备
                    try{
                        //查询设备连接是否在本服务器上
                        KissDeviceInfoRo infoRo = new KissDeviceInfoRo();
                        infoRo.setMsg(jsonObject.getString("msg"));
                        log.info("发送成功");
                        result = JSON.toJSONString(infoRo);
                    }catch (Exception ex){
                        log.error("同步出错:"+msg , ex);
                    }
                }else {
                    msg = "unkown cmd ["+jsonObject.getString("cmd")+"]";
                }
                //发送数据
                if(!flag && null == result){
                    KissUnknownResponse response = new KissUnknownResponse();
                    response.setAck("unknown");
                    response.setMsg(msg);
                    result = JSON.toJSONString(response);
                    log.info("return message:"+JSON.toJSONString(response));
                    channel.writeAndFlush(result);
                }else {
                    log.info("return message:"+result);
                    channel.writeAndFlush(result);
                }

            }else if(jsonObject.containsKey("ack")){
                if(jsonObject.getString("ack").equals("dc1")){
                    //开启设备
                    String did = null;
                    if(KissChannelMap.contain(clientId)){
                        did = KissChannelMap.get(clientId).getDid();
                    }
                    try{
                        KissStartResponse startResponse = JSON.parseObject(msg , KissStartResponse.class);
                        if(null != startResponse
                                && null != startResponse.getErr()
                                && startResponse.getErr().intValue() == KissNettyContext.CommonResponseContext.NORMAL.getCode()){
                            //开启成功
                            String customJson = waterRedis.get(KissCommonConstant.KISS_COMMON_DEVICE_START_PREFIX+clientId);
                            if(null != customJson && !"".equals(customJson)){
                                KissDeviceStartRo startRo = JSON.parseObject(customJson , KissDeviceStartRo.class);
                                if(null != startRo && null != startRo.getChannelId()){
                                    startRo.setErr(startResponse.getErr());
                                    if(KissChannelMap.contain(startRo.getChannelId())
                                            && KissChannelMap.get(startRo.getChannelId()).getChannel().isActive()){
                                        log.info("开启成功");
                                        KissChannelMap.get(startRo.getChannelId()).getChannel().writeAndFlush(JSON.toJSONString(startRo));
                                    }
                                }
                            }
                        }else{
                            //开启失败
                            log.error("停止设备失败，clientid:"+clientId+" did:"+ did);
                        }
                    }catch (Exception ex){
                        log.error("停止设备失败，clientid:"+clientId+" did:"+ did , ex);
                    }
                }else if(jsonObject.getString("ack").equals("dc2")){
                    //停止设备
                    String did = null;
                    if(KissChannelMap.contain(clientId)){
                        did = KissChannelMap.get(clientId).getDid();
                    }
                    try {
                        KissStopResponse stopResponse = JSON.parseObject(msg , KissStopResponse.class);
                        if(null != stopResponse
                                && null != stopResponse.getErr()
                                && stopResponse.getErr().intValue() == KissNettyContext.CommonResponseContext.NORMAL.getCode()){
                            //停止成功
                            String customJson = waterRedis.get(KissCommonConstant.KISS_COMMON_DEVICE_STOP_PREFIX+clientId);
                            if(null != customJson && !"".equals(customJson)){
                                KissDeviceStopRo stopRo = JSON.parseObject(customJson , KissDeviceStopRo.class);
                                if(null != stopRo && null != stopRo.getChannelId()){
                                    stopRo.setErr(stopResponse.getErr());
                                    if(KissChannelMap.contain(stopRo.getChannelId())
                                            && KissChannelMap.get(stopRo.getChannelId()).getChannel().isActive()){
                                        log.info("关闭成功");
                                        KissChannelMap.get(stopRo.getChannelId()).getChannel().writeAndFlush(JSON.toJSONString(stopRo));
                                    }
                                }
                            }

                        }else {
                            //停止失败
                            log.error("停止设备失败，clientid:"+clientId+" did:"+did);
                        }
                    }catch (Exception ex){
                        log.error("停止设备失败，clientid:"+clientId+" did:"+did , ex);
                    }
                }else if(jsonObject.getString("ack").equals("dc3")){
                    //获取时长
                    String did = null;
                    if(KissChannelMap.contain(clientId)){
                        did = KissChannelMap.get(clientId).getDid();
                    }
                    try {
                        KissTimeResponse timeResponse = JSON.parseObject(msg , KissTimeResponse.class);
                        if(null != timeResponse
                                && null != timeResponse.getErr()
                                && timeResponse.getErr().intValue() == KissNettyContext.CommonResponseContext.NORMAL.getCode()
                                && null != timeResponse.getRem()){
                            //获取时长 成功
                            //TODO 通知获取时长

                        }else {
                            //获取时长 失败
                            //TODO 通知获取时长失败
                            log.error("获取时长失败，clientid:"+clientId+" did:"+did);
                        }
                    }catch (Exception ex){
                        log.error("获取时长失败，clientid:"+clientId+" did:"+did , ex);
                    }
                }else if(jsonObject.getString("ack").equals("gds")){
                    //获取状态
                    String did = null;
                    if(KissChannelMap.contain(clientId)){
                        did = KissChannelMap.get(clientId).getDid();
                    }
                    try {
                        KissStatusResponse statusResponse = JSON.parseObject(msg , KissStatusResponse.class);
                        if(null != statusResponse
                                && null != statusResponse.getErr()){
                            //获取状态 成功
                            String customJson = waterRedis.get(KissCommonConstant.KISS_COMMON_DEVICE_GDS_PREFIX+clientId);
                            if(null != customJson && !"".equals(customJson)){
                                KissDeviceStatusRo statusRo = JSON.parseObject(customJson , KissDeviceStatusRo.class);
                                if(null != statusRo && null != statusRo.getChannelId()){
                                    statusRo.setErr(statusResponse.getErr());
                                    if(KissChannelMap.contain(statusRo.getChannelId())
                                            && KissChannelMap.get(statusRo.getChannelId()).getChannel().isActive()){
                                        KissChannelMap.get(statusRo.getChannelId()).getChannel().writeAndFlush(JSON.toJSONString(statusRo));
                                    }
                                }
                            }
                        }else {
                            //获取状态 失败
                            log.error("获取状态失败，clientid:"+clientId+" did:"+did);
                        }
                    }catch (Exception ex){
                        log.error("获取状态失败，clientid:"+clientId+" did:"+did , ex);
                    }
                }else {
                    msg = "unkown ack ["+jsonObject.getString("ack")+"]";
                }
            }else {
                msg = "invalid param";
            }
        }catch (Exception ex){
            log.error(ex.getMessage()+", msg:"+msg , ex);
        }
    }


    /**
     * 类型
     * @param type
     * @param did
     * @param syncRo
     */
    private void writeToCache(String type , String did , KissDeviceSyncRo syncRo , String clientId){
        log.info("写入缓存("+type+")");
        try {
            String cacheDidJson = waterRedis.get(KissCommonConstant.KISS_COMMON_DEVICE_CHANNEL_PREFIX+did);
            if(null != cacheDidJson && !"".equals(cacheDidJson)){
                KissDeviceSyncRo querySyncRo = JSON.parseObject(cacheDidJson , KissDeviceSyncRo.class);
                //log.info("newid:"+clientId+" oldjson:"+cacheDidJson);
                if(null != querySyncRo && null != querySyncRo.getClientId()
                        && !"".equals(querySyncRo.getClientId())
                        && !clientId.equals(querySyncRo.getClientId())){
                    KissChannelBean channelBean = KissChannelMap.get(querySyncRo.getClientId());
                    if(null != channelBean && null != channelBean.getChannel()){
                        log.info("关闭失效连接："+querySyncRo.getClientId());
                        channelBean.getChannel().close();
                    }
                }
            }
        }catch (Exception ex){
            log.info("关闭失效连接失败"+ex.getMessage() , ex);
        }
        waterRedis.set(KissCommonConstant.KISS_COMMON_DEVICE_CHANNEL_PREFIX+did , JSON.toJSONString(syncRo) , 60 * 30);
    }


    /**
     * 连接上时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
        ctx.channel().writeAndFlush( "Welcome to Share-Chair service!\n");
        String clientIdToLong= ctx.channel().id().asLongText();
        log.info("channel id:"+clientIdToLong);
        KissChannelMap.add(ctx.channel().id().asLongText() , new KissChannelBean(ctx.channel()));
        log.info("新增连接，现存数量为："+ KissChannelMap.size());
        super.channelActive(ctx);
    }


    /**
     * 异常时触发
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("exceptionCaught" , cause);
        ctx.close();
    }

    /**
     * 连接断开时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive:"+ctx.channel().id().asLongText());
        KissChannelBean channelBean = KissChannelMap.get(ctx.channel().id().asLongText());
        if(null != channelBean && null != channelBean.getDid()){
            String did = channelBean.getDid();
            KissDeviceMap.remove(did);
        }
        KissChannelMap.remove(ctx.channel().id().asLongText());
        log.info("断开连接，现存数量为："+ KissChannelMap.size());
        super.channelInactive(ctx);
    }



}
