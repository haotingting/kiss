/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.controller.base;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissUserSiteRelRo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.service.site.KissUserSiteRelService;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 通用方法验证
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public abstract class KissManageBaseController extends WaterBootBaseController {

    /** 用户site服务 **/
    @Autowired
    private KissUserSiteRelService kissUserSiteRelService;

    /**
     * 是否可以免场所查询
     * @param userRole
     * @return
     */
    public boolean isFreeSiteCode(String userRole){
        boolean flag = false;
        if(null != userRole
                && (KissCommonContext.RoleTypeContext.SUPER_ADMIN.getCode().equals(userRole)
                    || KissCommonContext.RoleTypeContext.GENERAL_ADMIN.getCode().equals(userRole))){
            flag = true;
        }
        return flag;
    }


    /**
     * 获得用户信息
     * @param userToken
     * @return
     */
    public KissBaseUserRo getUser(String userToken){
        KissBaseUserRo userRo = null;
        if(null != userToken && !"".equals(userToken)){
            String userJson = waterRedis.queryString(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+userToken);
            if(null != userJson && !"".equals(userJson)){
                userRo = JSON.parseObject(userJson , KissBaseUserRo.class);
            }
        }
        return userRo;
    }

    /**
     * 获得用户权限
     * @param userToken
     * @return
     */
    public String getUserRole(String userToken){
        String userRole = null;
        if(null != userToken && !"".equals(userToken)){
            String userJson = waterRedis.queryString(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+userToken);
            if(null != userJson && !"".equals(userJson)){
                KissBaseUserRo userRo = JSON.parseObject(userJson , KissBaseUserRo.class);
                if(null != userRo && null != userRo.getUserRole()
                        && !"".equals(userRo.getUserRole())
                        && null != KissCommonContext.RoleTypeContext.getName(userRo.getUserRole())
                        && !"".equals(KissCommonContext.RoleTypeContext.getName(userRo.getUserRole()))){
                    userRole = userRo.getUserRole();
                }
            }
        }
        return userRole;
    }

    /**
     * 是否允许添加
     * @param minRole
     * @return
     */
    public boolean isAllowAdd(String userToken , String minRole){
        boolean result = false;
        String userRole = getUserRole(userToken);
        if(null != userToken && !"".equals(userToken)){
            //判断大小
            if(null != userRole && !"".equals(userRole)
                    && null != minRole && !"".equals(minRole)){
                int userLevel = KissCommonContext.RoleTypeContext.getLevel(userRole);
                int minLevel = KissCommonContext.RoleTypeContext.getLevel(minRole);
                if(userLevel >= minLevel && userLevel >= KissCommonContext.RoleTypeContext.AGENT_ADMIN.getLevel()){
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 是否允许编辑
     * @param minRole
     * @return
     */
    public boolean isAllowEdit(String userToken , String minRole , String siteCode){
        boolean result = false;
        String userRole = getUserRole(userToken);
        if(null != userToken && !"".equals(userToken)){
            //判断大小
            if(null != userRole && !"".equals(userRole)
                    && null != minRole && !"".equals(minRole)){
                int userLevel = KissCommonContext.RoleTypeContext.getLevel(userRole);
                int minLevel = KissCommonContext.RoleTypeContext.getLevel(minRole);
                if(userLevel >= minLevel){
                    if(userLevel <= KissCommonContext.RoleTypeContext.AGENT_ADMIN.getLevel()){
                        if(null != siteCode && !"".equals(siteCode)){
                            //获得用户的场所
                            List<KissUserSiteRelRo> roList = queryUserSiteRelList(userToken);
                            if(null != roList && !roList.isEmpty()){
                                for(KissUserSiteRelRo ro : roList) {
                                    if (null != ro && null != ro.getSiteCode()
                                            && !"".equals(ro.getSiteCode())
                                            && siteCode.equals(ro.getSiteCode())) {
                                        result = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }else {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 是否允许操作
     * @param minRole
     * @return
     */
    public boolean isAllowSearch(String userToken , String minRole ){
        boolean result = false;
        String userRole = getUserRole(userToken);
        if(null != userToken && !"".equals(userToken)){
            //判断大小
            if(null != userRole && !"".equals(userRole)
                    && null != minRole && !"".equals(minRole)){
                int userLevel = KissCommonContext.RoleTypeContext.getLevel(userRole);
                int minLevel = KissCommonContext.RoleTypeContext.getLevel(minRole);
                if(userLevel >= minLevel){
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 是否允许操作
     * @param minRole
     * @return
     */
    public boolean isAllowQueryUser(String userToken , String minRole){
        boolean result = false;
        String userRole = getUserRole(userToken);
        if(null != userToken && !"".equals(userToken)){
            //判断大小
            if(null != userRole && !"".equals(userRole)
                    && null != minRole && !"".equals(minRole)){
                int userLevel = KissCommonContext.RoleTypeContext.getLevel(userRole);
                int minLevel = KissCommonContext.RoleTypeContext.getLevel(minRole);
                if(userLevel >= minLevel){
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 是否允许操作
     * @param minRole
     * @return
     */
    public boolean isAllowHandle(String userToken , String minRole , String siteCode){
        boolean result = false;
        String userRole = getUserRole(userToken);
        if(null != userToken && !"".equals(userToken)){
            //判断大小
            if(null != userRole && !"".equals(userRole)
                    && null != minRole && !"".equals(minRole)){
                int userLevel = KissCommonContext.RoleTypeContext.getLevel(userRole);
                int minLevel = KissCommonContext.RoleTypeContext.getLevel(minRole);
                if(userLevel >= minLevel){
                    //判断是否 代理商或场所管理员
                    if(KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode().equals(userRole)
                            || KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode().equals(userRole)){
                        if(null != siteCode && !"".equals(siteCode)){
                            //获得用户的场所
                            List<KissUserSiteRelRo> roList = queryUserSiteRelList(userToken);
                            if(null != roList && !roList.isEmpty()){
                                for(KissUserSiteRelRo ro : roList) {
                                    if (null != ro && null != ro.getSiteCode()
                                            && !"".equals(ro.getSiteCode())
                                            && siteCode.equals(ro.getSiteCode())) {
                                        result = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }else {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获得用户site列表
     * @return
     */
    public List<KissUserSiteRelRo> queryUserSiteRelList(String userToken){
        List<KissUserSiteRelRo> roList = null;
        if(null != userToken && !"".equals(userToken)){
            String userJson = waterRedis.queryString(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+userToken);
            if(null != userJson && !"".equals(userJson)){
                KissBaseUserRo userRo = JSON.parseObject(userJson , KissBaseUserRo.class);
                if(null != userRo && null != userRo.getUserCode() && !"".equals(userRo.getUserCode())){
                    //查询缓存是否存在
                    String siteJson = waterRedis.queryString(KissCommonConstant.KISS_COMMON_USER_SITE_CACHE_PREFIX+userToken);
                    if(null != siteJson && !"".equals(siteJson)){
                        roList = JSON.parseArray(siteJson , KissUserSiteRelRo.class);
                    }else {
                        roList = kissUserSiteRelService.queryUserSiteRelByUserCode(userRo.getUserCode());
                        waterRedis.set(KissCommonConstant.KISS_COMMON_USER_SITE_CACHE_PREFIX+userToken , JSON.toJSONString(roList) , KissCommonConstant.KISS_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME);
                    }
                }
            }
        }
        return roList;
    }

}
