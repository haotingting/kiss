/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.service.user.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissBaseUser;
import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserWithPwdRo;
import com.zjtachao.fish.kiss.common.bean.so.KissBaseUserSo;
import com.zjtachao.fish.kiss.data.mapper.KissBaseUserMapper;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 修改用户基础信息
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissBaseUserServiceImpl implements com.zjtachao.fish.kiss.data.service.user.KissBaseUserService {


    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 用户基础信息mapper **/
    @Autowired
    private KissBaseUserMapper kissBaseUserMapper;

    /**
     * 通过手机号查询用户
     * @param mobile
     * @return
     */
    @Override
    public KissBaseUserWithPwdRo findBaseUserByPhone(String mobile){
        KissBaseUserSo so = new KissBaseUserSo();
        so.setUserMobile(mobile);
        KissBaseUser user = kissBaseUserMapper.findBaseUserByPhone(so);
        KissBaseUserWithPwdRo ro = waterDozer.convert(user , KissBaseUserWithPwdRo.class);
        return ro;
    }

    /**
     * 微信用户
     * @param openid
     * @return
     */
    public KissBaseUserRo findBaseUserByOpenid(String openid){
        KissBaseUserSo so = new KissBaseUserSo();
        so.setUserOpenid(openid);
        KissBaseUser user = kissBaseUserMapper.findBaseUserByOpenid(so);
        KissBaseUserRo ro = waterDozer.convert(user , KissBaseUserRo.class);
        return ro;
    }

    /**
     * 修改用户登录信息
     * @param ro
     */
    @Override
    public void updateBaseUserLoginInfo(KissBaseUserWithPwdRo ro , Date date){
        KissBaseUser user = waterDozer.convert(ro , KissBaseUser.class);
        user.setModifyTime(date);
        user.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissBaseUserMapper.updateBaseUserLoginInfo(user);
    }

    /**
     * 添加用户
     * @param ro
     * @param date
     */
    public void addUser(KissBaseUserRo ro , Date date){
        KissBaseUser user = waterDozer.convert(ro , KissBaseUser.class);
        user.setCreateTime(date);
        user.setModifyTime(date);
        user.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissBaseUserMapper.addUser(user);
    }

    /**
     * 修改用户昵称
     * @param ro
     */
    public void updateBaseUserNickname(KissBaseUserRo ro){
        KissBaseUser user = waterDozer.convert(ro , KissBaseUser.class);
        Date date = new Date();
        user.setModifyTime(date);
        user.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissBaseUserMapper.updateBaseUserNickname(user);
    }

}
