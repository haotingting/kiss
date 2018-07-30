/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.service.user;

import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserWithPwdRo;

import java.util.Date;

/**
 * 请在此处添加注释
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissBaseUserService {
    /**
     * 通过手机号查询用户
     * @param mobile
     * @return
     */
    KissBaseUserWithPwdRo findBaseUserByPhone(String mobile);

    /**
     * 修改用户登录信息
     * @param ro
     */
    void updateBaseUserLoginInfo(KissBaseUserWithPwdRo ro, Date date);



    /**
     * 微信用户
     * @param openid
     * @return
     */
    public KissBaseUserRo findBaseUserByOpenid(String openid);

    /**
     * 添加用户
     * @param ro
     * @param date
     */
    public void addUser(KissBaseUserRo ro , Date date);

    /**
     * 修改用户昵称
     * @param ro
     */
    public void updateBaseUserNickname(KissBaseUserRo ro);
}
