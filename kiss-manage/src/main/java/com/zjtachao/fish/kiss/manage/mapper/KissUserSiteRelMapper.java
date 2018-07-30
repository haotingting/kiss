/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.mapper;

import com.zjtachao.fish.kiss.common.bean.domain.KissUserSiteRel;
import com.zjtachao.fish.kiss.common.bean.so.KissUserSiteRelSo;

import java.util.List;

/**
 * 用户站点关联
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissUserSiteRelMapper {

    /**
     * 查询用户所有场所
     * @param so
     * @return
     */
    public List<KissUserSiteRel> queryUserSiteRelByUserCode(KissUserSiteRelSo so);

    /**
     * 添加用户和站点关系
     * @param rel
     */
    public void addUserSiteRel(KissUserSiteRel rel);

    /**
     * 通过用户编码删除
     * @param rel
     */
    public void deleteUserSiteRelByUserCode(KissUserSiteRel rel);

    /**
     * 通过用户编码和站点
     * @param rel
     */
    public void deleteUserSiteRelByUserCodeAndSite(KissUserSiteRel rel);

}
