/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.controller.order;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissOrderRo;
import com.zjtachao.fish.kiss.common.bean.so.KissOrderSo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.controller.base.KissManageBaseController;
import com.zjtachao.fish.kiss.manage.service.order.KissOrderService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import com.zjtachao.fish.water.common.tool.WaterValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

/**
 * 订单管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/order")
public class KissOrderController extends KissManageBaseController {

    /** 订单服务 **/
    @Autowired
    private KissOrderService kissOrderService;

    /** 时间参数 **/
    private String DATE_FORMAT = "yyyyMM";

    /** 最小日期 **/
    @Value("${com.zjtachao.fish.kiss.manage.normal.param.min-date}")
    private String minOrderDate;

    /**
     * 查询单条订单
     * @return
     */
    @GET
    @Path("/query/single/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissOrderRo> querySingle(@PathParam("code") String code ,
                                                        @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissOrderRo> result = new WaterBootResultBean<KissOrderRo>();
        try {
            String maxOrderDate = WaterDateUtil.getCurrentDateStr(DATE_FORMAT);
            if(null != code && !"".equals(code) && code.length() >= 20){
                String prefixOrder = code.substring(0,6);
                if(WaterValidateUtil.validateNumber(prefixOrder)){
                    int min = Integer.parseInt(minOrderDate);
                    int max = Integer.parseInt(maxOrderDate);
                    int current = Integer.parseInt(prefixOrder);
                    if(current>= min && current <= max){
                        KissOrderRo ro = kissOrderService.queryOrderByCode(code , prefixOrder);
                        if(null != ro && null != ro.getStieCode()){
                            boolean allow = isAllowHandle(userToken , KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode() , ro.getStieCode());
                            if(allow){
                                //判断权限
                                result.setRst(ro);
                                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                result.setMsg("查询成功");
                            }else {
                                result.setMsg("该用户无权操作");
                            }
                        }else {
                            result.setMsg("未找到数据");
                        }
                    }else{
                        result.setMsg("订单号码不合法");
                    }
                }else{
                    result.setMsg("订单号码格式错误");
                }
            }else{
                result.setMsg("订单号码错误");
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
    public WaterBootResultBean<KissOrderRo> queryList(String json,
                                                      @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissOrderRo> result = new WaterBootResultBean<KissOrderRo>();
        try {
            if(null != json && !"".equals(json)){
                KissOrderSo so = JSON.parseObject(json , KissOrderSo.class);
                boolean flag = true;
                String msg = null;
                if(null != so){
                    if(flag && null == so.getStartTime()){
                        flag = false;
                        msg = "开时时间不能为空！";
                    }
                    if(flag && null == so.getEndTime()){
                        flag = false;
                        msg = "结束时间不能为空！";
                    }
                    if(flag && WaterDateUtil.compareDate(so.getEndTime() , so.getStartTime()) != 1 ){
                        flag = false;
                        msg = "结束时间必须大于开始时间";
                    }
                    if(flag){
                        Date minDate = WaterDateUtil.str2Date(minOrderDate ,DATE_FORMAT);
                        if(WaterDateUtil.compareDate(so.getStartTime() , minDate) < 0 ){
                            flag = false;
                            msg = "开始时间必须大于等"+minOrderDate;
                        }
                        Date date = new Date();
                        date = WaterDateUtil.addDay(date);
                        if(flag && (WaterDateUtil.compareDate(date , so.getEndTime()) < 0)){
                            flag = false;
                            msg = "最大时间必须小等于今天";
                        }
                        String min = WaterDateUtil.date2Str(so.getStartTime() , DATE_FORMAT);
                        String max = WaterDateUtil.date2Str(so.getEndTime() , DATE_FORMAT);
                        if(!min.equals(max)){
                            flag = false;
                            msg = "最小时间和最大时间的月份必须相同";
                        }
                    }
                }else{
                    flag = false;
                    msg = "参数错误";
                }
                if(flag){
                    boolean allowFlag = isAllowHandle(userToken  , KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode(), so.getStieCode());
                    if(allowFlag){
                        String tbDate = WaterDateUtil.date2Str(so.getStartTime() , DATE_FORMAT);
                        so.setTbDate(tbDate);
                        //查询数量
                        Long count = kissOrderService.queryOrderCount(so);
                        so.setDataCount(count);
                        List<KissOrderRo> roList = kissOrderService.queryOrderList(so);
                        result.setRst(roList);
                        result.setCount(count);
                        result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        result.setMsg("查询成功");
                    }else {
                        result.setMsg("该用户无权限操作");
                    }
                }else {
                    result.setMsg(msg);
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

}
