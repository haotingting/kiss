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

import java.util.Set;

/**
 * 添加用户site
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissBaseUserSiteRo extends WaterBootBaseRo {

    /** 序列号 **/
    private static final long serialVersionUID = -4547689336931296790L;

    /** 用户编码 **/
    private String userCode;

    /** 用户站点 **/
    private Set<String> userSites;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Set<String> getUserSites() {
        return userSites;
    }

    public void setUserSites(Set<String> userSites) {
        this.userSites = userSites;
    }
}
