/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.netty.controller.chair;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.*;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.netty.tcp.KissChannelMap;
import com.zjtachao.fish.kiss.netty.tcp.KissDeviceMap;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import io.netty.channel.Channel;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 *
 *  共享按摩椅通用
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/chair")
public class KissChairController extends WaterBootBaseController {

    /**
     * 开启设备
     * @return
     */
    @POST
    @Path("/device/start")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceStartRo> deviceStart(String json){
        WaterBootResultBean<KissDeviceStartRo> result = new WaterBootResultBean<KissDeviceStartRo>();
        try {
            if(null != json && !"".equals(json)){
                boolean flag = true;
                KissDeviceStartRo startRo = JSON.parseObject(json , KissDeviceStartRo.class);
                if(null == startRo){
                    flag = false;
                    result.setMsg("参数错误");
                }
                if(flag && (null == startRo.getDid() || "".equals(startRo.getDid()))){
                    flag = false;
                    result.setMsg("设备序列号为空");
                }
                if(flag && (startRo.getPrd() <= 0)){
                    flag = false;
                    result.setMsg("时长必须大于0");
                }
                if(flag && !KissDeviceMap.contain(startRo.getDid())){
                    flag = false;
                    result.setMsg("未找到设备");
                }
                if(flag){
                    String syncJson = waterRedis.queryString(KissCommonConstant.KISS_COMMON_DEVICE_CHANNEL_PREFIX+startRo.getDid());
                    if(null != syncJson && !"".equals(syncJson)){
                        KissDeviceStartChannelRo channelRo = new KissDeviceStartChannelRo();
                        channelRo.setPrd(startRo.getPrd());
                        String channelJson = JSON.toJSONString(channelRo);
                        Channel channel = null;
                        if(KissChannelMap.contain(KissDeviceMap.get(startRo.getDid()))) {
                            channel = KissChannelMap.get(KissDeviceMap.get(startRo.getDid())).getChannel();
                        }
                        if(null != channel && channel.isActive()){
                            channel.writeAndFlush(channelJson);
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("发送成功");
                        }else {
                            result.setMsg("连接通道不存在或者已关闭");
                        }
                    }else {
                        flag = false;
                        result.setMsg("该设备不在线");
                    }

                }
            }else {
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 关闭设备
     * @return
     */
    @POST
    @Path("/device/stop")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceStopRo> deviceStop(String json){
        WaterBootResultBean<KissDeviceStopRo> result = new WaterBootResultBean<KissDeviceStopRo>();
        try {
            if(null != json && !"".equals(json)){
                boolean flag = true;
                KissDeviceStopRo stopRo = JSON.parseObject(json , KissDeviceStopRo.class);
                if(null == stopRo){
                    flag = false;
                    result.setMsg("参数错误");
                }
                if(flag && (null == stopRo.getDid() || "".equals(stopRo.getDid()))){
                    flag = false;
                    result.setMsg("设备序列号为空");
                }
                if(flag && !KissDeviceMap.contain(stopRo.getDid())){
                    flag = false;
                    result.setMsg("未找到设备");
                }
                if(flag){
                    String syncJson = waterRedis.queryString(KissCommonConstant.KISS_COMMON_DEVICE_CHANNEL_PREFIX+stopRo.getDid());
                    if(null != syncJson && !"".equals(syncJson)){
                        KissDeviceStartChannelRo channelRo = new KissDeviceStartChannelRo();
                        String channelJson = JSON.toJSONString(channelRo);
                        Channel channel = null;
                        if(KissChannelMap.contain(KissDeviceMap.get(stopRo.getDid()))) {
                            channel = KissChannelMap.get(KissDeviceMap.get(stopRo.getDid())).getChannel();
                        }
                        if(null != channel && channel.isActive()){
                            channel.writeAndFlush(channelJson);
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("发送成功");
                        }else {
                            result.setMsg("连接通道不存在或者已关闭");
                        }
                    }else {
                        flag = false;
                        result.setMsg("该设备不在线");
                    }

                }
            }else {
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }


    /**
     * 获取时长
     * @return
     */
    @POST
    @Path("/device/time")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceTimeRo> deviceTime(String json){
        WaterBootResultBean<KissDeviceTimeRo> result = new WaterBootResultBean<KissDeviceTimeRo>();
        try {
            if(null != json && !"".equals(json)){
                boolean flag = true;
                KissDeviceTimeRo timeRo = JSON.parseObject(json , KissDeviceTimeRo.class);
                if(null == timeRo){
                    flag = false;
                    result.setMsg("参数错误");
                }
                if(flag && (null == timeRo.getDid() || "".equals(timeRo.getDid()))){
                    flag = false;
                    result.setMsg("设备序列号为空");
                }
                if(flag && !KissDeviceMap.contain(timeRo.getDid())){
                    flag = false;
                    result.setMsg("未找到设备");
                }
                if(flag){
                    String syncJson = waterRedis.queryString(KissCommonConstant.KISS_COMMON_DEVICE_CHANNEL_PREFIX+timeRo.getDid());
                    if(null != syncJson && !"".equals(syncJson)){
                        KissDeviceStartChannelRo channelRo = new KissDeviceStartChannelRo();
                        String channelJson = JSON.toJSONString(channelRo);
                        Channel channel = null;
                        if(KissChannelMap.contain(KissDeviceMap.get(timeRo.getDid()))) {
                            channel = KissChannelMap.get(KissDeviceMap.get(timeRo.getDid())).getChannel();
                        }
                        if(null != channel && channel.isActive()){
                            channel.writeAndFlush(channelJson);
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("发送成功");
                        }else {
                            result.setMsg("连接通道不存在或者已关闭");
                        }
                    }else {
                        flag = false;
                        result.setMsg("该设备不在线");
                    }
                }
            }else {
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }


    /**
     * 设备状态
     * @return
     */
    @POST
    @Path("/device/status")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceStatusRo> deviceStatus(String json){
        WaterBootResultBean<KissDeviceStatusRo> result = new WaterBootResultBean<KissDeviceStatusRo>();
        try {
            if(null != json && !"".equals(json)){
                boolean flag = true;
                KissDeviceStatusRo statusRo = JSON.parseObject(json , KissDeviceStatusRo.class);
                if(null == statusRo){
                    flag = false;
                    result.setMsg("参数错误");
                }
                if(flag && (null == statusRo.getDid() || "".equals(statusRo.getDid()))){
                    flag = false;
                    result.setMsg("设备序列号为空");
                }
                if(flag && !KissDeviceMap.contain(statusRo.getDid())){
                    flag = false;
                    result.setMsg("未找到设备");
                }
                if(flag){
                    String syncJson = waterRedis.queryString(KissCommonConstant.KISS_COMMON_DEVICE_CHANNEL_PREFIX+statusRo.getDid());
                    if(null != syncJson && !"".equals(syncJson)){
                        KissDeviceStartChannelRo channelRo = new KissDeviceStartChannelRo();
                        String channelJson = JSON.toJSONString(channelRo);
                        Channel channel = null;
                        if(KissChannelMap.contain(KissDeviceMap.get(statusRo.getDid()))) {
                            channel = KissChannelMap.get(KissDeviceMap.get(statusRo.getDid())).getChannel();
                        }
                        if(null != channel && channel.isActive()){
                            channel.writeAndFlush(channelJson);
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("发送成功");
                        }else {
                            result.setMsg("连接通道不存在或者已关闭");
                        }
                    }else {
                        flag = false;
                        result.setMsg("该设备不在线");
                    }
                }
            }else {
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

}
