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

import com.zjtachao.fish.kiss.common.bean.domain.KissBaseUser;
import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserWithPwdRo;
import com.zjtachao.fish.kiss.common.bean.so.KissBaseUserSo;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.mapper.KissBaseUserMapper;
import com.zjtachao.fish.kiss.manage.service.user.KissBaseUserService;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户查询
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissBaseUserServiceImpl implements KissBaseUserService {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 用户基本操作 **/
    @Autowired
    private KissBaseUserMapper kissBaseUserMapper;

    /**
     * 通过code查询用户
     * @param userCode
     * @return
     */
    public KissBaseUserWithPwdRo findBaseUserWithPwdByCode(String userCode){
        KissBaseUserSo so = new KissBaseUserSo();
        so.setUserCode(userCode);
        KissBaseUser user = kissBaseUserMapper.findBaseUserByCode(so);
        KissBaseUserWithPwdRo ro = waterDozer.convert(user , KissBaseUserWithPwdRo.class);
        return ro;
    }

    /**
     * 通过code查询用户
     * @param userCode
     * @return
     */
    public KissBaseUserRo findBaseUserByCode(String userCode){
        KissBaseUserSo so = new KissBaseUserSo();
        so.setUserCode(userCode);
        KissBaseUser user = kissBaseUserMapper.findBaseUserByCode(so);
        KissBaseUserRo ro = waterDozer.convert(user , KissBaseUserRo.class);
        return ro;
    }

    /**
     * 查询数量
     * @param so
     * @return
     */
    public Long findBaseUserCount(KissBaseUserSo so){
        Long count = kissBaseUserMapper.findBaseUserCount(so);
        return count;
    }

    /**
     * 查询列表
     * @param so
     * @return
     */
    public List<KissBaseUserRo> findBaseUserList(KissBaseUserSo so){
        List<KissBaseUser> userList = kissBaseUserMapper.findBaseUserList(so);
        List<KissBaseUserRo> roList = waterDozer.convertList(userList , KissBaseUserRo.class);
        return roList;
    }

    /**
     * 更新用户信息
     * @param ro
     */
    public void updateBaseUser(KissBaseUserWithPwdRo ro){
        KissBaseUserSo so = new KissBaseUserSo();
        so.setUserCode(ro.getUserCode());
        KissBaseUser user = kissBaseUserMapper.findBaseUserByCode(so);
        if(null != user){
            user.setUserMobile(ro.getUserMobile());
            user.setUserNickname(ro.getUserNickname());
            user.setSiteCode(ro.getSiteCode());
            Date date = new Date();
            user.setModifyTime(date);
            kissBaseUserMapper.updateBaseUser(user);
        }

    }

    /**
     * 修改状态
     */
    public void updateBaseUserStatus(String code , Integer status){
        KissBaseUser user = new KissBaseUser();
        user.setUserCode(code);
        user.setUserStatus(status);
        Date date = new Date();
        user.setModifyTime(date);
        user.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissBaseUserMapper.updateBaseUserStatus(user);
    }

    /**
     * 修改用户昵称
     * @param code
     * @param nickname
     */
    public void updateBaseUserNickname(String code , String nickname){
        KissBaseUser user = new KissBaseUser();
        user.setUserCode(code);
        user.setUserNickname(nickname);
        Date date = new Date();
        user.setModifyTime(date);
        user.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissBaseUserMapper.updateBaseUserNickname(user);
    }

    /**
     * 修改密码
     * @param code
     * @param pwd
     */
    public void updateBaseUserPwd(String code , String pwd){
        KissBaseUser user = new KissBaseUser();
        user.setUserCode(code);
        user.setUserPwd(pwd);
        Date date = new Date();
        user.setModifyTime(date);
        user.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissBaseUserMapper.updateBaseUserPwd(user);
    }

    /**
     * 修改用户场所
     * @param code
     * @param siteCode
     */
    public void updateBaseUserSite(String code , String siteCode){
        KissBaseUser user = new KissBaseUser();
        user.setUserCode(code);
        user.setSiteCode(siteCode);
        Date date = new Date();
        user.setModifyTime(date);
        user.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissBaseUserMapper.updateBaseUserSite(user);
    }

    /**
     * 添加用户
     * @param ro
     */
    public void insertBaseUser(KissBaseUserWithPwdRo ro){
        KissBaseUser user = waterDozer.convert(ro , KissBaseUser.class);
        Date date = new Date();
        user.setUserRegTime(date);
        user.setCreateTime(date);
        user.setModifyTime(date);
        user.setUserStatus(KissCommonContext.UserStatusContext.NORMAL.getCode());
        user.setUserRegType(KissCommonContext.LoginTypeContext.MOBILE.getCode());
        kissBaseUserMapper.insertBaseUser(user);
    }

}
