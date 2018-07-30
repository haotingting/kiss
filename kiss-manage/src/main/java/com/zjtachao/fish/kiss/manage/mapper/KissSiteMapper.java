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

import com.zjtachao.fish.kiss.common.bean.domain.KissSite;
import com.zjtachao.fish.kiss.common.bean.so.KissSiteSo;

import java.util.List;

/**
 * 场所mapper
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissSiteMapper {

    /**
     * 查询单条 通过code
     * @param so
     * @return
     */
    public KissSite querySiteByCode(KissSiteSo so);

    /**
     * 查询列表
     * @param so
     * @return
     */
    public List<KissSite> querySiteList(KissSiteSo so);

    /**
     * 查询数量
     * @param so
     * @return
     */
    public Long querySiteCount(KissSiteSo so);

    /**
     * 添加场所
     * @param site
     */
    public void addSite(KissSite site);

    /**
     * 修改场所
     * @param site
     */
    public void updateSite(KissSite site);

    /**
     * 删除场所
     * @param site
     */
    public void deleteSite(KissSite site);

}
