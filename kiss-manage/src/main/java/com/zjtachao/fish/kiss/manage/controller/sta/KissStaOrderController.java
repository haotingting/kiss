/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.controller.sta;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissStaOrderRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissStaOrderTotalRo;
import com.zjtachao.fish.kiss.common.bean.so.KissStaOrderSo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.controller.base.KissManageBaseController;
import com.zjtachao.fish.kiss.manage.service.order.KissOrderService;
import com.zjtachao.fish.kiss.manage.service.sta.KissStaOrderService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.tool.WaterBigdecimalUtil;
import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单统计
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/sta/order")
public class KissStaOrderController extends KissManageBaseController {

    /** 订单统计服务 **/
    @Autowired
    private KissStaOrderService kissStaOrderService;

    /** 查询订单数据 **/
    @Autowired
    private KissOrderService kissOrderService;

    /** 时间参数 **/
    private String DATE_FORMAT = "yyyyMM";

    /** 最小日期 **/
    @Value("${com.zjtachao.fish.kiss.manage.normal.param.min-date}")
    private String minOrderDate;

    /**
     * 生成数据
     * @return
     */
    @POST
    @Path("/gen")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissStaOrderRo> gen(String json){
        WaterBootResultBean<KissStaOrderRo> result = new WaterBootResultBean<KissStaOrderRo>();
        try {
            if(null != json && !"".equals(json)){
                boolean flag = true;
                KissStaOrderSo so = JSON.parseObject(json , KissStaOrderSo.class);
                if(null == so){
                    flag = false;
                    result.setMsg("参数为空");
                }
                if(flag && null == so.getGenDate()){
                    flag = false;
                    result.setMsg("日期为空");
                }
                if(flag){
                    Date minDate = WaterDateUtil.str2Date(minOrderDate , DATE_FORMAT);
                    Date maxDate = WaterDateUtil.moveBeginOfDay(new Date());
                    if(so.getGenDate().getTime() >= minDate.getTime() && so.getGenDate().getTime() < maxDate.getTime()){
                        kissStaOrderService.genStaOrderList(so.getGenDate());
                        result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        result.setMsg("生成成功");
                    }else {
                        flag = false;
                        result.setMsg("时间范围有误");
                    }
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
     * 查询列表
     * @return
     */
    @POST
    @Path("/query/list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissStaOrderTotalRo> queryList(String json ,
                                                              @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissStaOrderTotalRo> result = new WaterBootResultBean<KissStaOrderTotalRo>();
        try {
            if(null != json && !"".equals(json)){
                KissStaOrderSo so = JSON.parseObject(json , KissStaOrderSo.class);
                boolean flag = true;
                if(null == so){
                    flag = false;
                    result.setMsg("参数为空");
                }
                if(flag && (null == so.getDays() || so.getDays().intValue() < 0 || so.getDays().intValue() > 30)){
                    flag = false;
                    result.setMsg("时长错误，时长需在0-30天之间");
                }
                if(flag) {
                    boolean allowFlag = isAllowHandle(userToken  , KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode(), so.getSiteCode());
                    if(allowFlag){
                        if(so.getDays().intValue() > 1){
                            Date now = new Date();
                            Date maxDate = WaterDateUtil.addDays(now , -1);
                            Date minDate = WaterDateUtil.addDays(now , -so.getDays());
                            String maxDateStr = WaterDateUtil.date2Str(maxDate , "yyyyMMdd");
                            String minDateStr = WaterDateUtil.date2Str(minDate , "yyyyMMdd");
                            so.setMaxPeriodDay(Integer.parseInt(maxDateStr));
                            so.setMinPeriodDay(Integer.parseInt(minDateStr));
                            List<KissStaOrderRo> roList = kissStaOrderService.queryStaOrderList(so);
                            //补全数据
                            List<KissStaOrderRo> supplyList = new ArrayList<KissStaOrderRo>();

                            int currentCount = 0;
                            BigDecimal currentAmount = new BigDecimal(0);

                            for(int i=0;i < so.getDays(); i++){
                                Date currentDate = WaterDateUtil.addDays(minDate , i);
                                String currentDateStr = WaterDateUtil.date2Str(currentDate , "yyyyMMdd");
                                int period = Integer.parseInt(currentDateStr);
                                KissStaOrderRo orderRo = this.queryStaOrderRo(period , roList);
                                if(null != orderRo){
                                    if(null != orderRo.getTotalCount()){
                                        currentCount = currentCount + orderRo.getTotalCount();
                                    }
                                    if(null != orderRo.getTotalAmount()){
                                        currentAmount = WaterBigdecimalUtil.plus(currentAmount , orderRo.getTotalAmount());
                                    }
                                }
                                supplyList.add(orderRo);
                            }

                            KissStaOrderTotalRo totalRo = new KissStaOrderTotalRo();
                            totalRo.setDetailList(supplyList);
                            totalRo.setCurrentAmount(currentAmount);
                            totalRo.setCurrentCount(currentCount);

                            //查询总数
                            KissStaOrderRo staOrderRo = kissStaOrderService.queryStaOrderTotal(so);
                            if(null != staOrderRo){
                                if(null != staOrderRo.getTotalCount()){
                                    totalRo.setTotalCount(staOrderRo.getTotalCount());
                                }
                                if(null != staOrderRo.getTotalAmount()){
                                    totalRo.setTotalAmount(staOrderRo.getTotalAmount());
                                }
                            }

                            if(null == totalRo.getTotalCount()){
                                totalRo.setTotalCount(0);
                            }
                            if(null == totalRo.getTotalAmount()){
                                totalRo.setTotalAmount(new BigDecimal(0));
                            }

                            result.setRst(totalRo);
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("查询成功");
                        }else {
                            //直接查询订单表
                            Date now = new Date();
                            Date startTime = WaterDateUtil.addDays(now , -so.getDays());
                            startTime = WaterDateUtil.moveBeginOfDay(startTime);
                            Date endTime = WaterDateUtil.addDay(startTime);

                            List<KissStaOrderRo> roList = kissOrderService.queryStaOrderRealTimeList(startTime , endTime , so.getSiteCode() ,
                                    so.getGoodsCode() , so.getDeviceSerialNumber() , so.getPayWay());

                            //补全数据
                            List<KissStaOrderRo> supplyList = new ArrayList<KissStaOrderRo>();

                            int currentCount = 0;
                            BigDecimal currentAmount = new BigDecimal(0);

                            for(int i=0;i < 24; i++){
                                Date currentDate = WaterDateUtil.addHours(startTime , i);
                                String currentDateStr = WaterDateUtil.date2Str(currentDate , "HH");
                                int period = Integer.parseInt(currentDateStr);
                                KissStaOrderRo orderRo = this.queryStaOrderRo(period , roList);
                                if(null != orderRo){
                                    if(null != orderRo.getTotalCount()){
                                        currentCount = currentCount + orderRo.getTotalCount();
                                    }
                                    if(null != orderRo.getTotalAmount()){
                                        currentAmount = WaterBigdecimalUtil.plus(currentAmount , orderRo.getTotalAmount());
                                    }
                                }
                                supplyList.add(orderRo);
                            }
                            KissStaOrderTotalRo totalRo = new KissStaOrderTotalRo();
                            totalRo.setDetailList(supplyList);
                            totalRo.setCurrentAmount(currentAmount);
                            totalRo.setCurrentCount(currentCount);

                            //查询总数
                            KissStaOrderRo staOrderRo = kissStaOrderService.queryStaOrderTotal(so);
                            if(null != staOrderRo){
                                if(null != staOrderRo.getTotalCount()){
                                    totalRo.setTotalCount(staOrderRo.getTotalCount());
                                }
                                if(null != staOrderRo.getTotalAmount()){
                                    totalRo.setTotalAmount(staOrderRo.getTotalAmount());
                                }
                            }

                            if(null == totalRo.getTotalCount()){
                                totalRo.setTotalCount(0);
                            }
                            if(null == totalRo.getTotalAmount()){
                                totalRo.setTotalAmount(new BigDecimal(0));
                            }
                            result.setRst(totalRo);
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("查询成功");
                        }
                    }else {
                        result.setMsg("该用户无权限操作");
                    }
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
     * 查询统计数据
     * @param periodDay
     * @param roList
     * @return
     */
    private KissStaOrderRo queryStaOrderRo(int periodDay , List<KissStaOrderRo> roList){
        KissStaOrderRo ro = null;
        if(null != roList && !roList.isEmpty()){
            for(KissStaOrderRo staOrderRo : roList){
                if(null != staOrderRo && null != staOrderRo.getPeriodDay()
                        && staOrderRo.getPeriodDay().intValue() == periodDay){
                    ro = staOrderRo;
                    break;
                }
            }
        }
        if(null == ro){
            ro = new KissStaOrderRo();
            ro.setPeriodDay(periodDay);
            ro.setTotalAmount(new BigDecimal(0));
            ro.setTotalCount(0);
        }
        return ro;
    }

}
