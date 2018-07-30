/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.client.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Kiss连接参数
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KIssClientHandler extends SimpleChannelInboundHandler<String> {

    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(KIssClientHandler.class);

    /**
     * 连接上
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("连接上");
        InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
        KissClientMap.add(address.getHostString()+":"+ address.getPort() , ctx.channel());
        logger.info("IP地址："+ address.getHostString()+":"+ address.getPort());
    }

    /**
     * 读取数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        logger.info("client channelRead..");
        logger.info("Client received:" + msg);
        try{
            if(msg.contains("{") && msg.contains("}")){
                JSONObject jsonObject = JSON.parseObject(msg);
                if(jsonObject.containsKey("cmd")){
                    String result = null;
                    if(jsonObject.getString("cmd").equals("log5")){
                        //登录
                        result = "{\"ack\":\""+jsonObject.getString("cmd")+"\",\"err\":0}";
                    }else if(jsonObject.getString("cmd").equals("gds")) {
                        //获取设备状态
                        result = "{\"ack\":\""+jsonObject.getString("cmd")+"\",\"err\":0}";
                    }else if(jsonObject.getString("cmd").equals("dc1")) {
                        //开启设备
                        result = "{\"ack\":\""+jsonObject.getString("cmd")+"\",\"err\":0}";
                    }else if(jsonObject.getString("cmd").equals("dc2")) {
                        //关闭设备
                        result = "{\"ack\":\""+jsonObject.getString("cmd")+"\",\"err\":0}";
                    }
                    if(null != result && !"".equals(result)){
                        ctx.channel().writeAndFlush(result);
                    }

                }
            }
        }catch (Exception ex){
            logger.error("读取异常："+msg , ex);
        }
    }

    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
        KissClientMap.remove(address.getHostString()+":"+ address.getPort());
        super.channelInactive(ctx);
        logger.info("断开连接");
    }

    /**
     * 异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("异常");
        cause.printStackTrace();
        ctx.close();
    }
}
