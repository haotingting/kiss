/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.task;

import com.zjtachao.fish.kiss.manage.service.sta.KissStaOrderService;
import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Component
public class KissStaOrderTask {


    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(KissStaOrderTask.class);

    /** 生成统计报表 **/
    @Autowired
    private KissStaOrderService kissStaOrderService;

    /**
     * 生成统计报表
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void executeGenStaOrder(){
        try{
            logger.info("开始生成统计报表");
            Date genDate = WaterDateUtil.addDays(new Date() , -1);
            kissStaOrderService.genStaOrderList(genDate);
            logger.info("结束生成统计报表");
        }catch (Exception ex){
            logger.error("生成统计报表出错" ,ex);
        }
    }

}
