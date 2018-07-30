/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.controller.user;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.domain.KissBaseUser;
import com.zjtachao.fish.kiss.common.bean.ro.*;
import com.zjtachao.fish.kiss.common.bean.so.KissBaseUserSo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.controller.base.KissManageBaseController;
import com.zjtachao.fish.kiss.manage.service.site.KissUserSiteRelService;
import com.zjtachao.fish.kiss.manage.service.user.KissBaseUserService;
import com.zjtachao.fish.kiss.manage.service.user.KissUserRoleRelService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;
import com.zjtachao.fish.water.common.tool.WaterValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

/**
 * 用户管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/user")
public class KissBaseUserController extends KissManageBaseController {

    /**
     * 用户service
     **/
    @Autowired
    private KissBaseUserService kissBaseUserService;

    /**
     * 用户角色service
     **/
    @Autowired
    private KissUserRoleRelService kissUserRoleRelService;

    /**
     * 用户场所service
     **/
    @Autowired
    private KissUserSiteRelService kissUserSiteRelService;

    /**
     * 管理员角色编码
     **/
    @Value("${com.zjtachao.fish.kiss.manage.normal.user.role.admin}")
    private String adminRoleCode;

    /**
     * 查询用户单条数据
     *
     * @param code
     * @return
     */
    @GET
    @Path("/query/single/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserRo> queryUserByCode(@PathParam("code") String code) {
        WaterBootResultBean<KissBaseUserRo> result = new WaterBootResultBean<KissBaseUserRo>();
        try {
            KissBaseUserRo ro = kissBaseUserService.findBaseUserByCode(code);
            result.setRst(ro);
            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
            result.setMsg("查询成功");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 查询列表
     *
     * @param json
     * @return
     */
    @POST
    @Path("/query/list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserRo> queryList(String json,
                                                         @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String userToken) {
        WaterBootResultBean<KissBaseUserRo> result = new WaterBootResultBean<KissBaseUserRo>();
        try {
            if (null != json && !"".equals(json)) {
                KissBaseUserSo so = JSON.parseObject(json, KissBaseUserSo.class);
                boolean allowFlag = isAllowQueryUser(userToken, KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode());
                if (allowFlag) {
                    so.setUserRole(getUserRole(userToken));
                    so.setAdminUserCode(getUser(userToken).getUserCode());
                    Long count = kissBaseUserService.findBaseUserCount(so);
                    so.setDataCount(count);
                    List<KissBaseUserRo> roList = kissBaseUserService.findBaseUserList(so);
                    result.setCount(count);
                    result.setRst(roList);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("查询成功");
                } else {
                    result.setMsg("该用户无权限操作");
                }

            } else {
                result.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
                result.setMsg("参数有误");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 修改状态
     *
     * @return
     */
    @GET
    @Path("/status/{code}/{status}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserRo> upateStatus(
            @PathParam("code") String code,
            @PathParam("status") Integer status) {
        WaterBootResultBean<KissBaseUserRo> result = new WaterBootResultBean<KissBaseUserRo>();
        try {
            kissBaseUserService.updateBaseUserStatus(code, status);
            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
            result.setMsg("修改状态成功！");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 修改用户
     *
     * @return
     */
    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserWithPwdRo> update(String json) {
        WaterBootResultBean<KissBaseUserWithPwdRo> result = new WaterBootResultBean<KissBaseUserWithPwdRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissBaseUserWithPwdRo ro = null;
            if (null != json && !"".equals(json)) {
                ro = JSON.parseObject(json, KissBaseUserWithPwdRo.class);
                if (null != ro) {
                    if (null == ro.getUserCode() || "".equals(ro.getUserCode())) {
                        flag = false;
                        msg = "用户编码有误";
                    }
                    if (null == ro.getUserNickname() || "".equals(ro.getUserNickname())
                            || ro.getUserNickname().length() < 1 || ro.getUserNickname().length() > 100) {
                        flag = false;
                        msg = "昵称有误";
                    }
                    if (null == ro.getUserPwd() || "".equals(ro.getUserPwd()) || !WaterValidateUtil.validateMd5Pwd(ro.getUserPwd())) {
                        flag = false;
                        msg = "密码有误，必须为md5加密后的数据";
                    }
                    if (null == ro.getUserMobile() || "".equals(ro.getUserMobile()) || !WaterValidateUtil.isMobileLegal(ro.getUserMobile())) {
                        flag = false;
                        msg = "手机号有误";
                    }
                } else {
                    flag = false;
                    msg = "参数传递有误";
                }
            } else {
                flag = false;
                msg = "参数传递有误";
            }

            if (flag) {
                KissBaseUserSo so = new KissBaseUserSo();
                so.setUserMobile(ro.getUserMobile());
                List<KissBaseUserRo> roList = kissBaseUserService.findBaseUserList(so);

                boolean mobileFlag = true;
                if (null != roList && !roList.isEmpty()) {
                    for (KissBaseUserRo userRo : roList) {
                        if (null != userRo
                                && !userRo.getUserCode().equals(ro.getUserCode())) {
                            mobileFlag = false;
                            break;
                        }
                    }
                }
                if (mobileFlag) {
                    kissBaseUserService.updateBaseUser(ro);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    msg = "修改成功";
                } else {
                    msg = "手机号码已存在";
                }
            }
            result.setMsg(msg);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 修改用户
     *
     * @return
     */
    @POST
    @Path("/resetpwd")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserWithPwdRo> resetpwd(String json ,
                                                               @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String userToken) {
        WaterBootResultBean<KissBaseUserWithPwdRo> result = new WaterBootResultBean<KissBaseUserWithPwdRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissBaseUserWithPwdRo ro = null;
            if (null != json && !"".equals(json)) {
                ro = JSON.parseObject(json, KissBaseUserWithPwdRo.class);
                if (null != ro) {
                    if (null == ro.getUserCode() || "".equals(ro.getUserCode())) {
                        flag = false;
                        msg = "用户编码有误";
                    }
                    if (null == ro.getUserPwd() || "".equals(ro.getUserPwd()) || !WaterValidateUtil.validateMd5Pwd(ro.getUserPwd())) {
                        flag = false;
                        msg = "密码有误，必须为md5加密后的数据";
                    }
                } else {
                    flag = false;
                    msg = "参数传递有误";
                }
            } else {
                flag = false;
                msg = "参数传递有误";
            }

            if (flag) {
                //修改昵称
                kissBaseUserService.updateBaseUserPwd(ro.getUserCode() , ro.getUserPwd());
                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                msg = "修改成功";
            }
            result.setMsg(msg);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 修改用户
     *
     * @return
     */
    @POST
    @Path("/updatenickname")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserWithPwdRo> updateNickname(String json,
                                                                     @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String userToken) {
        WaterBootResultBean<KissBaseUserWithPwdRo> result = new WaterBootResultBean<KissBaseUserWithPwdRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissBaseUserWithPwdRo ro = null;
            if (null != json && !"".equals(json)) {
                ro = JSON.parseObject(json, KissBaseUserWithPwdRo.class);
                if (null != ro) {
                    if (null == ro.getUserNickname() || "".equals(ro.getUserNickname())
                            || ro.getUserNickname().length() < 1 || ro.getUserNickname().length() > 100) {
                        flag = false;
                        msg = "昵称有误";
                    }
                } else {
                    flag = false;
                    msg = "参数传递有误";
                }
            } else {
                flag = false;
                msg = "参数传递有误";
            }

            if (flag) {
                //修改昵称
                KissBaseUserRo userRo = getUser(userToken);
                if (null != userRo && null != userRo.getUserCode()) {
                    kissBaseUserService.updateBaseUserNickname(userRo.getUserCode(), ro.getUserNickname());
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("修改成功");
                } else {
                    result.setMsg("未找到需要修改的用户信息");
                }
            } else {
                result.setMsg(msg);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }


    /**
     * 修改用户
     *
     * @return
     */
    @POST
    @Path("/updatepwd")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserWithPwdRo> updatePwd(String json,
                                                                @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String userToken) {
        WaterBootResultBean<KissBaseUserWithPwdRo> result = new WaterBootResultBean<KissBaseUserWithPwdRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissBaseUserWithPwdRo ro = null;
            if (null != json && !"".equals(json)) {
                ro = JSON.parseObject(json, KissBaseUserWithPwdRo.class);
                if (null != ro) {
                    if (null == ro.getOldUserPwd() || "".equals(ro.getOldUserPwd()) || !WaterValidateUtil.validateMd5Pwd(ro.getOldUserPwd())) {
                        flag = false;
                        msg = "旧密码有误，必须为md5加密后的数据";
                    }
                    if (flag && (null == ro.getUserPwd() || "".equals(ro.getUserPwd()) || !WaterValidateUtil.validateMd5Pwd(ro.getUserPwd()))) {
                        flag = false;
                        msg = "新密码有误，必须为md5加密后的数据";
                    }
                    if (flag && (ro.getUserPwd().equals(ro.getOldUserPwd()))) {
                        flag = false;
                        msg = "新密码不能与旧密码相同";
                    }
                } else {
                    flag = false;
                    msg = "参数传递有误";
                }
            } else {
                flag = false;
                msg = "参数传递有误";
            }

            if (flag) {
                //修改昵称
                KissBaseUserRo userRo = getUser(userToken);
                if (null != userRo && null != userRo.getUserCode()) {
                    //查询用户旧密码
                    KissBaseUserWithPwdRo oldUserRo = kissBaseUserService.findBaseUserWithPwdByCode(userRo.getUserCode());
                    if (null != oldUserRo && null != oldUserRo.getUserPwd() && oldUserRo.getUserPwd().equals(ro.getOldUserPwd())) {
                        kissBaseUserService.updateBaseUserPwd(userRo.getUserCode(), ro.getUserPwd());
                        result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        result.setMsg("修改成功");
                    } else {
                        result.setMsg("旧密码不正确");
                    }

                } else {
                    result.setMsg("未找到需要修改的用户信息");
                }
            } else {
                result.setMsg(msg);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }


    /**
     * 设为用户权限
     *
     * @return
     */
    @POST
    @Path("/admin/yes")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissUserRoleRelRo> adminYes(String json,
                                                           @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String userToken) {
        WaterBootResultBean<KissUserRoleRelRo> result = new WaterBootResultBean<KissUserRoleRelRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissBaseUserRo ro = null;
            if (null != json && !"".equals(json)) {
                ro = JSON.parseObject(json, KissBaseUserRo.class);
                if (null != ro) {
                    if (null == ro.getUserCode() || "".equals(ro.getUserCode())) {
                        flag = false;
                        msg = "用户编码有误";
                    }
                    if (flag && (null == ro.getUserRole() || "".equals(ro.getUserRole())
                            || KissCommonContext.RoleTypeContext.getLevel(ro.getUserRole()) == 0
                            || KissCommonContext.RoleTypeContext.getLevel(ro.getUserRole()) >= KissCommonContext.RoleTypeContext.SUPER_ADMIN.getLevel())) {
                        flag = false;
                        msg = "用户权限有误";
                    }
                    if (flag && (ro.getUserRole().equals(KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode()) && (
                            null == ro.getSiteCode() || "".equals(ro.getSiteCode())
                    ))) {
                        flag = false;
                        msg = "场所管理员必须设置场所编码";
                    }


                } else {
                    flag = false;
                    msg = "参数传递有误";
                }
            } else {
                flag = false;
                msg = "参数传递有误";
            }

            if (flag) {
                //查询用户基本信息
                KissBaseUserRo targetUser = kissBaseUserService.findBaseUserByCode(ro.getUserCode());
                //验证用户
                if (flag && (null == targetUser || null == targetUser.getUserCode())) {
                    flag = false;
                    msg = "未找到用户需要修改的用户";
                }
                //验证用户是否是管理员
                if (flag && (null != targetUser.getUserRole() && !"".equals(targetUser.getUserRole()))) {
                    flag = false;
                    msg = "该用户已是管理员了";
                }
                //验证本人是否有权限对目标用户进行操作
                if (flag) {
                    String currentUserRole = getUserRole(userToken);
                    if (KissCommonContext.RoleTypeContext.getLevel(currentUserRole) <= KissCommonContext.RoleTypeContext.SITE_ADMIN.getLevel()) {
                        flag = false;
                        msg = "该用户无权限操作";
                    }
                }
            }

            //设置权限
            if (flag) {
                kissUserRoleRelService.add(ro.getUserCode(), ro.getUserRole());
                //设置场所与用户关系
                if (null != ro.getSiteCode() && ro.getUserRole().equals(KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode())) {
                    //添加site
                    kissUserSiteRelService.addUserSiteRel(ro.getUserCode(), ro.getSiteCode());
                    //修改用户site
                    kissBaseUserService.updateBaseUserSite(ro.getUserCode() , ro.getSiteCode());
                }

                //设置修改的用户登录过期
                notifyChangeRole(ro.getUserCode(), userToken);

                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                result.setMsg("设置成功");
            } else {
                result.setMsg(msg);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }


    /**
     * 取消管理员
     *
     * @return
     */
    @POST
    @Path("/admin/no")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissUserRoleRelRo> adminNo(String json,
                                                          @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String userToken) {
        WaterBootResultBean<KissUserRoleRelRo> result = new WaterBootResultBean<KissUserRoleRelRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissBaseUserRo ro = null;
            if (null != json && !"".equals(json)) {
                ro = JSON.parseObject(json, KissBaseUserRo.class);
                if (null != ro) {
                    if (null == ro.getUserCode() || "".equals(ro.getUserCode())) {
                        flag = false;
                        msg = "用户编码有误";
                    }
                } else {
                    flag = false;
                    msg = "参数传递有误";
                }
            } else {
                flag = false;
                msg = "参数传递有误";
            }
            if (flag) {
                //查询用户基本信息
                KissBaseUserRo targetUser = kissBaseUserService.findBaseUserByCode(ro.getUserCode());
                //验证用户
                if (flag && (null == targetUser || null == targetUser.getUserCode())) {
                    flag = false;
                    msg = "未找到用户需要修改的用户";
                }
                //验证用户是否是管理员
                if (flag && (null == targetUser.getUserRole() || "".equals(targetUser.getUserRole()))) {
                    flag = false;
                    msg = "该用户不是管理员";
                }
                //验证该用户是否具备取消目标用户权限
                if (flag) {
                    String currentUserRole = getUserRole(userToken);
                    if (KissCommonContext.RoleTypeContext.getLevel(currentUserRole) <= KissCommonContext.RoleTypeContext.getLevel(targetUser.getUserRole())) {
                        flag = false;
                        msg = "该用户无权限操作";
                    }

                    //判断是否是代理商对场所管理员操作
                    if (flag && currentUserRole.equals(KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode())
                            && targetUser.getUserRole().equals(KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode())) {
                        //查询目标用户site
                        boolean targetAllow = false;
                        List<KissUserSiteRelRo> targerUserSiteRelList = kissUserSiteRelService.queryUserSiteRelByUserCode(targetUser.getUserCode());
                        if (null != targerUserSiteRelList && targerUserSiteRelList.size() == 1) {
                            String targetSite = targerUserSiteRelList.get(0).getSiteCode();
                            //获得当前用户site
                            List<KissUserSiteRelRo> currentUserSiteRelList = queryUserSiteRelList(userToken);
                            if (null != currentUserSiteRelList && !currentUserSiteRelList.isEmpty()) {
                                for (KissUserSiteRelRo currentRo : currentUserSiteRelList) {
                                    if (null != currentRo && null != currentRo.getSiteCode() && currentRo.getSiteCode().equals(targetSite)) {
                                        targetAllow = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if (!targetAllow) {
                            flag = false;
                            msg = "该用户无权限操作";
                        }
                    }
                }

            }

            //判断用户是否有权限
            if (flag) {
                //取消目标用户管理员角色
                kissUserRoleRelService.deleteByUserCode(ro.getUserCode());
                kissUserSiteRelService.deleteUserSiteRelByUserCode(ro.getUserCode());

                //设置修改的用户登录过期
                notifyChangeRole(ro.getUserCode(), userToken);

                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                result.setMsg("取消成功");
            } else {
                result.setMsg(msg);
            }


        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 添加用户
     *
     * @param json
     * @return
     */
    @POST
    @Path("/add/user")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserRo> addUser(String json,
                                                       @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String userToken) {
        WaterBootResultBean<KissBaseUserRo> result = new WaterBootResultBean<KissBaseUserRo>();
        try {
            KissBaseUserWithPwdRo ro = null;
            boolean flag = true;
            String msg = null;
            if (null != json && !"".equals(json)) {
                ro = JSON.parseObject(json, KissBaseUserWithPwdRo.class);
                if (null == ro.getUserNickname() || "".equals(ro.getUserNickname())
                        || ro.getUserNickname().length() < 1 || ro.getUserNickname().length() > 100) {
                    flag = false;
                    msg = "昵称有误";
                }
                if (null == ro.getUserPwd() || "".equals(ro.getUserPwd()) || !WaterValidateUtil.validateMd5Pwd(ro.getUserPwd())) {
                    flag = false;
                    msg = "密码有误，必须为md5加密后的数据";
                }
                if (null == ro.getUserMobile() || "".equals(ro.getUserMobile()) || !WaterValidateUtil.isMobileLegal(ro.getUserMobile())) {
                    flag = false;
                    msg = "手机号有误";
                }

                if (flag) {
                    KissBaseUserSo so = new KissBaseUserSo();
                    so.setUserMobile(ro.getUserMobile());
                    Long count = kissBaseUserService.findBaseUserCount(so);
                    if (null != count && count.longValue() > 0l) {
                        flag = false;
                        msg = "该用户手机号已存在";
                    }
                }

                if (flag) {
                    boolean allow = isAllowAdd(userToken, KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode());
                    if (allow) {
                        //判断是否是代理商
                        String currentUserRole = getUserRole(userToken);
                        if (currentUserRole.equals(KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode()) && (null == ro.getSiteCode() || "".equals(ro.getSiteCode()))) {
                            flag = false;
                            msg = "代理商添加用户必须选择场所";
                        }
                        if (flag) {
                            //新增用户
                            String uuid = WaterUUIDUtil.getUUID();
                            ro.setUserCode(uuid);
                            kissBaseUserService.insertBaseUser(ro);
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("新增成功");
                        } else {
                            result.setMsg(msg);
                        }
                    } else {
                        result.setMsg("该用户无权限操作");
                    }

                } else {
                    result.setMsg(msg);
                }

            } else {
                result.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
                result.setMsg("参数有误");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 查询用户下所有场所
     *
     * @return
     */
    @GET
    @Path("/querysite/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissUserSiteRelRo> querySite(@PathParam("code") String code,
                                                          @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String userToken) {
        WaterBootResultBean<KissUserSiteRelRo> result = new WaterBootResultBean<KissUserSiteRelRo>();
        try {
            if(null != code && !"".equals(code)){
                List<KissUserSiteRelRo> roList = kissUserSiteRelService.queryUserSiteRelByUserCode(code);
                result.setRst(roList);
                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                result.setMsg("查询成功");
            }else {
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
     * 修改场所
     *
     * @param json
     * @return
     */
    @POST
    @Path("/updatesite")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissBaseUserRo> updateSite(String json,
                                                          @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String userToken) {
        WaterBootResultBean<KissBaseUserRo> result = new WaterBootResultBean<KissBaseUserRo>();
        try {
            KissBaseUserSiteRo ro = null;
            boolean flag = true;
            String msg = null;
            KissBaseUserRo targetUser = null;
            String siteAdminCode = null;
            if (null != json && !"".equals(json)) {
                ro = JSON.parseObject(json, KissBaseUserSiteRo.class);
                if (null == ro) {
                    flag = false;
                    msg = "参数有误";
                }
                if (flag && null == ro.getUserCode()) {
                    flag = false;
                    msg = "用户编码不能为空";
                }
                if (flag) {
                    //查询用户基本信息
                    targetUser = kissBaseUserService.findBaseUserByCode(ro.getUserCode());
                    //验证用户
                    if (flag && (null == targetUser || null == targetUser.getUserCode())) {
                        flag = false;
                        msg = "未找到用户需要修改的用户";
                    }
                    //验证用户是否是管理员
                    if (flag && (null == targetUser.getUserRole() || "".equals(targetUser.getUserRole()))) {
                        flag = false;
                        msg = "该用户不是管理员";
                    }
                    //验证该用户是否具备授予站点权限
                    if (flag) {
                        String currentUserRole = getUserRole(userToken);
                        if (KissCommonContext.RoleTypeContext.getLevel(currentUserRole) <= KissCommonContext.RoleTypeContext.getLevel(targetUser.getUserRole())) {
                            flag = false;
                            msg = "该用户无权限操作";
                        }
                    }
                    //验证场所管理员是否有多个
                    if (flag && KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode().equals(targetUser.getUserRole())) {
                        if (null != ro.getUserSites() && ro.getUserSites().size() > 1) {
                            if(ro.getUserSites().size() == 1){
                                for(String site : ro.getUserSites()){
                                    siteAdminCode = site;
                                    break;
                                }
                            }else {
                                flag = false;
                                msg = "场所管理员最多只能设置一个场所";
                            }
                        }
                    }
                }
                if (flag) {
                    //删除原有数据
                    kissUserSiteRelService.deleteUserSiteRelByUserCode(ro.getUserCode());

                    kissUserSiteRelService.queryUserSiteRelByUserCode(ro.getUserCode());
                    //新增数据
                    if (null != ro.getUserSites() && ro.getUserSites().size() > 0) {
                        for (String userSite : ro.getUserSites()) {
                            if (null != userSite && !"".equals(userSite)) {
                                kissUserSiteRelService.addUserSiteRel(ro.getUserCode(), userSite);
                            }
                        }
                    }


                    //设置场所与用户关系
                    if (null != targetUser && null != ro.getUserSites() && null != siteAdminCode && !"".equals(siteAdminCode)
                            && targetUser.getUserRole().equals(KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode())) {
                        //修改用户site
                        kissBaseUserService.updateBaseUserSite(targetUser.getUserCode() , siteAdminCode);
                    }


                    //设置修改的用户登录过期
                    notifyChangeRole(ro.getUserCode(), userToken);

                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("设置成功");
                } else {
                    result.setMsg(msg);
                }

            } else {
                result.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
                result.setMsg("参数有误");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 通知权限变更
     *
     * @param userCode
     */
    private void notifyChangeRole(String userCode, String userToken) {
        KissBaseUserUpdateRo ro = new KissBaseUserUpdateRo();
        ro.setUserCode(userCode);
        ro.setRoleDate(new Date());
        waterRedis.set(KissCommonConstant.KISS_COMMON_USER_SITE_NOTIFY_PREFIX + userCode, JSON.toJSONString(ro), KissCommonConstant.KISS_COMMON_USER_NORMAL_ROLE_EXPIRE_TIME);
    }


}
