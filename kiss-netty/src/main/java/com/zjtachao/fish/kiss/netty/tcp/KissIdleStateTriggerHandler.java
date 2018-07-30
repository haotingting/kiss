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

import com.zjtachao.fish.water.common.tool.WaterIpUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * 连接超时触发器
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Component
@Qualifier("kissIdleStateTriggerHandler")
@ChannelHandler.Sharable
public class KissIdleStateTriggerHandler extends ChannelInboundHandlerAdapter{

    /** 日志 **/
    private static final Logger log = LoggerFactory.getLogger(KissIdleStateTriggerHandler.class);

    /**
     * 心跳触发
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean flag = true;
        try{
            //判断是否为局域网
            InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
            String host = address.getHostString();
            if(!WaterIpUtil.internalIp(host)){
                if(evt instanceof IdleStateEvent){
                    IdleStateEvent event = (IdleStateEvent)evt;
                    if (event.state() == IdleState.ALL_IDLE) {
                        log.info("连接超时");
                        ctx.channel().writeAndFlush("Connection timeout, auto close!\n").addListener((ChannelFuture ch) -> ch.channel().close());
                        flag = false;
                    }
                }
            }
        }catch (Exception ex){
            log.error("连接超时，关闭出错。"+ex.getMessage() , ex);
        }
        if(flag){
            super.userEventTriggered(ctx, evt);
        }
    }

}
