/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.bean.domain;

import com.zjtachao.fish.water.common.base.bean.WaterBootBaseDomain;

import java.util.Date;

/**
 * 用户基类
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissBaseUser extends WaterBootBaseDomain {

    /** 序列号 **/
    private static final long serialVersionUID = -7569185717975602581L;

    /** 主键 **/
    private Long id;

    /** 用户编码 **/
    private String userCode;

    /** 用户openid **/
    private String userOpenid;

    /** 用户unionid **/
    private String userUnionid;

    /** 用户手机号 **/
    private String userMobile;

    /** 用户密码 **/
    private String userPwd;

    /** 用户昵称 **/
    private String userNickname;

    /** 用户头像 **/
    private String userHeadimg;

    /** 用户注册时间 **/
    private Date userRegTime;

    /** 用户注册类型 program-微信小程序 mobile-手机号 **/
    private String userRegType;

    /** 用户登陆类型 program-微信小程序 mobile-手机号 sms-验证码 **/
    private String userLoginType;

    /** 用户最后一次登陆时间 **/
    private Date userLoginTime;

    /** 用户状态 0-未激活 1-正常 2-禁用 **/
    private Integer userStatus;

    /** 场所编码 **/
    private String siteCode;

    /** 用户角色 **/
    private String userRole;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
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
