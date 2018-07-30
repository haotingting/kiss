/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.controller.site;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissBaseUserRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissGoodsRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissSiteRo;
import com.zjtachao.fish.kiss.common.bean.so.KissDeviceSo;
import com.zjtachao.fish.kiss.common.bean.so.KissSiteSo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.controller.base.KissManageBaseController;
import com.zjtachao.fish.kiss.manage.service.device.KissDeviceService;
import com.zjtachao.fish.kiss.manage.service.goods.KissGoodsService;
import com.zjtachao.fish.kiss.manage.service.site.KissSiteService;
import com.zjtachao.fish.kiss.manage.service.site.KissUserSiteRelService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 场所管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/site")
public class KissSiteController extends KissManageBaseController {

    /** 场所服务 **/
    @Autowired
    private KissSiteService kissSiteService;

    /** 设备服务*/
    @Autowired
    private KissDeviceService kissDeviceService;

    /** 商品服务 **/
    @Autowired
    private KissGoodsService kissGoodsService;

    /**
     * 用户场所service
     **/
    @Autowired
    private KissUserSiteRelService kissUserSiteRelService;

    /** 默认参数 **/
    @Value("${com.zjtachao.fish.kiss.manage.normal.param.goods}")
    private String defaultGoods;

    /**
     * 查询单条场所
     * @return
     */
    @GET
    @Path("/query/single/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissSiteRo> querySingle(@PathParam("code") String code,
                                                       @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissSiteRo> result = new WaterBootResultBean<KissSiteRo>();
        try {
            KissSiteRo ro = kissSiteService.querySiteByCode(code);
            if(null != ro && null != ro.getSiteCode()){
                //权限
                boolean allow = isAllowHandle(userToken , KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode() , ro.getSiteCode());
                if(allow){
                    result.setRst(ro);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("查询成功");
                }else {
                    result.setMsg("该用户无权限操作");
                }
            }else {
                result.setMsg("未找到数据");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 查询列表
     * @return
     */
    @POST
    @Path("/query/list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissSiteRo> queryList(String json,
                                                     @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissSiteRo> result = new WaterBootResultBean<KissSiteRo>();
        try {
            if(null != json && !"".equals(json)){
                KissSiteSo so = JSON.parseObject(json , KissSiteSo.class);
                boolean allowFlag = isAllowSearch(userToken  , KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode());
                if(allowFlag){
                    //查询数量
                    so.setUserRole(getUserRole(userToken));
                    so.setAdminUserCode(getUser(userToken).getUserCode());
                    Long count = kissSiteService.querySiteCount(so);
                    so.setDataCount(count);
                    List<KissSiteRo> roList = kissSiteService.querySiteList(so);
                    result.setRst(roList);
                    result.setCount(count);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("查询成功");
                }else {
                    result.setMsg("该用户无权限操作");
                }
            }else {
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
     * 新增场所
     * @return
     */
    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissSiteRo> add(String json ,
                                               @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissSiteRo> result = new WaterBootResultBean<KissSiteRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissSiteRo ro = null;
            if(null != json && !"".equals(json)){
                ro = JSON.parseObject(json , KissSiteRo.class);
                if(null != ro){
                    if(null == ro.getSiteName() || "".equals(ro.getSiteName())){
                        flag = false;
                        msg = "名称有误";
                    }
                    if(null == ro.getAreaProvince() || "".equals(ro.getAreaProvince())){
                        flag = false;
                        msg = "省级有误";
                    }
                    if(null == ro.getAreaCity() || "".equals(ro.getAreaCity())){
                        flag = false;
                        msg = "市级有误";
                    }
                    if(null == ro.getAreaCounty() || "".equals(ro.getAreaCounty())){
                        flag = false;
                        msg = "区级有误";
                    }
                    if(null == ro.getAreaDetail() || "".equals(ro.getAreaDetail())){
                        flag = false;
                        msg = "场所详情有误";
                    }
                }else{
                    flag = false;
                    msg = "参数传递有误";
                }
            }else {
                flag = false;
                msg = "参数传递有误";
            }

            if(flag){
                boolean allowAdd = isAllowAdd(userToken , KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode());
                if(allowAdd){
                    String uuid = WaterUUIDUtil.getUUID();
                    ro.setSiteCode(uuid);
                    //新增场所
                    kissSiteService.addSite(ro);
                    //新增关系
                    KissBaseUserRo currentUser  = getUser(userToken);
                    if(null != currentUser && null != currentUser.getUserRole()
                            && null != currentUser.getUserCode()
                            && KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode().equals(currentUser.getUserRole())){
                        kissUserSiteRelService.addUserSiteRel(currentUser.getUserCode() , uuid);
                        //删除缓存
                        waterRedis.delete(KissCommonConstant.KISS_COMMON_USER_SITE_CACHE_PREFIX+userToken);
                    }

                    //默认添加商品
                    if(null != defaultGoods && !defaultGoods.isEmpty()){
                        List<KissGoodsRo> goodsList  = JSON.parseArray(defaultGoods , KissGoodsRo.class);
                        for(KissGoodsRo goodsRo : goodsList){
                            goodsRo.setSiteCode(uuid);
                            goodsRo.setGoodsType("SHARE-CHAIR");
                            String goodsCode = WaterUUIDUtil.getUUID();
                            goodsRo.setGoodsCode(goodsCode);
                            goodsRo.setGoodsStatus(KissCommonContext.GoodsContext.ONLINE.getCode());
                            kissGoodsService.addGoods(goodsRo);
                        }
                    }
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    msg = "新增成功";
                }else {
                    msg = "该用户无权限操作";
                }
            }
            result.setMsg(msg);
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 修改场所
     * @return
     */
    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissSiteRo> update(String json,
                                                  @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissSiteRo> result = new WaterBootResultBean<KissSiteRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissSiteRo ro = null;
            if(null != json && !"".equals(json)){
                ro = JSON.parseObject(json , KissSiteRo.class);
                if(null != ro){
                    if(null == ro.getSiteCode() || "".equals(ro.getSiteCode())){
                        flag = false;
                        msg = "编码有误";
                    }
                    if(null == ro.getSiteName() || "".equals(ro.getSiteName())){
                        flag = false;
                        msg = "名称有误";
                    }
                    if(null == ro.getAreaProvince() || "".equals(ro.getAreaProvince())){
                        flag = false;
                        msg = "省级有误";
                    }
                    if(null == ro.getAreaCity() || "".equals(ro.getAreaCity())){
                        flag = false;
                        msg = "市级有误";
                    }
                    if(null == ro.getAreaCounty() || "".equals(ro.getAreaCounty())){
                        flag = false;
                        msg = "区级有误";
                    }
                    if(null == ro.getAreaDetail() || "".equals(ro.getAreaDetail())){
                        flag = false;
                        msg = "场所详情有误";
                    }
                }else{
                    flag = false;
                    msg = "参数传递有误";
                }
            }else {
                flag = false;
                msg = "参数传递有误";
            }

            if(flag){
                //权限
                boolean allow = isAllowEdit(userToken , KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode() , ro.getSiteCode());
                if(allow){
                    kissSiteService.updateSite(ro);
                    //删除缓存
                    waterRedis.delete(KissCommonConstant.KISS_COMMON_USER_SITE_CACHE_PREFIX+userToken);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    msg = "修改成功";
                }else {
                    msg = "该用户无无权限操作";
                }
            }
            result.setMsg(msg);
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 删除场所
     * @return
     */
    @GET
    @Path("/delete/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissSiteRo> delete(@PathParam("code") String code,
                                                  @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissSiteRo> result = new WaterBootResultBean<KissSiteRo>();
        try {
            //通过code查询 查询场所
            KissSiteRo ro = kissSiteService.querySiteByCode(code);
            if(null != ro){
                //权限
                boolean allow = isAllowEdit(userToken , KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode() , ro.getSiteCode());
                if(allow){
                    KissDeviceSo so = new KissDeviceSo();
                    so.setSiteCode(code);
                    Long count = kissDeviceService.queryDeviceCount(so);
                    if(count > 0){
                        result.setMsg("该场所下存在设备，不能删除该场所");
                    }else {
                        kissSiteService.deleteSite(code);
                        //删除缓存
                        waterRedis.delete(KissCommonConstant.KISS_COMMON_USER_SITE_CACHE_PREFIX+userToken);
                        result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        result.setMsg("删除成功！");
                    }
                }else {
                    result.setMsg("该用户无权限操作");
                }
            }else {
                result.setMsg("未找到该场所");
            }

        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }



}
