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

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 连接服务器
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissDeviceTcpClient {

    /** 日志 **/
    private static final Logger log = LoggerFactory.getLogger(KissDeviceTcpClient.class);

    /** 服务器 **/
    private final String host;

    /** 端口号 **/
    private final int port;

    /** 渠道 **/
    protected Channel channel;

    /**
     * 构造方法
     * @param host
     * @param port
     */
    public KissDeviceTcpClient(String host , int port){
        this.host = host;
        this.port = port;
    }

    /**
     * 启动
     */
    public void start(){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            final ByteBuf delimiter = Unpooled.copiedBuffer("}".getBytes());
            b.group(group) // 注册线程池
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                    .remoteAddress(new InetSocketAddress(this.host, this.port)) // 绑定连接端口和host信息
                    .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            log.info("连接中");
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 , delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new KissDeviceTcpHandler());
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    });
            ChannelFuture cf = b.connect().sync();
            channel = cf.channel();
            cf.channel().closeFuture().sync();
        }catch (Exception ex){
            log.error("启动错误" , ex);
        }
    }

}
