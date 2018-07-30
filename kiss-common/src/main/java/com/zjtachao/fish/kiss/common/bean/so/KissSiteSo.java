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
 * 场所查询so
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissSiteSo extends WaterBootBaseSo {

    /** 序列号 **/
    private static final long serialVersionUID = 7091633528990370067L;

    /** 场所编码 **/
    private String siteCode;

    /** 场所名称 **/
    private String siteName;

    /** 场所省 **/
    private String areaProvince;

    /** 场所市 **/
    private String areaCity;

    /** 场所区 **/
    private String areaCounty;

    /** 管理员用户编码 **/
    private String adminUserCode;

    /** 用户角色 **/
    private String userRole;

    /** 删除标志 **/
    private String deleteFlag = WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode();


    public String getAdminUserCode() {
        return adminUserCode;
    }

    public void setAdminUserCode(String adminUserCode) {
        this.adminUserCode = adminUserCode;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
