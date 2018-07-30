/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.sta;

import com.zjtachao.fish.kiss.common.bean.ro.KissStaOrderRo;
import com.zjtachao.fish.kiss.common.bean.so.KissStaOrderSo;

import java.util.Date;
import java.util.List;

/**
 * 订单统计
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissStaOrderService {
    /**
     * 查询订单服务
     * @param so
     * @return
     */
    List<KissStaOrderRo> queryStaOrderList(KissStaOrderSo so);

    /**
     * 生成统计报表
     * @param date
     */
    public void genStaOrderList(Date date);

    /**
     * 查询统计数据
     * @param so
     * @return
     */
    public KissStaOrderRo queryStaOrderTotal(KissStaOrderSo so);
}
