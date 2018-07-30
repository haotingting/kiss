/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.mapper;

import com.zjtachao.fish.kiss.common.bean.domain.KissBaseUser;
import com.zjtachao.fish.kiss.common.bean.so.KissBaseUserSo;

/**
 * 用户参数
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissBaseUserMapper {

    /**
     * 通过手机号查询用户
     * @param so
     * @return
     */
    public KissBaseUser findBaseUserByPhone(KissBaseUserSo so);

    /**
     * 通过openid查询用户
     * @param so
     * @return
     */
    public KissBaseUser findBaseUserByOpenid(KissBaseUserSo so);

    /**
     * 修改用户登录信息
     * @param user
     */
    public void updateBaseUserLoginInfo(KissBaseUser user);

    /**
     * 添加用户
     * @param user
     */
    public void addUser(KissBaseUser user);

    /**
     * 修改用户基本信息
     * @param user
     */
    public void updateBaseUserNickname(KissBaseUser user);

}
