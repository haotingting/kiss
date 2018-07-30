/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.bean.so;

import com.zjtachao.fish.water.common.base.bean.WaterBootBaseSo;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;

import java.util.Date;

/**
 * 搜索条件
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissStaOrderSo extends WaterBootBaseSo {

    /** 序列号 **/
    private static final long serialVersionUID = 4028443169999783234L;

    /** 生成日期 **/
    private Date genDate;

    /** 天数 **/
    private Integer days;

    /** 最小天数 **/
    private Integer minPeriodDay;

    /** 最大天数 **/
    private Integer maxPeriodDay;

    /** 场所编码 **/
    private String siteCode;

    /** 设备编码 **/
    private String deviceSerialNumber;

    /** 商品编码 **/
    private String goodsCode;

    /** 支付方式 **/
    private String payWay;

    /** 删除标志 **/
    private String deleteFlag = WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode();

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getMinPeriodDay() {
        return minPeriodDay;
    }

    public void setMinPeriodDay(Integer minPeriodDay) {
        this.minPeriodDay = minPeriodDay;
    }

    public Integer getMaxPeriodDay() {
        return maxPeriodDay;
    }

    public void setMaxPeriodDay(Integer maxPeriodDay) {
        this.maxPeriodDay = maxPeriodDay;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
}
