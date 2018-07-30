/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.bean.ro;

/**
 * 用户基础返回ro
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissBaseUserWithPwdRo extends KissBaseUserRo {


    /** 序列号 **/
    private static final long serialVersionUID = -6716441631585053635L;

    /** 用户密码 **/
    private String userPwd;

    /** 旧密码 **/
    private String oldUserPwd;

    public String getOldUserPwd() {
        return oldUserPwd;
    }

    public void setOldUserPwd(String oldUserPwd) {
        this.oldUserPwd = oldUserPwd;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
