/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.order;

import com.zjtachao.fish.kiss.common.bean.ro.KissOrderRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissStaOrderRo;
import com.zjtachao.fish.kiss.common.bean.so.KissOrderSo;

import java.util.Date;
import java.util.List;

/**
 * 订单
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissOrderService {
    /**
     * 查询RO列表
     * @param so
     * @return
     */
    List<KissOrderRo> queryOrderList(KissOrderSo so);

    /**
     * 查询数量
     * @param so
     * @return
     */
    Long queryOrderCount(KissOrderSo so);

    /**
     * 查询编码
     * @param code
     * @return
     */
    KissOrderRo queryOrderByCode(String code , String tbDate);

    /**
     * 查询统计订单列表
     * @param staDate
     * @return
     */
    public List<KissStaOrderRo> queryStaOrderList(Date staDate);

    /**
     * 查询实时数据
     * @param startDate
     * @param endDate
     * @param stieCode
     * @param goodsCode
     * @param deviceSerialNumber
     * @param payWay
     * @return
     */
    public List<KissStaOrderRo> queryStaOrderRealTimeList(Date startDate , Date endDate ,
                                                          String stieCode , String goodsCode ,
                                                          String deviceSerialNumber , String payWay);
}
