/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.controller.login;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.*;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.data.service.user.KissBaseUserService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import com.zjtachao.fish.water.common.data.WaterDozer;
import com.zjtachao.fish.water.common.tool.WaterHttpUtil;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;
import com.zjtachao.fish.water.common.tool.WaterValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * 登录
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/login")
public class KissLogin extends WaterBootBaseController {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 用户基础服务 **/
    @Autowired
    private KissBaseUserService kissBaseUserService;

    /** appid **/
    @Value("${com.zjtachao.fish.kiss.data.program.appid}")
    private String appid;

    /** appsecret **/
    @Value("${com.zjtachao.fish.kiss.data.program.appsecret}")
    private String appsecret;

    /** url **/
    @Value("${com.zjtachao.fish.kiss.data.program.url}")
    private String url;

    /**
     * 用户名密码登录
     * @return
     */
    @POST
    @Path("/normal")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserRo> loginNormal(String json,
                                                           @Context HttpServletResponse response,
                      @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_CAPTCHA_COOKIE_KEY) String captchaid,
                       @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String logintoken){
        WaterBootResultBean<KissBaseUserRo> result = new WaterBootResultBean<KissBaseUserRo>();
        try {
            boolean flag = true;
            if(null != json && !"".equals(json)){
                KissLoginNormalRo ro = JSON.parseObject(json , KissLoginNormalRo.class);
                if(null != ro){
                    //验证用户名是否是手机
                    if(flag && !WaterValidateUtil.isMobileLegal(ro.getUserName())){
                        flag = false;
                        result.setMsg("用户名必须是手机号码");
                    }
                    //验证密码
                    if(flag && !WaterValidateUtil.validateMd5Pwd(ro.getUserPwd())){
                        flag = false;
                        result.setMsg("密码必须是32位小写的加密字符串");
                    }
                    //captchaid
                    if(flag && (null == captchaid || "".equals(captchaid))){
                        flag = false;
                        result.setMsg("验证码不存在或已失效");
                    }
                    //验证码
                    if(flag && (null == ro.getCaptcha() || "".equals(ro.getCaptcha()))){
                        flag = false;
                        result.setMsg("请填写验证码");
                    }
                    if(flag){
                        //查询验证码
                        String captchaCache = KissCommonConstant.KISS_COMMON_USER_LOGIN_CAPTCHA_CACHE_PREFIX+captchaid;
                        String captcha = waterRedis.queryString(captchaCache);
                        if(null == captcha || "".equals(captcha) || !captcha.equalsIgnoreCase(ro.getCaptcha())){
                            flag = false;
                            result.setMsg("验证码错误");
                        }
                    }
                    //
                }else {
                   flag = false;
                   result.setMsg("参数不能为空");
                }
                if(flag){
                    //查询用户
                    KissBaseUserWithPwdRo userRo = kissBaseUserService.findBaseUserByPhone(ro.getUserName());
                    if(null != userRo){
                        if(null != userRo.getUserMobile() && ro.getUserPwd().equals(userRo.getUserPwd())){
                            if(null != userRo.getUserStatus()
                                    && userRo.getUserStatus().intValue() ==  KissCommonContext.UserStatusContext.NORMAL.getCode()){
                                //查询用户的权限
                                //TODO 专供后台使用
                                if(null != userRo && null != userRo.getUserRole()
                                        && !"".equals(userRo.getUserRole())
                                        && null != KissCommonContext.RoleTypeContext.getName(userRo.getUserRole())
                                        && !"".equals(KissCommonContext.RoleTypeContext.getName(userRo.getUserRole()))){
                                    //注销前面传过来的数据
                                    if(null != logintoken){
                                        waterRedis.delete(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+logintoken);
                                    }
                                    Date date = new Date();
                                    userRo.setUserLoginTime(date);
                                    userRo.setUserLoginType(KissCommonContext.LoginTypeContext.MOBILE.getCode());
                                    //登录
                                    String uuid = WaterUUIDUtil.getUUID();
                                    waterRedis.set(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+uuid , JSON.toJSONString(userRo) , KissCommonConstant.KISS_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME);
                                    kissBaseUserService.updateBaseUserLoginInfo(userRo , date);
                                    KissBaseUserRo loginRo = waterDozer.convert(userRo , KissBaseUserRo.class);
                                    loginRo.setLoginToken(uuid);

                                    //添加cookie
                                    addCookie(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY , uuid , -1 ,response);

                                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                    result.setRst(loginRo);
                                    result.setMsg("登录成功");
                                }else {
                                    result.setMsg("您暂无权限登录，请联系管理员");
                                }
                            }else {
                                result.setMsg("用户状态为："+KissCommonContext.UserStatusContext.getName(userRo.getUserStatus())+"，暂时不能登录，请联系管理员");
                            }
                        }else {
                            flag = false;
                            result.setMsg("密码错误");
                        }
                    }else {
                        flag = false;
                        result.setMsg("用户不存在");
                    }
                }

            }else {
                flag = false;
                result.setMsg("参数错误");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }


    /**
     * 微信小程序登录
     * @return
     */
    @POST
    @Path("/program")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserRo> loginProgram(String json,
                                                           @Context HttpServletResponse response,
                                                           @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String logintoken) {
        WaterBootResultBean<KissBaseUserRo> result = new WaterBootResultBean<KissBaseUserRo>();
        try {
            boolean flag = true;
            if (null != json && !"".equals(json)) {
                KissLoginProgramRo ro = JSON.parseObject(json, KissLoginProgramRo.class);
                if (null != ro) {
                    if(flag && (null == ro.getLoginCode() || "".equals(ro.getLoginCode()))){
                        flag = false;
                        result.setMsg("logincode不能为空");
                    }
                    if(flag){
                        //尝试调用微信获得openid和unionid
                        String wechatUrl = url.replaceAll("#APPID#" , appid).replaceAll("#SECRET#" , appsecret).replaceAll("#JSCODE#",ro.getLoginCode());
                        logger.info("wechaturl:"+wechatUrl);
                        String wechatJson = WaterHttpUtil.httpGet(wechatUrl);
                        KissLoginWechatRo wechatRo = JSON.parseObject(wechatJson , KissLoginWechatRo.class);
                        if(null != wechatRo && null != wechatRo.getOpenid() && !"".equals(wechatRo.getOpenid())){
                            //通过openid查询数据
                            KissBaseUserRo userRo = kissBaseUserService.findBaseUserByOpenid(wechatRo.getOpenid());
                            Date date = new Date();
                            if(null == userRo){
                                //新增RO
                                userRo = new KissBaseUserRo();
                                userRo.setUserCode(WaterUUIDUtil.getUUID());
                                userRo.setUserOpenid(wechatRo.getOpenid());
                                userRo.setUserRegType(KissCommonContext.LoginTypeContext.PROGRAM.getCode());
                                userRo.setUserLoginType(KissCommonContext.LoginTypeContext.PROGRAM.getCode());
                                userRo.setUserRegTime(date);
                                userRo.setUserLoginTime(date);
                                userRo.setUserStatus(KissCommonContext.UserStatusContext.NORMAL.getCode());
                                //创建RO
                                kissBaseUserService.addUser(userRo , date);
                            }

                            if(null != userRo.getUserStatus()
                                    && userRo.getUserStatus().intValue() ==  KissCommonContext.UserStatusContext.NORMAL.getCode()){
                                //放入缓存
                                if(null != logintoken){
                                    waterRedis.delete(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+logintoken);
                                }
                                userRo.setUserLoginTime(date);
                                userRo.setUserLoginType(KissCommonContext.LoginTypeContext.PROGRAM.getCode());
                                //登录
                                String uuid = WaterUUIDUtil.getUUID();
                                waterRedis.set(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+uuid , JSON.toJSONString(userRo) , KissCommonConstant.KISS_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME);
                                KissBaseUserWithPwdRo userPwdRo = waterDozer.convert(userRo , KissBaseUserWithPwdRo.class);
                                kissBaseUserService.updateBaseUserLoginInfo(userPwdRo , date);

                                userRo.setLoginToken(uuid);
                                //添加cookie
                                addCookie(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY , uuid , -1 ,response);

                                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                result.setRst(userRo);
                                result.setMsg("登录成功");
                            }else {
                                result.setMsg("用户状态为："+KissCommonContext.UserStatusContext.getName(userRo.getUserStatus())+"，暂时不能登录，请联系管理员");
                            }

                        }else {
                            flag = false;
                            result.setMsg("code:"+ro.getLoginCode()+",未从微信中获取正确的openid");
                            logger.warn("code:"+ro.getLoginCode()+",返回值：" +wechatJson);
                        }
                    }

                }else {
                    flag = false;
                    result.setMsg("参数不能为空");
                }
            } else {
                flag = false;
                result.setMsg("参数错误");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 微信小程序登录
     * @return
     */
    @POST
    @Path("/admin/update/wechatinfo")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserRo> updateProgram(String json,
                                                            @Context HttpServletResponse response,
                                                            @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String logintoken) {
        WaterBootResultBean<KissBaseUserRo> result = new WaterBootResultBean<KissBaseUserRo>();
        try {
            boolean flag = true;
            //判断用户登录状态
            if(null != logintoken && !"".equals(logintoken)){
                String loginInfo = waterRedis.get(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+logintoken);
                KissBaseUserRo userRo  = JSON.parseObject(loginInfo , KissBaseUserRo.class);
                if(null != userRo && null != userRo.getUserCode() && !"".equals(userRo.getUserCode())){
                    if (null != json && !"".equals(json)) {
                        KissLoginProgramRo wechatRo = JSON.parseObject(json , KissLoginProgramRo.class);
                        if(null == wechatRo){
                            flag = false;
                            result.setMsg("参数为空");
                        }

                        if(flag && (null == wechatRo.getNickName() || "".equals(wechatRo.getNickName()))){
                            flag = false;
                            result.setMsg("昵称为空");
                        }
                        if(flag && (null == wechatRo.getAvatarUrl() || "".equals(wechatRo.getAvatarUrl()))){
                            flag = false;
                            result.setMsg("头像为空");
                        }

                        if(flag){
                            //更新用户信息
                            userRo.setUserHeadimg(wechatRo.getAvatarUrl());
                            userRo.setUserNickname(wechatRo.getNickName());
                            kissBaseUserService.updateBaseUserNickname(userRo);

                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("更新成功");
                        }

                    } else {
                        flag = false;
                        result.setMsg("参数错误");
                    }

                }else {
                    flag = false;
                    result.setMsg("未找到用户登录信息");
                }
            }else {
                flag = false;
                result.setMsg("未找到用户登录信息");
            }


        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }
}
