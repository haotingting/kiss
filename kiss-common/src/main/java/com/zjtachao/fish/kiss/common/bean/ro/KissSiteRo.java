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

/**
 * 场所RO
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissSiteRo extends WaterBootBaseRo {

    /** 序列号 **/
    private static final long serialVersionUID = 5079184739808876066L;

    /** 主键 **/
    private Long id;

    /** 场所编码 **/
    private String siteCode;

    /** 场所名称 **/
    private String siteName;

    /** 场所省 **/
    private String areaProvince;

    /** 场所省 **/
    private String areaProvinceName;

    /** 场所市 **/
    private String areaCity;

    /** 场所市 **/
    private String areaCityName;

    /** 场所区 **/
    private String areaCounty;

    /** 场所区 **/
    private String areaCountyName;

    /** 场所详情 **/
    private String areaDetail;

    public String getAreaProvinceName() {
        return areaProvinceName;
    }

    public void setAreaProvinceName(String areaProvinceName) {
        this.areaProvinceName = areaProvinceName;
    }

    public String getAreaCityName() {
        return areaCityName;
    }

    public void setAreaCityName(String areaCityName) {
        this.areaCityName = areaCityName;
    }

    public String getAreaCountyName() {
        return areaCountyName;
    }

    public void setAreaCountyName(String areaCountyName) {
        this.areaCountyName = areaCountyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAreaProvince() {
        return areaProvince;
    }

    public void setAreaProvince(String areaProvince) {
        this.areaProvince = areaProvince;
    }

    public String getAreaCity() {
        return areaCity;
    }

    public void setAreaCity(String areaCity) {
        this.areaCity = areaCity;
    }

    public String getAreaCounty() {
        return areaCounty;
    }

    public void setAreaCounty(String areaCounty) {
        this.areaCounty = areaCounty;
    }

    public String getAreaDetail() {
        return areaDetail;
    }

    public void setAreaDetail(String areaDetail) {
        this.areaDetail = areaDetail;
    }
}
