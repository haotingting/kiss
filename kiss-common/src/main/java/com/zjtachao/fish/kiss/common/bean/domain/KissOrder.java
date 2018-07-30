/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.bean.domain;

import com.zjtachao.fish.water.common.base.bean.WaterBootBaseDomain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单基础表
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissOrder extends WaterBootBaseDomain {

    /** 序列号 **/
    private static final long serialVersionUID = -6668514238899938346L;

    /** 订单时间 **/
    private String tbDate;

    /** 主键 **/
    private Long id;

    /** 订单编号 **/
    private String orderNumber;

    /** 订单创建时间 **/
    private Date orderTime;

    /** 场所编号 **/
    private String  stieCode;

    /** 场所名称 **/
    private String  stieName;

    /** 设备编号 **/
    private String deviceCode;

    /** 设备序列号 **/
    private String deviceSerialNumber;

    /** 设备名称 **/
    private String deviceName;

    /** 支付方式 A-支付宝 W-微信支付 **/
    private String payWay;

    /** 支付来源 H5、APP、WEB、PROGRAM **/
    private String paySource;

    /** 支付编号 **/
    private String payNumber;

    /** 支付币种 默认CNY **/
    private String payCurrency;

    /** 支付商品名称 **/
    private String payGoods;

    /** 支付金额 **/
    private BigDecimal payAmount;

    /** 支付数量 **/
    private BigDecimal payQuantity;

    /** 支付标题 **/
    private String payTitle;

    /** 支付描述 **/
    private String payDesc;

    /** 支付备注 **/
    private String payRemark ;


    /**
     *
     * 	* 商户服务端交易创建 ,等待api应答
     * SELLER_CREATE(0,"SELLER_CREATE"),
     * api调用成功(支付订单创建成功)
     * INVOKE_SUCCESS(1,"INVOKE_SUCCESS"),
     * api调用失败(支付订单创建失败)
     * INVOKE_FAILED(2,"INVOKE_FAILED"),
     * api交易创建，等待付款
     * WAIT_BUYER_PAY(3 ,"WAIT_BUYER_PAY"),
     * * api未付款超时关闭或全额退款
     * TRADE_CLOSED(4 ,"TRADE_CLOSED"),
     * api交易支付成功
     * TRADE_SUCCESS(5 ,"TRADE_SUCCESS"),
     * api交易结束，不可退款
     * TRADE_FINISHED(6 ,"TRADE_FINISHED"),
     * 交易失败
     * TRADE_FAIL(7 ,"TRADE_FAIL");
      */
    private Integer payStatus;

    /** 支付时间 **/
    private Date payTime;

    /** 购买方用户编码 **/
    private String payBuyerCode;

    /** 购买方用户名称 **/
    private String payBuyerName;

    /** 卖方用户编码 **/
    private String paySellerCode;

    /** 卖方用户名称 **/
    private String paySellerName;

    /** 订单附加信息 **/
    private String orderRemark;


    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getTbDate() {
        return tbDate;
    }

    public void setTbDate(String tbDate) {
        this.tbDate = tbDate;
    }

    public String getStieName() {
        return stieName;
    }

    public void setStieName(String stieName) {
        this.stieName = stieName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getStieCode() {
        return stieCode;
    }

    public void setStieCode(String stieCode) {
        this.stieCode = stieCode;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPaySource() {
        return paySource;
    }

    public void setPaySource(String paySource) {
        this.paySource = paySource;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public String getPayCurrency() {
        return payCurrency;
    }

    public void setPayCurrency(String payCurrency) {
        this.payCurrency = payCurrency;
    }

    public String getPayGoods() {
        return payGoods;
    }

    public void setPayGoods(String payGoods) {
        this.payGoods = payGoods;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayQuantity() {
        return payQuantity;
    }

    public void setPayQuantity(BigDecimal payQuantity) {
        this.payQuantity = payQuantity;
    }

    public String getPayTitle() {
        return payTitle;
    }

    public void setPayTitle(String payTitle) {
        this.payTitle = payTitle;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public void setPayDesc(String payDesc) {
        this.payDesc = payDesc;
    }

    public String getPayRemark() {
        return payRemark;
    }

    public void setPayRemark(String payRemark) {
        this.payRemark = payRemark;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayBuyerCode() {
        return payBuyerCode;
    }

    public void setPayBuyerCode(String payBuyerCode) {
        this.payBuyerCode = payBuyerCode;
    }

    public String getPayBuyerName() {
        return payBuyerName;
    }

    public void setPayBuyerName(String payBuyerName) {
        this.payBuyerName = payBuyerName;
    }

    public String getPaySellerCode() {
        return paySellerCode;
    }

    public void setPaySellerCode(String paySellerCode) {
        this.paySellerCode = paySellerCode;
    }

    public String getPaySellerName() {
        return paySellerName;
    }

    public void setPaySellerName(String paySellerName) {
        this.paySellerName = paySellerName;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }
}
