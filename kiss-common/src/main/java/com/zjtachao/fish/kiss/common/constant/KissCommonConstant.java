/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.constant;

/**
 * 通用参数存储
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissCommonConstant {

    /** 用户登录token **/
    public static final String KISS_COMMON_USER_LOGIN_COOKIE_KEY = "KISSTOKEN";

    /** 用户登录token **/
    public static final String KISS_COMMON_USER_LOGIN_CACHE_PREFIX = "kiss:user:login:cache:token-";

    /** 用户site数据 **/
    public static final String KISS_COMMON_USER_SITE_CACHE_PREFIX = "kiss:user:site:cache:token-";

    /** 用户site数据站点 **/
    public static final String KISS_COMMON_USER_SITE_NOTIFY_PREFIX = "kiss:user:site:notify:token-";

    /** 用户websocketsession **/
    public static final String KISS_COMMON_USER_WEBSOCKET_SESSION_PREFIX = "kiss:user:websocket:session:token-";

    /** 普通登录过期时间 **/
    public static final long KISS_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME = 60 * 60 * 12;

    /** 权限判断时间 **/
    public static final long KISS_COMMON_USER_NORMAL_ROLE_EXPIRE_TIME = 60 * 60 * 24;

    /** 微信小程序登录过期时间 **/
    public static final long KISS_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME = 60 * 60 * 24 * 30;

    /** 用户登录验证码key **/
    public static final String KISS_COMMON_USER_LOGIN_CAPTCHA_COOKIE_KEY = "KISSCAPTCHA";

    /** 用户登录验证码前缀 **/
    public static final String KISS_COMMON_USER_LOGIN_CAPTCHA_CACHE_PREFIX = "kiss:user:login:cache:captcha-";

    /** 所有可售商品列表 **/
    public static final String KISS_COMMON_GOODS_CACHE_LIST = "kiss:goods:cache:goods-list";

    /** 微信小程序token **/
    public static final String KISS_COMMON_APP_TOKEN = "kiss:app:token";

    /** 设备连接channel **/
    public static final String KISS_COMMON_DEVICE_CHANNEL_PREFIX = "kiss:device:channel:device-";

    /** 获得服务器列表 **/
    public static final String KISS_COMMON_DEVICE_NETTY_PREFIX = "kiss:device:netty:server-list";

    /** 状态 状态字段 **/
    public static final String KISS_COMMON_DEVICE_GDS_PREFIX = "kiss:device:gds:gds-";

    /** 开始 状态字段 **/
    public static final String KISS_COMMON_DEVICE_START_PREFIX = "kiss:device:start:start-";

    /** 关闭 状态字段 **/
    public static final String KISS_COMMON_DEVICE_STOP_PREFIX = "kiss:device:stop:stop-";

    /** 状态字段前缀 **/
    public static final String KISS_COMMON_DEVICE_WORK_PREFIX = "kiss:device:work:work-";

    /** 用户时间剩余 **/
    public static final String KISS_COMMON_DEVICE_TIME_SURPLUS_PREFIX = "kiss:device:time:surplus-";

}
