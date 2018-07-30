/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.site.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissSite;
import com.zjtachao.fish.kiss.common.bean.ro.KissSiteRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissStaOrderRo;
import com.zjtachao.fish.kiss.common.bean.so.KissSiteSo;
import com.zjtachao.fish.kiss.manage.mapper.KissSiteMapper;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 场所管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissSiteServiceImpl implements com.zjtachao.fish.kiss.manage.service.site.KissSiteService {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 场所mapper **/
    @Autowired
    private KissSiteMapper kissSiteMapper;

    /**
     * 查询ro
     * @param code
     * @return
     */
    @Override
    public KissSiteRo querySiteByCode(String code){
        KissSiteSo so = new KissSiteSo();
        so.setSiteCode(code);
        KissSite site = kissSiteMapper.querySiteByCode(so);
        KissSiteRo ro = waterDozer.convert(site , KissSiteRo.class);
        return ro;
    }

    /**
     * 查询列表
     * @param so
     * @return
     */
    @Override
    public List<KissSiteRo> querySiteList(KissSiteSo so){
        List<KissSite> list = kissSiteMapper.querySiteList(so);
        List<KissSiteRo> roList = waterDozer.convertList(list , KissSiteRo.class);
        if(null != roList && !roList.isEmpty()){

        }
        return roList;
    }

    /**
     * 查询数量
     * @param so
     * @return
     */
    public Long querySiteCount(KissSiteSo so){
        Long result = kissSiteMapper.querySiteCount(so);
        return result;
    }

    /**
     * 添加场所
     * @param ro
     */
    @Override
    public void addSite(KissSiteRo ro){
        KissSite site = waterDozer.convert(ro , KissSite.class);
        Date date = new Date();
        site.setCreateTime(date);
        site.setModifyTime(date);
        site.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissSiteMapper.addSite(site);
    }

    /**
     * 修改场所
     * @param ro
     */
    @Override
    public void updateSite(KissSiteRo ro){
        KissSiteSo so = new KissSiteSo();
        so.setSiteCode(ro.getSiteCode());
        KissSite site = kissSiteMapper.querySiteByCode(so);
        if(null != site){
            //设置名称
            site.setSiteName(ro.getSiteName());
            //设置省级
            site.setAreaProvince(ro.getAreaProvince());
            //设置市级
            site.setAreaCity(ro.getAreaCity());
            //设置区级
            site.setAreaCounty(ro.getAreaCounty());
            //设置详情
            site.setAreaDetail(ro.getAreaDetail());

            Date date = new Date();
            //设置时间
            site.setModifyTime(date);
            kissSiteMapper.updateSite(site);
        }
    }

    /**
     * 删除场所
     * @param code
     */
    @Override
    public void deleteSite(String code){
        KissSite site = new KissSite();
        site.setSiteCode(code);
        Date date = new Date();
        site.setModifyTime(date);
        site.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        kissSiteMapper.deleteSite(site);
    }


}
