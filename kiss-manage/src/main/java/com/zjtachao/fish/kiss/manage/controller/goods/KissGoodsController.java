/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.controller.goods;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissGoodsRo;
import com.zjtachao.fish.kiss.common.bean.so.KissGoodsSo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.controller.base.KissManageBaseController;
import com.zjtachao.fish.kiss.manage.service.goods.KissGoodsService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.tool.WaterBigdecimalUtil;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/goods")
public class KissGoodsController extends KissManageBaseController {


    /** 商品服务 **/
    @Autowired
    private KissGoodsService kissGoodsService;

    /**
     * 查询单条商品
     * @return
     */
    @GET
    @Path("/query/single/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissGoodsRo> querySingle(@PathParam("code") String code,
                                                        @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissGoodsRo> result = new WaterBootResultBean<KissGoodsRo>();
        try {
            KissGoodsRo ro = kissGoodsService.queryGoodsByCode(code);
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
    public WaterBootResultBean<KissGoodsRo> queryList(String json,
                                                      @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissGoodsRo> result = new WaterBootResultBean<KissGoodsRo>();
        try {
            if(null != json && !"".equals(json)){
                KissGoodsSo so = JSON.parseObject(json , KissGoodsSo.class);
                boolean allowFlag = isAllowHandle(userToken  , KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode(), so.getSiteCode());
                if(allowFlag){
                    //查询数量
                    Long count = kissGoodsService.queryGoodsCount(so);
                    so.setDataCount(count);
                    List<KissGoodsRo> roList = kissGoodsService.queryGoodsList(so);
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
     * 新增商品
     * @return
     */
    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissGoodsRo> add(String json,
                                                @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissGoodsRo> result = new WaterBootResultBean<KissGoodsRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissGoodsRo ro = null;
            if(null != json && !"".equals(json)){
                ro = JSON.parseObject(json , KissGoodsRo.class);
                if(null != ro){
                    if(null == ro.getGoodsType() || "".equals(ro.getGoodsType()) || !"SHARE-CHAIR".equals(ro.getGoodsType())){
                        flag = false;
                        msg = "类型有误";
                    }
                    if(null == ro.getSiteCode() || "".equals(ro.getSiteCode())){
                        flag = false;
                        msg = "场所编码有误";
                    }
                    if(null == ro.getGoodsName() || "".equals(ro.getGoodsName())){
                        flag = false;
                        msg = "商品名称有误";
                    }
                    if(null == ro.getGoodsPrice() || WaterBigdecimalUtil.compare(ro.getGoodsPrice(),new BigDecimal(0)) < 1){
                        flag = false;
                        msg = "价格有误";
                    }
                    if(null == ro.getGoodsUnit() || WaterBigdecimalUtil.compare(ro.getGoodsUnit(),new BigDecimal(0)) < 1){
                        flag = false;
                        msg = "计量单位有误";
                    }
                    if(null == ro.getGoodsDesc() || "".equals(ro.getGoodsDesc())){
                        flag = false;
                        msg = "商品描述有误";
                    }
                    if(null == ro.getGoodsOrder()){
                        flag = false;
                        msg = "排序有误";
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
                    ro.setGoodsCode(uuid);
                    ro.setGoodsStatus(KissCommonContext.GoodsContext.UNUSED.getCode());
                    kissGoodsService.addGoods(ro);
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
     * 修改商品
     * @return
     */
    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissGoodsRo> update(String json,
                                                   @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissGoodsRo> result = new WaterBootResultBean<KissGoodsRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissGoodsRo ro = null;
            if(null != json && !"".equals(json)){
                ro = JSON.parseObject(json , KissGoodsRo.class);
                if(null != ro){
                    if(null == ro.getGoodsCode() || "".equals(ro.getGoodsCode())){
                        flag = false;
                        msg = "商品编码有误";
                    }
                    if(null == ro.getSiteCode() || "".equals(ro.getSiteCode())){
                        flag = false;
                        msg = "场所编码有误";
                    }
                    if(null == ro.getGoodsType() || "".equals(ro.getGoodsType()) || !"SHARE-CHAIR".equals(ro.getGoodsType())){
                        flag = false;
                        msg = "类型有误";
                    }
                    if(null == ro.getGoodsName() || "".equals(ro.getGoodsName())){
                        flag = false;
                        msg = "商品名称有误";
                    }
                    if(null == ro.getGoodsPrice() || WaterBigdecimalUtil.compare(ro.getGoodsPrice(),new BigDecimal(0)) < 1){
                        flag = false;
                        msg = "价格有误";
                    }
                    if(null == ro.getGoodsUnit() || WaterBigdecimalUtil.compare(ro.getGoodsUnit(),new BigDecimal(0)) < 1){
                        flag = false;
                        msg = "计量单位有误";
                    }
                    if(null == ro.getGoodsDesc() || "".equals(ro.getGoodsDesc())){
                        flag = false;
                        msg = "详情有误";
                    }
                    if(null == ro.getGoodsOrder()){
                        flag = false;
                        msg = "排序有误";
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
                    kissGoodsService.updateGoods(ro);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    msg = "修改成功";
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
     * 删除商品
     * @return
     */
    @GET
    @Path("/delete/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissGoodsRo> delete(@PathParam("code") String code,
                                                   @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissGoodsRo> result = new WaterBootResultBean<KissGoodsRo>();
        try {
            KissGoodsRo ro = kissGoodsService.queryGoodsByCode(code);
            if(null != ro && null != ro.getSiteCode()){
                //权限
                boolean allow = isAllowEdit(userToken , KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode() , ro.getSiteCode());
                if(allow){
                    //通过code查询 查询场所
                    kissGoodsService.deleteGoods(code);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("删除成功！");
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
     * 修改状态
     * @return
     */
    @GET
    @Path("/status/{code}/{status}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissGoodsRo> upateStatus(
            @PathParam("code") String code,
            @PathParam("status") Integer status,
            @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissGoodsRo> result = new WaterBootResultBean<KissGoodsRo>();
        try {
            KissGoodsRo ro = kissGoodsService.queryGoodsByCode(code);
            if(null != ro && null != ro.getSiteCode()){
                //权限
                boolean allow = isAllowEdit(userToken , KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode() , ro.getSiteCode());
                if(allow){
                    kissGoodsService.updateGoodsStatus(code , status);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("修改状态成功！");
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



}
