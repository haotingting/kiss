/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.service.order;

import com.zjtachao.fish.kiss.common.bean.ro.KissOrderRo;

/**
 * 订单管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissOrderService {
    /**
     * 添加订单
     * @param ro
     */
    void addOrder(KissOrderRo ro);

    /**
     * 更新状态为成功
     * @param orderNumber
     * @param payNumber
     */
    public void updateOrderPaySuccess(String orderNumber , String payNumber , String minDate);

    /**
     * 通过编码查询订单
     * @param orderNumber
     * @return
     */
    public KissOrderRo queryOrderByCode(String orderNumber , String minDate);
}
