/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.sta.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissStaOrder;
import com.zjtachao.fish.kiss.common.bean.ro.KissStaOrderRo;
import com.zjtachao.fish.kiss.common.bean.so.KissStaOrderSo;
import com.zjtachao.fish.kiss.manage.mapper.KissStaOrderMapper;
import com.zjtachao.fish.kiss.manage.service.order.KissOrderService;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单统计服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissStaOrderServiceImpl implements com.zjtachao.fish.kiss.manage.service.sta.KissStaOrderService {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 订单mapper **/
    @Autowired
    private KissStaOrderMapper kissStaOrderMapper;

    /** 订单服务 **/
    @Autowired
    private KissOrderService kissOrderService;

    /**
     * 查询订单服务
     * @param so
     * @return
     */
    @Override
    public List<KissStaOrderRo> queryStaOrderList(KissStaOrderSo so){
        List<KissStaOrder> list = kissStaOrderMapper.queryStaOrderList(so);
        return waterDozer.convertList(list , KissStaOrderRo.class);
    }

    /**
     * 生成统计报表
     * @param date
     */
    public void genStaOrderList(Date date){
        List<KissStaOrderRo> orderList = kissOrderService.queryStaOrderList(date);
        String dateStr = WaterDateUtil.date2Str(date , "yyyyMMdd");
        int periodDay = Integer.parseInt(dateStr);
        Date genDate = new Date();
        KissStaOrder delStaOrder = new KissStaOrder();
        delStaOrder.setPeriodDay(periodDay);
        delStaOrder.setModifyTime(genDate);
        delStaOrder.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        kissStaOrderMapper.deleteStaOrder(delStaOrder);
        if(null != orderList && !orderList.isEmpty()){
            for(KissStaOrderRo orderRo : orderList){
                orderRo.setPeriodDay(periodDay);
                KissStaOrder staOrder = waterDozer.convert(orderRo , KissStaOrder.class);
                staOrder.setCreateTime(genDate);
                staOrder.setModifyTime(genDate);
                kissStaOrderMapper.addStaOrder(staOrder);
            }
        }
    }

    /**
     * 查询统计数据
     * @param so
     * @return
     */
    public KissStaOrderRo queryStaOrderTotal(KissStaOrderSo so){
        KissStaOrder order = kissStaOrderMapper.queryStaOrderTotal(so);
        return waterDozer.convert(order , KissStaOrderRo.class);
    }

}
