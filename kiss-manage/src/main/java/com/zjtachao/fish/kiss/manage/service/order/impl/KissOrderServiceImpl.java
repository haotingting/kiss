/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.order.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissOrder;
import com.zjtachao.fish.kiss.common.bean.ro.KissOrderRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissStaOrderRo;
import com.zjtachao.fish.kiss.common.bean.so.KissOrderSo;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.mapper.KissOrderMapper;
import com.zjtachao.fish.water.common.data.WaterDozer;
import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissOrderServiceImpl implements com.zjtachao.fish.kiss.manage.service.order.KissOrderService {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 订单mapper **/
    @Autowired
    private KissOrderMapper kissOrderMapper;

    /**
     * 查询RO列表
     * @param so
     * @return
     */
    @Override
    public List<KissOrderRo> queryOrderList(KissOrderSo so){
        List<KissOrder> list = kissOrderMapper.queryOrderList(so);
        List<KissOrderRo> roList = waterDozer.convertList(list , KissOrderRo.class);
        return roList;
    }

    /**
     * 查询数量
     * @param so
     * @return
     */
    @Override
    public Long queryOrderCount(KissOrderSo so){
        Long count = kissOrderMapper.queryOrderCount(so);
        return count;
    }

    /**
     * 查询编码
     * @param code
     * @return
     */
    @Override
    public KissOrderRo queryOrderByCode(String code , String tbDate){
        KissOrderSo so = new KissOrderSo();
        so.setOrderNumber(code);
        so.setTbDate(tbDate);
        KissOrder order = kissOrderMapper.queryOrderByCode(so);
        KissOrderRo ro = waterDozer.convert(order , KissOrderRo.class);
        return ro;
    }

    /**
     * 查询统计订单列表
     * @param staDate
     * @return
     */
    public List<KissStaOrderRo> queryStaOrderList(Date staDate){
        KissOrderSo so = new KissOrderSo();
        String tbDate = WaterDateUtil.date2Str(staDate , "yyyyMM");
        so.setTbDate(tbDate);
        Date startTime = WaterDateUtil.moveBeginOfDay(staDate);
        Date endTime = WaterDateUtil.addDays(staDate , 1);
        so.setStartTime(startTime);
        so.setEndTime(endTime);
        so.setPayStatus(KissCommonContext.PayStatusContext.TRADE_SUCCESS.getCode());
        List<KissStaOrderRo> list = kissOrderMapper.queryStaOrderList(so);
        return list;
    }

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
                                                          String deviceSerialNumber , String payWay){
        KissOrderSo so = new KissOrderSo();
        String tbDate = WaterDateUtil.date2Str(startDate , "yyyyMM");
        so.setTbDate(tbDate);
        Date startTime = WaterDateUtil.moveBeginOfDay(startDate);
        so.setStartTime(startTime);
        so.setEndTime(endDate);
        so.setPayStatus(KissCommonContext.PayStatusContext.TRADE_SUCCESS.getCode());
        if(null != stieCode && !"".equals(stieCode)){
            so.setStieCode(stieCode);
        }
        if(null != goodsCode && !"".equals(goodsCode)){
            so.setGoodsCode(goodsCode);
        }
        if(null != deviceSerialNumber && !"".equals(deviceSerialNumber)){
            so.setDeviceSerialNumber(deviceSerialNumber);
        }
        if(null != payWay && !"".equals(payWay)){
            so.setPayWay(payWay);
        }
        List<KissStaOrderRo> list = kissOrderMapper.queryStaOrderRealTimeList(so);
        return list;
    }


}
