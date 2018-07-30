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

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Component
public class KissNettyAsyncTask {

    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(KissNettyAsyncTask.class);

    /**
     * 启动服务
     */
    @Async
    public void nettyChannelStart(KissNettyConnect connect){
        try {
            connect.start();
        }catch (Exception ex){
            logger.error("启动出错" ,ex);
        }

    }

    /**
     * 登录
     */
    @Async
    public void nettyLogin() throws Exception{
        while (true){
            logger.info("开始同步状态，大小："+KissClientMap.size());
            if(KissClientMap.size() > 0){
                for(Channel channel : KissClientMap.getAll()){
                    try {
                        if(channel.isActive()){
                            Random random = new Random();
                            int text = 1;//random.nextInt(9);
                            String msg = null;
                            if(text % 2 == 0){
                                msg = "{\"cmd\":\"sync\",\"did\":\""+KissClientContants.CLIENT_CODE+"\",\"err\":0}{\"cmd\":\"sync\",\"did\":\""+KissClientContants.CLIENT_CODE+"\",\"err\":0}";
                            }else {
                                msg = "{\"cmd\":\"sync\",\"did\":\""+KissClientContants.CLIENT_CODE+"\",\"err\":0}";
                            }
                            logger.info("同步状态，消息："+msg);
                            channel.writeAndFlush(msg);
                            //channel.writeAndFlush("{\"cmd\":\"gds\",\"did\":\"a1708110739\",\"kissToken\":\"0cb09ae521094969bf4a295917e63baf\",\"messageId\":\"2\",\"siteCode\":\"123456\"}");

                        }
                    }catch (Exception ex){
                        logger.error("登录出错" , ex);
                    }
                }
            }
            TimeUnit.SECONDS.sleep(6);
        }
    }

    /**
     * 启动服务
     * @param address
     * @param kissNettyAsyncTask
     */
    @Async
    public void nettyStart(String address , KissNettyAsyncTask kissNettyAsyncTask) throws Exception{
        try {
            if (null != address && !"".equals(address) && address.contains(":")) {
                String[] addressArr = address.split(":");
                String ip = addressArr[0];
                int port = Integer.parseInt(addressArr[1]);

                while (true) {
                    if (!KissClientMap.contain(address)){
                        try {
                            logger.info("开始加载启动服务:"+address);
                            KissNettyConnect connect = new KissNettyConnect(ip, port);
                            kissNettyAsyncTask.nettyChannelStart(connect);
                            logger.info("完成加载启动服务:"+address);
                        }catch (Exception ex){
                            logger.error("加载服务客户端出错" , ex);
                        }
                    }
                    TimeUnit.SECONDS.sleep(3);
                }
            }else{
                logger.error("启动失败");
                throw new Exception("参数错误，com.zjtachao.fish.kiss.client.normal.param:"+address);
            }
        }catch (Exception ex){
            logger.error("加载服务客户端出错" , ex);
            throw ex;
        }
    }

}
