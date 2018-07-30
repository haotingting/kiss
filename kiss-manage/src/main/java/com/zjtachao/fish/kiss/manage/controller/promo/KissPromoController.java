/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.controller.promo;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissGoodsRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissPromoGenRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissPromoRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissSiteRo;
import com.zjtachao.fish.kiss.common.bean.so.KissPromoSo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.common.util.KissCommonPromoUtil;
import com.zjtachao.fish.kiss.manage.controller.base.KissManageBaseController;
import com.zjtachao.fish.kiss.manage.service.goods.KissGoodsService;
import com.zjtachao.fish.kiss.manage.service.promo.KissPromoService;
import com.zjtachao.fish.kiss.manage.service.site.KissSiteService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 优惠码管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/promo")
public class KissPromoController extends KissManageBaseController {

    /** 优惠码服务 **/
    @Autowired
    private KissPromoService kissPromoService;

    /** 商品服务 **/
    @Autowired
    private KissGoodsService kissGoodsService;

    /** 场所服务 **/
    @Autowired
    private KissSiteService kissSiteService;

    /**
     * 查询列表
     * @return
     */
    @POST
    @Path("/query/list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissPromoRo> queryList(String json ,
                                                      @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissPromoRo> result = new WaterBootResultBean<KissPromoRo>();
        try {
            if(null != json && !"".equals(json)){
                KissPromoSo so = JSON.parseObject(json , KissPromoSo.class);
                //权限
                boolean allow = isAllowHandle(userToken , KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode() , so.getSiteCode());
                if(allow){
                    //查询数量
                    Long count = kissPromoService.queryPromoCount(so);
                    so.setDataCount(count);
                    List<KissPromoRo> roList = kissPromoService.queryPromoList(so);
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
     * 生成优惠码
     * @return
     */
    @POST
    @Path("/gen")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissPromoRo> genPromo(String json ,
                                                     @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissPromoRo> result = new WaterBootResultBean<KissPromoRo>();
        try{
            if(null != json && !"".equals(json)){
                KissPromoGenRo genRo = JSON.parseObject(json , KissPromoGenRo.class);
                boolean flag = true;
                if(null == genRo){
                    flag = false;
                    result.setMsg("参数有误");
                }
                if(flag && (null == genRo.getSiteCode() || "".equals(genRo.getSiteCode()))){
                    flag = false;
                    result.setMsg("场所编码有误");
                }
                if(flag && (null == genRo.getGoodsCode()) || "".equals(genRo.getGoodsCode())){
                    flag = false;
                    result.setMsg("商品编码有误");
                }
                if(flag && (null == genRo.getStartTime())){
                    flag = false;
                    result.setMsg("有效期开始时间有误");
                }
                if(flag && (null == genRo.getEndTime())){
                    flag = false;
                    result.setMsg("有效期结束时间有误");
                }
                if(flag && (WaterDateUtil.compareDate(genRo.getEndTime() , genRo.getStartTime()) <= 0)){
                    flag = false;
                    result.setMsg("有效期结束时间必须大于开始时间");
                }
                if(flag && (WaterDateUtil.compareDate(genRo.getEndTime() , new Date()) <= 0)){
                    flag = false;
                    result.setMsg("结束时间必须大于现在时间");
                }
                if(flag && (null == genRo.getCount() || genRo.getCount().intValue() <= 0 || genRo.getCount().intValue() > 1000)){
                    flag = false;
                    result.setMsg("需生成优惠码的数量必须在1-1000之间");
                }
                String goodsName =  null;
                if(flag){
                    //验证商品编码是否存在
                    KissGoodsRo goodsRo = kissGoodsService.queryGoodsByCode(genRo.getGoodsCode());
                    if(null == goodsRo || null == goodsRo.getGoodsStatus()){
                        flag = false;
                        result.setMsg("商品不存在");
                    }
                    if(flag && goodsRo.getGoodsStatus().intValue() != KissCommonContext.GoodsContext.ONLINE.getCode()){
                        flag = false;
                        result.setMsg("商品不是已上线状态");
                    }
                    if(flag){
                        goodsName = goodsRo.getGoodsName();
                    }
                }
                String siteName = null;
                if(flag){
                    //验证场所是否存在
                    KissSiteRo siteRo = kissSiteService.querySiteByCode(genRo.getSiteCode());
                    if(null == siteRo){
                        flag = false;
                        result.setMsg("场所不存在");
                    }
                    if(flag){
                        siteName = siteRo.getSiteName();
                    }
                }
                if(flag){
                    boolean allowAdd = isAllowAdd(userToken , KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode());
                    if(allowAdd){
                        Set<String> codes = KissCommonPromoUtil.genPromoCode(genRo.getCount());
                        //数据库验证
                        if(null != codes && !codes.isEmpty()){
                            Long codeCount = kissPromoService.queryPromoCountByCodes(codes);
                            if(null != codeCount && codeCount.longValue() == 0l){
                                List<KissPromoRo> promoRoList = new ArrayList<KissPromoRo>();
                                for(String code : codes){
                                    KissPromoRo promoRo = new KissPromoRo();
                                    promoRo.setPromoCode(code);
                                    promoRo.setSiteCode(genRo.getSiteCode());
                                    promoRo.setSiteName(siteName);
                                    promoRo.setGoodsCode(genRo.getGoodsCode());
                                    promoRo.setGoodsName(goodsName);
                                    promoRo.setStartTime(genRo.getStartTime());
                                    promoRo.setEndTime(genRo.getEndTime());
                                    promoRoList.add(promoRo);
                                    //插入数据库
                                    kissPromoService.addPromo(promoRo);
                                }
                                result.setRst(promoRoList);
                                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                result.setMsg("生成成功");

                            }else {
                                result.setMsg("找到重复编码");
                            }
                        }else {
                            result.setMsg("未生成编码");
                        }
                    }else {
                       result.setMsg("该用户无权限操作");
                    }
                }
            }else {
                result.setMsg("参数不能为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 删除
     * @return
     */
    @GET
    @Path("/delete/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissPromoRo> delete(@PathParam("code") String code,
                                                   @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissPromoRo> result = new WaterBootResultBean<KissPromoRo>();
        try {
            //查询优惠码
            KissPromoRo ro = kissPromoService.queryPromoByCode(code);
            if(null != ro && null != ro.getSiteCode()){
                boolean allow = isAllowEdit(userToken , KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode() , ro.getSiteCode());
                if(allow){
                    kissPromoService.deletePromo(code);
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

}
