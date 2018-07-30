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


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 通用初始化
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Component
@Qualifier("springProtocolInitializer")
public class KissStringProtocolInitalizer extends ChannelInitializer<SocketChannel> {

    @Autowired
    StringDecoder stringDecoder;

    @Autowired
    StringEncoder stringEncoder;

    @Autowired
    KissIdleStateTriggerHandler kissIdleStateTriggerHandler;

    @Autowired
    KissServerHandler kissServerHandler;

    /** 超时时间 */
    @Value("${com.zjtachao.fish.kiss.netty.timeout.all}")
    private int timeout;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        ByteBuf delimiter = Unpooled.copiedBuffer("}".getBytes());
        ChannelHandler[] handlers = new ChannelHandler[]{new IdleStateHandler(timeout , timeout ,timeout , TimeUnit.SECONDS) ,
                new DelimiterBasedFrameDecoder(1024 , delimiter),
                kissIdleStateTriggerHandler ,
                stringDecoder ,
                stringEncoder ,
                kissServerHandler};
        pipeline.addLast(handlers);
        /**
        pipeline.addLast("idleHandle" , kissTriggerHandler);
        pipeline.addLast("kissIdleStateTriggerHandler" , kissIdleStateTriggerHandler);
        pipeline.addLast("decoder", stringDecoder);
        pipeline.addLast("encoder", stringEncoder);
        pipeline.addLast("handler", kissServerHandler);
         **/
    }

    public StringDecoder getStringDecoder() {
        return stringDecoder;
    }

    public void setStringDecoder(StringDecoder stringDecoder) {
        this.stringDecoder = stringDecoder;
    }

    public StringEncoder getStringEncoder() {
        return stringEncoder;
    }

    public void setStringEncoder(StringEncoder stringEncoder) {
        this.stringEncoder = stringEncoder;
    }

    public KissServerHandler getKissServerHandler() {
        return kissServerHandler;
    }

    public void setKissServerHandler(KissServerHandler kissServerHandler) {
        this.kissServerHandler = kissServerHandler;
    }

}
