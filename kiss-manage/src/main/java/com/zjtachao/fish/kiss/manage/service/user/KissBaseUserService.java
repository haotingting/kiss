/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.user;

import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserWithPwdRo;
import com.zjtachao.fish.kiss.common.bean.so.KissBaseUserSo;

import java.util.List;

/**
 * 用户基本操作接口
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissBaseUserService {

    /**
     * 通过code查询用户
     * @param userCode
     * @return
     */
    public KissBaseUserWithPwdRo findBaseUserWithPwdByCode(String userCode);

    /**
     * 通过code查询用户
     * @param userCode
     * @return
     */
    public KissBaseUserRo findBaseUserByCode(String userCode);

    /**
     * 查询列表
     * @param so
     * @return
     */
    public List<KissBaseUserRo> findBaseUserList(KissBaseUserSo so);

    /**
     * 查询数量
     * @param so
     * @return
     */
    public Long findBaseUserCount(KissBaseUserSo so);

    /**
     * 更新用户信息
     * @param ro
     */
    public void updateBaseUser(KissBaseUserWithPwdRo ro);

    /**
     * 修改状态
     */
    public void updateBaseUserStatus(String code , Integer status);

    /**
     * 修改用户昵称
     * @param code
     * @param nickname
     */
    public void updateBaseUserNickname(String code , String nickname);

    /**
     * 修改密码
     * @param code
     * @param pwd
     */
    public void updateBaseUserPwd(String code , String pwd);


    /**
     * 修改用户场所
     * @param code
     * @param siteCode
     */
    public void updateBaseUserSite(String code , String siteCode);


    /**
     * 添加用户
     * @param ro
     */
    public void insertBaseUser(KissBaseUserWithPwdRo ro);
}
