/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.service.order.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissOrder;
import com.zjtachao.fish.kiss.common.bean.ro.KissOrderRo;
import com.zjtachao.fish.kiss.common.bean.so.KissOrderSo;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.common.util.KissCommonOrderUtil;
import com.zjtachao.fish.kiss.data.mapper.KissOrderMapper;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 订单service
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissOrderServiceImpl implements com.zjtachao.fish.kiss.data.service.order.KissOrderService {

    /** 订单 **/
    @Autowired
    private KissOrderMapper kissOrderMapper;

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /**
     * 添加订单
     * @param ro
     */
    @Override
    public void addOrder(KissOrderRo ro){
        KissOrder order =  waterDozer.convert(ro , KissOrder.class);
        if(null != order && null != order.getOrderNumber() && order.getOrderNumber().length() >= 20){
            String tdDate = order.getOrderNumber().substring(0,6);
            order.setTbDate(tdDate);
            Date date  = new Date();
            order.setCreateTime(date);
            order.setModifyTime(date);
            order.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
            kissOrderMapper.addOrder(order);
        }
    }


    /**
     * 更新状态为成功
     * @param orderNumber
     * @param payNumber
     */
    public void updateOrderPaySuccess(String orderNumber , String payNumber , String minDate){
        KissOrder order = new KissOrder();
        if(null != orderNumber && KissCommonOrderUtil.validateOrderNumber(orderNumber , minDate)){
            String tdDate = KissCommonOrderUtil.getOrderNumberPrefix(orderNumber , minDate);
            order.setTbDate(tdDate);
            order.setOrderNumber(orderNumber);
            order.setPayNumber(payNumber);
            Date date = new Date();
            order.setPayTime(date);
            order.setModifyTime(date);
            order.setPayStatus(KissCommonContext.PayStatusContext.TRADE_SUCCESS.getCode());
            order.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
            kissOrderMapper.updateOrderStatus(order);
        }

    }

    /**
     * 通过编码查询订单
     * @param orderNumber
     * @return
     */
    public KissOrderRo queryOrderByCode(String orderNumber , String minDate){
        KissOrderRo orderRo = null;
        KissOrderSo so = new KissOrderSo();
        if(null != orderNumber && KissCommonOrderUtil.validateOrderNumber(orderNumber , minDate)){
            so.setOrderNumber(orderNumber);
            String tdDate = KissCommonOrderUtil.getOrderNumberPrefix(orderNumber , minDate);
            so.setTbDate(tdDate);
            KissOrder order = kissOrderMapper.queryOrderByCode(so);
            orderRo = waterDozer.convert(order , KissOrderRo.class);
        }
        return orderRo;
    }

}
