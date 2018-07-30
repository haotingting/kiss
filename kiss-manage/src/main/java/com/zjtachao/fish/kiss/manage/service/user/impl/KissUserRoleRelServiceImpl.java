/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.user.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissUserRoleRel;
import com.zjtachao.fish.kiss.common.bean.ro.KissUserRoleRelRo;
import com.zjtachao.fish.kiss.manage.mapper.KissUserRoleRelMapper;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户角色服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissUserRoleRelServiceImpl implements com.zjtachao.fish.kiss.manage.service.user.KissUserRoleRelService {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 用户角色 **/
    @Autowired
    private KissUserRoleRelMapper kissUserRoleRelMapper;

    /**
     * 通过ro查询
     * @param code
     * @param role
     * @return
     */
    public KissUserRoleRelRo findByCode(String code ,String  role){
        KissUserRoleRel rel = new KissUserRoleRel();
        rel.setUserCode(code);
        rel.setRoleCode(role);
        rel.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        KissUserRoleRel find = kissUserRoleRelMapper.findRoleByUserCode(rel);
        KissUserRoleRelRo ro = waterDozer.convert(find , KissUserRoleRelRo.class);
        return ro;
    }

    /**
     * 添加管理角色
     * @param code
     * @param role
     */
    @Override
    public void add(String code, String role){
        KissUserRoleRel rel = new KissUserRoleRel();
        rel.setUserCode(code);
        rel.setRoleCode(role);
        Date date = new Date();
        rel.setCreateTime(date);
        rel.setModifyTime(date);
        rel.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissUserRoleRelMapper.addUserRoleRel(rel);
    }

    /**
     * 取消角色
     * @param code
     * @param role
     */
    @Override
    public void delete(String code, String role){
        KissUserRoleRel rel = new KissUserRoleRel();
        rel.setUserCode(code);
        rel.setRoleCode(role);
        Date date = new Date();
        rel.setModifyTime(date);
        rel.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        kissUserRoleRelMapper.deleteUserRoleRel(rel);
    }

    /**
     * 取消角色
     * @param code
     */
    @Override
    public void deleteByUserCode(String code){
        KissUserRoleRel rel = new KissUserRoleRel();
        rel.setUserCode(code);
        Date date = new Date();
        rel.setModifyTime(date);
        rel.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        kissUserRoleRelMapper.deleteByUserCode(rel);
    }

}
