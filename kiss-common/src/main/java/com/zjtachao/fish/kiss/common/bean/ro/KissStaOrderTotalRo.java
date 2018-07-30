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

import com.zjtachao.fish.water.common.base.bean.WaterBootBaseRo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单壳
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissStaOrderTotalRo extends WaterBootBaseRo {

    /** 序列号 **/
    private static final long serialVersionUID = -8847268682537286076L;

    /** 总单量 **/
    private Integer totalCount;

    /** 总金额 **/
    private BigDecimal totalAmount;

    /** 当前单量 **/
    private Integer currentCount;

    /** 当前金额 **/
    private BigDecimal currentAmount;

    /** 详情列表 **/
    private List<KissStaOrderRo> detailList;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public List<KissStaOrderRo> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<KissStaOrderRo> detailList) {
        this.detailList = detailList;
    }
}
