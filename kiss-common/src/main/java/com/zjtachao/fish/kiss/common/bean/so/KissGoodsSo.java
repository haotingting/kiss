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

/**
 * 商品查询条件
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissGoodsSo extends WaterBootBaseSo {

    /** 序列号 **/
    private static final long serialVersionUID = 8526181924998411476L;

    /** 场所编码 **/
    private String siteCode;

    /** 商品类型 暂时只有共享按摩椅 SHARE-CHAIR' **/
    private String goodsType;

    /** 商品编码 **/
    private String goodsCode;

    /** 商品名称 **/
    private String goodsName;

    /** 商品状态 0-未发布 1-已发布 2-已下线 **/
    private Integer goodsStatus;

    /** 删除标志 **/
    private String deleteFlag = WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode();

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
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

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }
}
