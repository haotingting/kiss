/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.domain.KissDevice;
import com.zjtachao.fish.kiss.common.bean.ro.*;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.common.util.KissCommonOrderUtil;
import com.zjtachao.fish.kiss.data.service.device.KissDeviceService;
import com.zjtachao.fish.kiss.data.service.goods.KissGoodsService;
import com.zjtachao.fish.kiss.data.service.order.KissOrderService;
import com.zjtachao.fish.kiss.data.socket.KissDeviceMap;
import com.zjtachao.fish.kiss.data.socket.KissDeviceMessageAsyncTask;
import com.zjtachao.fish.kiss.data.socket.KissDeviceTcpMap;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.data.WaterRedis;
import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 控制参数
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissDeviceHandleServiceImpl implements com.zjtachao.fish.kiss.data.service.device.KissDeviceHandleService {

    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(KissDeviceHandleServiceImpl.class);

    /** Redis **/
    @Autowired
    public WaterRedis waterRedis;

    /** 设备服务*/
    @Autowired
    private KissDeviceService kissDeviceService;

    /** 订单服务 **/
    @Autowired
    private KissOrderService kissOrderService;

    /** 商品服务 **/
    @Autowired
    private KissGoodsService kissGoodsService;

    /** 异步执行任务 **/
    @Autowired
    private KissDeviceMessageAsyncTask kissDeviceMessageAsyncTask;

    /** 最小日期 **/
    @Value("${com.zjtachao.fish.kiss.data.normal.param.min-date}")
    private String minOrderDate;

    /**
     * 启动服务
     */
    public void nettyChannelStart(){
        Set<String> channels = waterRedis.smembers(KissCommonConstant.KISS_COMMON_DEVICE_NETTY_PREFIX);
        if(null != channels && !channels.isEmpty()){
            for(String value : channels){
                if(null != value &&  value.contains(":")){
                    boolean flag = true;
                    if(KissDeviceTcpMap.contain(value) && KissDeviceTcpMap.get(value).isActive()){
                        flag = false;
                    }
                    if(flag){
                        //启动服务
                        String[] address = value.split(":");
                        logger.info("启动服务:"+value);
                        kissDeviceMessageAsyncTask.nettyChannelStart(address[0] , Integer.parseInt(address[1]));
                    }
                }
            }
        }
    }

    /**
     * 发送心跳
     */
    public void sendHeartBeat(){
        kissDeviceMessageAsyncTask.heartBeat();
    }

    /**
     * 发送消息
     * @param message
     * @param did
     * @return
     */
    private boolean sendMessage(String message , String did){
        boolean flag = false;
        String json = waterRedis.get(KissCommonConstant.KISS_COMMON_DEVICE_CHANNEL_PREFIX+did);
        if(null != json && !"".equals(json)){
            KissDeviceSyncRo syncRo = JSON.parseObject(json , KissDeviceSyncRo.class);
            if(null != syncRo && null != syncRo.getAddress()){
                logger.info("发送数据为:"+message);
                kissDeviceMessageAsyncTask.sendMessage(message , syncRo.getAddress());
                flag = true;
            }else {
                logger.info("address地址错误");
            }
        }else {
            logger.info("渠道json为空");
        }
        return flag;

    }


    /**
     * 总控
     * @param json
     * @param sessionId
     */
    @Override
    public void handle(String json, String sessionId){
        String message = null;
        String complate = null;
        try{
            boolean flag = true;

            if(null == json || "".equals(json)){
                message = "参数错误";
                flag = false;
            }
            if(flag && !((json.contains("{") && json.contains("}")) || (json.contains("[") && json.contains("]")))){
                message = "数据必须是json格式";
                flag = false;
            }
            if(flag){
                KissDeviceHandleRo handleRo = JSON.parseObject(json, KissDeviceHandleRo.class);
                if(null == handleRo || null == handleRo.getKissToken()){
                    message = "未找到token参数";
                    flag = false;
                }
                if(flag && (null == handleRo.getCmd() || "".equals(handleRo.getCmd()))){
                    message = "未找到命令参数";
                    flag = false;
                }

                //查询用户是否登录
                if(flag){
                    String userJson = waterRedis.queryString(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+handleRo.getKissToken());
                    Long expireTime = waterRedis.ttl(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+handleRo.getKissToken());
                    if(null != userJson && !"".equals(userJson)){
                        KissBaseUserRo userRo = JSON.parseObject(userJson , KissBaseUserRo.class);
                        if(null != userRo){
                            //延期登录
                            if(null != userRo.getUserLoginType()){
                                if(userRo.getUserLoginType().equals(KissCommonContext.LoginTypeContext.PROGRAM.getCode())){
                                    if((KissCommonConstant.KISS_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME - expireTime)
                                            > KissCommonConstant.KISS_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME / 3){
                                        waterRedis.expireTime(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+handleRo.getKissToken()
                                                ,KissCommonConstant.KISS_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME/60);
                                    }
                                }
                            }else if(null != userRo.getUserLoginType()){
                                if(userRo.getUserLoginType().equals(KissCommonContext.LoginTypeContext.MOBILE.getCode())){
                                    if((KissCommonConstant.KISS_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME - expireTime)
                                            > KissCommonConstant.KISS_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME / 3){
                                        waterRedis.expireTime(KissCommonConstant.KISS_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME+handleRo.getKissToken()
                                                ,KissCommonConstant.KISS_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME/60);
                                    }
                                }
                            }
                        }else {
                            message = "未找到用户信息";
                            flag = false;
                        }
                    }else {
                        message = "未找到用户信息";
                        flag = false;
                    }
                }

                if(flag){
                    //启动服务
                    nettyChannelStart();
                    if("QUERY_DEVICE".equals(handleRo.getCmd())){
                        complate = this.querySingle(json , sessionId);
                    }else if("START_DEVICE".equals(handleRo.getCmd())){
                        complate =  this.startDevice(json , sessionId);
                    }else if("STOP_DEVICE".equals(handleRo.getCmd())){
                        complate = this.stopDevice(json , sessionId);
                    }else if("HEART_BEAT".equals(handleRo.getCmd())){
                        complate = this.heartBeat(json , sessionId);
                    }else if("RECOVER_DEVICE".equals(handleRo.getCmd())){
                        complate = this.recoverDevice(json , sessionId);
                    }else {
                        message = "未找命令："+handleRo.getCmd();
                        flag = false;
                    }
                }

            }
        }catch (Exception ex){
            logger.error("参数错误" , ex);
        }
        if(null != complate){
            KissDeviceMap.get(sessionId).sendMessage(complate);
        }
        if(null != message){
            WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
            result.setMsg(message);
            KissDeviceMap.get(sessionId).sendMessage(JSON.toJSONString(result));
        }
    }


    /**
     * 心跳
     * @return
     */
    private String heartBeat(String json , String sessionId){
        WaterBootResultBean<KissDeviceHandleRo> result = new WaterBootResultBean<KissDeviceHandleRo>();
        try {
            if(null != json && !"".equals(json)) {
                KissDeviceHandleRo handleRo = JSON.parseObject(json , KissDeviceHandleRo.class);
                if(null != handleRo){
                    handleRo.setMessageId(sessionId);
                    result.setRst(handleRo);
                    //设置用户token对应的数据
                    waterRedis.set(KissCommonConstant.KISS_COMMON_USER_WEBSOCKET_SESSION_PREFIX+handleRo.getKissToken() , sessionId , 300l);
                }
                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                result.setMsg("HEART_BEAT OK!");
            }else {
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return JSON.toJSONString(result);
    }


    /**
     * 查询单条设备
     * @return
     */
    private String querySingle(String json , String sessionId){
        WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
        try {
            boolean flag = true;
            if(null != json && !"".equals(json)) {
                KissDeviceHandleRo handleRo = JSON.parseObject(json, KissDeviceHandleRo.class);
                handleRo.setMessageId(sessionId);
                if(null != handleRo && null != handleRo.getDeviceSerialNumber()
                        && !"".equals(handleRo.getDeviceSerialNumber())){
                    //设置用户token对应的数据
                    waterRedis.set(KissCommonConstant.KISS_COMMON_USER_WEBSOCKET_SESSION_PREFIX+handleRo.getKissToken() , sessionId , 300l);

                    KissDeviceRo ro = kissDeviceService.queryDeviceBySerialNumber(handleRo.getDeviceSerialNumber());
                    if(null != ro && null != ro.getDeviceStatus()
                            && ro.getDeviceStatus().intValue() == KissCommonContext.DeviceContext.ONLINE.getCode()){
                        Long time = waterRedis.ttl(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+handleRo.getDeviceSerialNumber());
                        String currentUser = waterRedis.queryString(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+handleRo.getDeviceSerialNumber());
                        if(null != time && time.longValue() > 0){
                            //直接返回
                            KissDeviceRo deviceRo = new KissDeviceRo();
                            deviceRo.setCmd("QUERY_DEVICE");
                            deviceRo.setSiteCode(ro.getSiteCode());
                            deviceRo.setDeviceStatus(KissCommonContext.DeviceContext.WORKING.getCode());
                            deviceRo.setRemainderTime(time);
                            if(null != currentUser && !"".equals(currentUser) && currentUser.equals(handleRo.getKissToken())){
                                deviceRo.setCurrentUser(1);
                            }
                            result.setRst(deviceRo);
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("查询成功");
                        }else {
                            //正在查询
                            KissDeviceStatusRo statusRo = new KissDeviceStatusRo();
                            //设备序列号
                            statusRo.setDid(handleRo.getDeviceSerialNumber());
                            //消息编号
                            statusRo.setMessageId(sessionId);
                            //用户token
                            statusRo.setKissToken(handleRo.getKissToken());
                            //sitecode
                            statusRo.setSiteCode(ro.getSiteCode());
                            boolean sendFlag = sendMessage(JSON.toJSONString(statusRo) , handleRo.getDeviceSerialNumber());
                            if(sendFlag){
                                KissDeviceRo sucDeviceRo = new KissDeviceRo();
                                sucDeviceRo.setSiteCode(ro.getSiteCode());
                                result.setRst(sucDeviceRo);
                                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                result.setMsg("消息发送成功");
                            }else {
                                result.setMsg("该设备不在线(ERR:NOT SEND)");
                            }
                        }
                    }else {
                        result.setMsg("该设备不在线(ERR:NOT ONLINE)");
                    }
                }else {
                    result.setMsg("设备参数有误");
                }
            }else {
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return JSON.toJSONString(result);
    }


    /**
     * 恢复设备
     * @param json
     * @param sessionId
     * @return
     */
    private String recoverDevice(String json , String sessionId){
        WaterBootResultBean<KissDeviceHandleRo> result = new WaterBootResultBean<KissDeviceHandleRo>();
        try {
            boolean flag = true;
            if(null != json && !"".equals(json)){
                KissDeviceHandleRo handleRo = JSON.parseObject(json , KissDeviceHandleRo.class);
                if(flag && null == handleRo){
                    flag = false;
                    result.setMsg("参数错误");
                }else {
                    handleRo.setMessageId(sessionId);
                    //设置用户token对应的数据
                    waterRedis.set(KissCommonConstant.KISS_COMMON_USER_WEBSOCKET_SESSION_PREFIX+handleRo.getKissToken() , sessionId , 300l);
                }

                if(flag && (null == handleRo.getOrderNumber() || handleRo.getOrderNumber().length() < 20)
                        || !KissCommonOrderUtil.validateOrderNumber(handleRo.getOrderNumber() , minOrderDate)){
                    flag = false;
                    result.setMsg("订单号码错误");
                }
                if(flag && (null == handleRo.getGoodsCode() || "".equals(handleRo.getGoodsCode()))){
                    flag = false;
                    result.setMsg("商品编码错误");
                }
                if(flag && (null == handleRo.getDeviceSerialNumber() || "".equals(handleRo.getDeviceSerialNumber()))){
                    flag = false;
                    result.setMsg("设备序列号错误");
                }

                if(flag){
                    KissOrderRo orderRo = kissOrderService.queryOrderByCode(handleRo.getOrderNumber() , minOrderDate);
                    if(null != orderRo && null != orderRo.getOrderNumber()){
                        if(!handleRo.getOrderNumber().equals(orderRo.getOrderNumber())){
                            flag = false;
                            result.setMsg("订单号码不匹配");
                        }
                        if(flag && (!handleRo.getGoodsCode().equals(orderRo.getPayGoods()))){
                            flag = false;
                            result.setMsg("商品编号不匹配");
                        }
                        if(flag && (null == orderRo.getOrderTime())){
                            flag = false;
                            result.setMsg("订单号时间缺失");
                        }
                        //通过序列号查询订单
                        if(flag){
                            KissDeviceRo deviceRo = kissDeviceService.queryDeviceBySerialNumber(handleRo.getDeviceSerialNumber());
                            if(null == deviceRo || null == deviceRo.getDeviceCode()){
                                flag = false;
                                result.setMsg("未找到设备信息");
                            }
                            if(flag && (!deviceRo.getDeviceSerialNumber().equals(handleRo.getDeviceSerialNumber()))){
                                flag = false;
                                result.setMsg("设备序列号错误");
                            }
                            //验证是否超时
                            if(flag){
                                Date now = new Date();
                                Date expireDate = WaterDateUtil.addMinutes(orderRo.getOrderTime() , 60);
                                if(WaterDateUtil.compareDate(expireDate , now) < 1){
                                    flag = false;
                                    result.setMsg("该订单已过期");
                                }
                            }
                            if(flag){
                                Long time = waterRedis.ttl(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+handleRo.getDeviceSerialNumber());
                                //订单时间大于50秒（前端用60秒进行判断）
                                if(null != time && time.intValue() >= 50l){
                                    KissDeviceStartRo startRo = new KissDeviceStartRo();
                                    startRo.setDid(handleRo.getDeviceSerialNumber());
                                    startRo.setMessageId(sessionId);
                                    startRo.setPrd(time.intValue());
                                    //用户token
                                    startRo.setKissToken(handleRo.getKissToken());
                                    boolean sendFlag = sendMessage(JSON.toJSONString(startRo) , handleRo.getDeviceSerialNumber());
                                    if(sendFlag){
                                        result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                        result.setMsg("消息发送成功");
                                    }else {
                                        result.setMsg("该设备不在线(ERR:NOT SEND)");
                                    }
                                }else {
                                    flag = false;
                                    result.setMsg("该订单已完成");
                                }
                            }
                        }
                    }else {
                        flag = false;
                        result.setMsg("未找到订单信息");
                    }
                }

            }else {
                flag = false;
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 启动设备
     * @return
     */
    private String startDevice(String json, String sessionId){
        WaterBootResultBean<KissDeviceHandleRo> result = new WaterBootResultBean<KissDeviceHandleRo>();
        try {
            boolean flag = true;
            if(null != json && !"".equals(json)){
                KissDeviceHandleRo handleRo = JSON.parseObject(json , KissDeviceHandleRo.class);
                if(flag && null == handleRo){
                    flag = false;
                    result.setMsg("参数错误");
                }else {
                    handleRo.setMessageId(sessionId);
                    //设置用户token对应的数据
                    waterRedis.set(KissCommonConstant.KISS_COMMON_USER_WEBSOCKET_SESSION_PREFIX+handleRo.getKissToken() , sessionId , 300l);
                }

                if(flag && (null == handleRo.getOrderNumber() || handleRo.getOrderNumber().length() < 20)
                        || !KissCommonOrderUtil.validateOrderNumber(handleRo.getOrderNumber() , minOrderDate)){
                    flag = false;
                    result.setMsg("订单号码错误");
                }
                if(flag && (null == handleRo.getGoodsCode() || "".equals(handleRo.getGoodsCode()))){
                    flag = false;
                    result.setMsg("商品编码错误");
                }
                if(flag && (null == handleRo.getDeviceSerialNumber() || "".equals(handleRo.getDeviceSerialNumber()))){
                    flag = false;
                    result.setMsg("设备序列号错误");
                }

                if(flag){
                    KissOrderRo orderRo = kissOrderService.queryOrderByCode(handleRo.getOrderNumber() , minOrderDate);
                    if(null != orderRo && null != orderRo.getOrderNumber()){
                        if(!handleRo.getOrderNumber().equals(orderRo.getOrderNumber())){
                            flag = false;
                            result.setMsg("订单号码不匹配");
                        }
                        if(flag && (!handleRo.getGoodsCode().equals(orderRo.getPayGoods()))){
                            flag = false;
                            result.setMsg("商品编号不匹配");
                        }
                        if(flag && (null == orderRo.getOrderTime())){
                            flag = false;
                            result.setMsg("订单号时间缺失");
                        }
                        //TODO
                        /**
                         if(flag && (null == orderRo.getPayStatus()
                         ||  KissCommonContext.PayStatusContext.TRADE_SUCCESS.getCode() != orderRo.getPayStatus().intValue())){
                         flag = false;
                         result.setMsg("该订单还未支付完成，请稍后");
                         }
                         **/
                        //通过序列号查询订单
                        if(flag){
                            KissDeviceRo deviceRo = kissDeviceService.queryDeviceBySerialNumber(handleRo.getDeviceSerialNumber());
                            if(null == deviceRo || null == deviceRo.getDeviceCode()){
                                flag = false;
                                result.setMsg("未找到设备信息");
                            }
                            if(flag && (!deviceRo.getDeviceSerialNumber().equals(handleRo.getDeviceSerialNumber()))){
                                flag = false;
                                result.setMsg("设备序列号错误");
                            }
                            //验证是否超时
                            if(flag){
                                Date now = new Date();
                                Date expireDate = WaterDateUtil.addMinutes(orderRo.getOrderTime() , 60);
                                if(WaterDateUtil.compareDate(expireDate , now) < 1){
                                    flag = false;
                                    result.setMsg("该订单已过期");
                                }
                            }
                            if(flag){
                                int time = queryGoodsTime(handleRo.getGoodsCode());
                                if(time >0){
                                    KissDeviceStartRo startRo = new KissDeviceStartRo();
                                    startRo.setDid(handleRo.getDeviceSerialNumber());
                                    startRo.setMessageId(sessionId);
                                    startRo.setPrd(time);
                                    //用户token
                                    startRo.setKissToken(handleRo.getKissToken());
                                    boolean sendFlag = sendMessage(JSON.toJSONString(startRo) , handleRo.getDeviceSerialNumber());
                                    if(sendFlag){
                                        result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                        result.setMsg("消息发送成功");
                                    }else {
                                        result.setMsg("该设备不在线(ERR:NOT SEND)");
                                    }
                                }else {
                                    result.setMsg("该订单时间错误");
                                }
                            }
                        }
                    }else {
                        flag = false;
                        result.setMsg("未找到订单信息");
                    }
                }

            }else {
                flag = false;
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 停止设备
     * @return
     */
    private String stopDevice(String json, String sessionId){
        WaterBootResultBean<KissDeviceHandleRo> result = new WaterBootResultBean<KissDeviceHandleRo>();
        try {
            boolean flag = true;
            if(null != json && !"".equals(json)){
                KissDeviceHandleRo handleRo = JSON.parseObject(json , KissDeviceHandleRo.class);
                if(null != handleRo){
                    handleRo.setMessageId(sessionId);
                    //设置用户token对应的数据
                    waterRedis.set(KissCommonConstant.KISS_COMMON_USER_WEBSOCKET_SESSION_PREFIX+handleRo.getKissToken() , sessionId , 300l);
                }
                if(flag && (null == handleRo.getDeviceSerialNumber() || "".equals(handleRo.getDeviceSerialNumber()))){
                    flag = false;
                    result.setMsg("设备序列号错误");
                }
                if(flag && (null == handleRo.getKissToken() || "".equals(handleRo.getKissToken()))){
                    flag = false;
                    result.setMsg("用户token有误");
                }

                if(flag){
                    handleRo.setMessageId(sessionId);
                    //通过did
                    String currentUser = waterRedis.queryString(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+handleRo.getDeviceSerialNumber());
                    if(null != currentUser && !"".equals(currentUser) && !currentUser.equals(handleRo.getKissToken())){
                        flag = false;
                        result.setMsg("只能使用者停止此设备");
                    }
                    if(flag){
                        KissDeviceStopRo stopRo = new KissDeviceStopRo();
                        stopRo.setDid(handleRo.getDeviceSerialNumber());
                        stopRo.setMessageId(sessionId);
                        stopRo.setKissToken(handleRo.getKissToken());
                        boolean sendFlag = sendMessage(JSON.toJSONString(stopRo) , handleRo.getDeviceSerialNumber());
                        if(sendFlag){
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("消息发送成功");
                        }else {
                            result.setMsg("该设备不在线(ERR:NOT SEND)");
                        }
                    }
                }

            }else {
                flag = false;
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 查询商品剩余时间
     * @param goodsCode
     * @return
     */
    private int queryGoodsTime(String goodsCode){
        int time = 0;
        KissGoodsRo ro = kissGoodsService.queryGoodsByCode(goodsCode);
        if(null != ro && null != ro.getGoodsCode()
                && goodsCode.equals(ro.getGoodsCode())
                && null != ro.getGoodsUnit()){
            time = ro.getGoodsUnit().intValue()*60;
        }
        return time;
    }


}
