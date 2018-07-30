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

import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.water.common.base.bean.WaterBootBaseRo;

import java.util.Date;

/**
 * 基础用户RO
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissBaseUserRo extends WaterBootBaseRo {

    /** 序列号 **/
    protected static final long serialVersionUID = -6254284751351098230L;

    /** 主键 **/
    protected Long id;

    /** 用户编码 **/
    protected String userCode;

    /** 用户openid **/
    protected String userOpenid;

    /** 用户unionid **/
    protected String userUnionid;

    /** 用户手机号 **/
    protected String userMobile;

    /** 用户昵称 **/
    protected String userNickname;

    /** 用户头像 **/
    protected String userHeadimg;

    /** 用户注册时间 **/
    protected Date userRegTime;

    /** 用户注册类型 program-微信小程序 mobile-手机号 **/
    protected String userRegType;

    /** 用户登陆类型 program-微信小程序 mobile-手机号 sms-验证码 **/
    protected String userLoginType;

    /** 用户最后一次登陆时间 **/
    protected Date userLoginTime;

    /** 用户状态 0-未激活 1-正常 2-禁用 **/
    protected Integer userStatus;

    /** 场所编码 **/
    protected String siteCode;

    /** 用户角色 **/
    protected String userRole;

    /** 用户级别 **/
    protected int userLevel;

    /** 登录token **/
    protected String loginToken;

    public int getUserLevel() {
        int result = 0;
        result = KissCommonContext.RoleTypeContext.getLevel(userRole);
        return result;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserOpenid() {
        return userOpenid;
    }

    public void setUserOpenid(String userOpenid) {
        this.userOpenid = userOpenid;
    }

    public String getUserUnionid() {
        return userUnionid;
    }

    public void setUserUnionid(String userUnionid) {
        this.userUnionid = userUnionid;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserHeadimg() {
        return userHeadimg;
    }

    public void setUserHeadimg(String userHeadimg) {
        this.userHeadimg = userHeadimg;
    }

    public Date getUserRegTime() {
        return userRegTime;
    }

    public void setUserRegTime(Date userRegTime) {
        this.userRegTime = userRegTime;
    }

    public String getUserRegType() {
        return userRegType;
    }

    public void setUserRegType(String userRegType) {
        this.userRegType = userRegType;
    }

    public String getUserLoginType() {
        return userLoginType;
    }

    public void setUserLoginType(String userLoginType) {
        this.userLoginType = userLoginType;
    }

    public Date getUserLoginTime() {
        return userLoginTime;
    }

    public void setUserLoginTime(Date userLoginTime) {
        this.userLoginTime = userLoginTime;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }
}
