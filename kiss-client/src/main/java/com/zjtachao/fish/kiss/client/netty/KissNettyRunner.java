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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 设备启动类
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Component
public class KissNettyRunner implements CommandLineRunner {

    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(KissNettyRunner.class);

    /** 最小日期 **/
    @Value("${com.zjtachao.fish.kiss.client.normal.param}")
    private String address;

    /** 异步执行 **/
    @Autowired
    private KissNettyAsyncTask kissNettyAsyncTask;

    /**
     * 加载启动类
     * @param strings
     * @throws Exception
     */
    @Override
    public void run(String... strings) throws Exception{
        kissNettyAsyncTask.nettyStart(address ,kissNettyAsyncTask);
    }
}
