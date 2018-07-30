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
 * 查询搜索条件
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissPromoSo extends WaterBootBaseSo {

    /** 序列号 **/
    private static final long serialVersionUID = -8928922366263369217L;

    /** 优惠编码 **/
    private String promoCode;

    /** 场所编码 **/
    private String siteCode;

    /** 商品编码 **/
    private String goodsCode;

    /** 优惠码状态 **/
    private Integer promoStatus;

    /** 编码格式 **/
    private String codes;

    /** 有效期开始时间 **/
    private Date valiStartTime;

    /** 有效期结束时间 **/
    private Date valiEndTime;

    /** 删除标志 **/
    private String deleteFlag = WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode();

    public Date getValiStartTime() {
        return valiStartTime;
    }

    public void setValiStartTime(Date valiStartTime) {
        this.valiStartTime = valiStartTime;
    }

    public Date getValiEndTime() {
        return valiEndTime;
    }

    public void setValiEndTime(Date valiEndTime) {
        this.valiEndTime = valiEndTime;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Integer getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
