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

import com.zjtachao.fish.kiss.common.bean.domain.KissBaseUser;
import com.zjtachao.fish.kiss.common.bean.so.KissBaseUserSo;

import java.util.List;

/**
 * 用户基础信息Mapper
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissBaseUserMapper {

    /**
     * 查询单条用户
     * @param so
     * @return
     */
    public KissBaseUser findBaseUserByCode(KissBaseUserSo so);

    /**
     * 新增用户
     * @param user
     */
    public void insertBaseUser(KissBaseUser user);

    /**
     * 查询数量
     * @param so
     * @return
     */
    public Long findBaseUserCount(KissBaseUserSo so);

    /**
     * 查询列表
     * @param so
     * @return
     */
    public List<KissBaseUser> findBaseUserList(KissBaseUserSo so);

    /**
     * 修改用户
     * @param user
     */
    public void updateBaseUser(KissBaseUser user);

    /**
     * 修改用户状态
      * @param user
     */
    public void updateBaseUserStatus(KissBaseUser user);

    /**
     * 修改用户昵称
     * @param user
     */
    public void updateBaseUserNickname(KissBaseUser user);

    /**
     * 修改用户密码
     * @param user
     */
    public void updateBaseUserPwd(KissBaseUser user);

    /**
     * 修改用户site
     * @param user
     */
    public void updateBaseUserSite(KissBaseUser user);

}
