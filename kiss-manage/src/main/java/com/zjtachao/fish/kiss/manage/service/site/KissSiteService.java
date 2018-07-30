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

import com.zjtachao.fish.kiss.common.bean.ro.KissSiteRo;
import com.zjtachao.fish.kiss.common.bean.so.KissSiteSo;

import java.util.List;

/**
 * 请在此处添加注释
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissSiteService {
    /**
     * 查询ro
     * @param code
     * @return
     */
    KissSiteRo querySiteByCode(String code);

    /**
     * 查询列表
     * @param so
     * @return
     */
    List<KissSiteRo> querySiteList(KissSiteSo so);

    /**
     * 查询数量
     * @param so
     * @return
     */
    public Long querySiteCount(KissSiteSo so);

    /**
     * 添加场所
     * @param ro
     */
    void addSite(KissSiteRo ro);

    /**
     * 修改场所
     * @param ro
     */
    void updateSite(KissSiteRo ro);

    /**
     * 删除站点
     * @param code
     */
    void deleteSite(String code);
}
