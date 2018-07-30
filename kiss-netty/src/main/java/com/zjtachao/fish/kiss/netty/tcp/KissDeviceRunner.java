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

import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.water.common.data.WaterRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动类
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Component
public class KissDeviceRunner implements CommandLineRunner {

    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(KissDeviceRunner.class);

    /** redis **/
    @Autowired
    private WaterRedis waterRedis;

    /** 地址 **/
    @Value("${fish.websocket.address}")
    private String address;

    /**
     * 发送数据
     * @param strings
     * @throws Exception
     */
    @Override
    public void run(String... strings) throws Exception {
        try {
            if(null != address && !"".equals(address)){
                waterRedis.sadd(KissCommonConstant.KISS_COMMON_DEVICE_NETTY_PREFIX , address);
            }else {
                throw new Exception("地址错误，address:"+address);
            }
        }catch (Exception ex){
            logger.error("发送本机地址错误" , ex);
            throw ex;
        }
    }
}
