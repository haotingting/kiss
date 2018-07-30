/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.bean.ro;

import com.zjtachao.fish.water.common.base.bean.WaterBootBaseDomain;
import com.zjtachao.fish.water.common.base.bean.WaterBootBaseRo;

import java.math.BigDecimal;

/**
 * 商品
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissGoodsRo extends WaterBootBaseRo {

    /** 序列号 **/
    private static final long serialVersionUID = -7512261666795213593L;

    /** 主键 **/
    private Long id;

    /** 场所编码 **/
    private String siteCode;

    /** 场所名称 **/
    private String siteName;

    /** 商品类型 暂时只有共享按摩椅 SHARE-CHAIR' **/
    private String goodsType;

    /** 商品编码 **/
    private String goodsCode;

    /** 商品名称 **/
    private String goodsName;

    /** 商品单价 **/
    private BigDecimal goodsPrice;

    /** 商品单位 **/
    private BigDecimal goodsUnit;

    /** 商品描述 **/
    private String goodsDesc;

    /** 商品状态 0-未发布 1-已发布 2-已下线 **/
    private Integer goodsStatus;

    /** 商品排序 **/
    private Integer goodsOrder;

    /** 备注 **/
    private String remark;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(BigDecimal goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Integer getGoodsOrder() {
        return goodsOrder;
    }

    public void setGoodsOrder(Integer goodsOrder) {
        this.goodsOrder = goodsOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
