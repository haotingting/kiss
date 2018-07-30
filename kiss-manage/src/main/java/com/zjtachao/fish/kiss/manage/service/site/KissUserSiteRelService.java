/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.site;

import com.zjtachao.fish.kiss.common.bean.ro.KissUserSiteRelRo;

import java.util.List;

/**
 * 查询用户site列表
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissUserSiteRelService {
    /**
     * 查询用户site的列表
     * @return
     */
    List<KissUserSiteRelRo> queryUserSiteRelByUserCode(String userCode);

    /**
     * 添加用户与站点关系
     * @param userCode
     * @param siteCode
     */
    public void addUserSiteRel(String userCode , String siteCode);

    /**
     * 通过用户编码删除数据
     * @param userCode
     */
    public void deleteUserSiteRelByUserCode(String userCode);

    /**
     * 通过用户编码删除数据
     * @param userCode
     */
    public void deleteUserSiteRelByUserCodeAndSite(String userCode , String siteCode);
}
