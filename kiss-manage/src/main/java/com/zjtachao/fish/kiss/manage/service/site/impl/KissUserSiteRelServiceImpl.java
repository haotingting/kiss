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

import com.zjtachao.fish.kiss.common.bean.domain.KissUserSiteRel;
import com.zjtachao.fish.kiss.common.bean.ro.KissUserSiteRelRo;
import com.zjtachao.fish.kiss.common.bean.so.KissUserSiteRelSo;
import com.zjtachao.fish.kiss.manage.mapper.KissUserSiteRelMapper;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户站点相关服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissUserSiteRelServiceImpl implements com.zjtachao.fish.kiss.manage.service.site.KissUserSiteRelService {

    /** 用户站点关系mapper **/
    @Autowired
    private KissUserSiteRelMapper kissUserSiteRelMapper;

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /**
     * 查询用户site的列表
     * @return
     */
    @Override
    public List<KissUserSiteRelRo> queryUserSiteRelByUserCode(String userCode){
        KissUserSiteRelSo so = new KissUserSiteRelSo();
        so.setUserCode(userCode);
        List<KissUserSiteRel> list = kissUserSiteRelMapper.queryUserSiteRelByUserCode(so);
        List<KissUserSiteRelRo> roList = waterDozer.convertList(list , KissUserSiteRelRo.class);
        return roList;
    }

    /**
     * 添加用户与站点关系
     * @param userCode
     * @param siteCode
     */
    public void addUserSiteRel(String userCode , String siteCode){
        KissUserSiteRel rel = new KissUserSiteRel();
        rel.setUserCode(userCode);
        rel.setSiteCode(siteCode);
        Date date = new Date();
        rel.setCreateTime(date);
        rel.setModifyTime(date);
        rel.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissUserSiteRelMapper.addUserSiteRel(rel);
    }

    /**
     * 通过用户编码删除数据
     * @param userCode
     */
    public void deleteUserSiteRelByUserCode(String userCode){
        KissUserSiteRel rel = new KissUserSiteRel();
        rel.setUserCode(userCode);
        rel.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        Date date = new Date();
        rel.setModifyTime(date);
        kissUserSiteRelMapper.deleteUserSiteRelByUserCode(rel);
    }

    /**
     * 通过用户编码删除数据
     * @param userCode
     */
    public void deleteUserSiteRelByUserCodeAndSite(String userCode , String siteCode){
        KissUserSiteRel rel = new KissUserSiteRel();
        rel.setUserCode(userCode);
        rel.setSiteCode(siteCode);
        rel.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        Date date = new Date();
        rel.setModifyTime(date);
        kissUserSiteRelMapper.deleteUserSiteRelByUserCodeAndSite(rel);
    }

}
