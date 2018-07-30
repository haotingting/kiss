/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.controller.device;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceHandleRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissGoodsRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissOrderRo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.common.util.KissCommonOrderUtil;
import com.zjtachao.fish.kiss.data.service.device.KissDeviceService;
import com.zjtachao.fish.kiss.data.service.goods.KissGoodsService;
import com.zjtachao.fish.kiss.data.service.order.KissOrderService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
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
 * 查询设备状态
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/device")
public class KissDeviceController extends WaterBootBaseController{

    /** 设备服务*/
    @Autowired
    private KissDeviceService kissDeviceService;

    /** 订单服务 **/
    @Autowired
    private KissOrderService kissOrderService;

    /** 商品服务 **/
    @Autowired
    private KissGoodsService kissGoodsService;

    /** 最小日期 **/
    @Value("${com.zjtachao.fish.kiss.data.normal.param.min-date}")
    private String minOrderDate;

    /**
     * 启动设备
     * @return
     */
    @POST
    @Path("/admin/start")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceHandleRo> startDevice(String json){
        WaterBootResultBean<KissDeviceHandleRo> result = new WaterBootResultBean<KissDeviceHandleRo>();
        try {
            boolean flag = true;
            if(null != json && !"".equals(json)){
                KissDeviceHandleRo handleRo = JSON.parseObject(json , KissDeviceHandleRo.class);
                if(flag && null == handleRo){
                    flag = false;
                    result.setMsg("参数错误");
                }
                if(flag && (null == handleRo.getOrderNumber() || handleRo.getOrderNumber().length() < 20)
                        || !KissCommonOrderUtil.validateOrderNumber(handleRo.getOrderNumber() , minOrderDate)){
                    flag = false;
                    result.setMsg("订单号码错误");
                }
                if(flag && (null == handleRo.getGoodsCode() || "".equals(handleRo.getGoodsCode()))){
                    flag = false;
                    result.setMsg("商品编码错误");
                }
                if(flag && (null == handleRo.getDeviceSerialNumber() || "".equals(handleRo.getDeviceSerialNumber()))){
                    flag = false;
                    result.setMsg("设备序列号错误");
                }

                if(flag){
                    KissOrderRo orderRo = kissOrderService.queryOrderByCode(handleRo.getOrderNumber() , minOrderDate);
                    if(null != orderRo && null != orderRo.getOrderNumber()){
                        if(!handleRo.getOrderNumber().equals(orderRo.getOrderNumber())){
                            flag = false;
                            result.setMsg("订单号码不匹配");
                        }
                        if(flag && (!handleRo.getGoodsCode().equals(orderRo.getPayGoods()))){
                            flag = false;
                            result.setMsg("商品编号不匹配");
                        }
                        //通过序列号查询订单
                        if(flag){
                            KissDeviceRo deviceRo = kissDeviceService.queryDeviceBySerialNumber(handleRo.getDeviceSerialNumber());
                            if(null == deviceRo || null == deviceRo.getDeviceCode()){
                                flag = false;
                                result.setMsg("未找到设备信息");
                            }
                            if(flag && (!deviceRo.getDeviceSerialNumber().equals(handleRo.getDeviceSerialNumber()))){
                                flag = false;
                                result.setMsg("设备序列号错误");
                            }
                            //验证是否超时
                            if(flag){
                                Date expireDate = WaterDateUtil.addMinutes(new Date() , 60);
                                if(WaterDateUtil.compareDate(expireDate , orderRo.getOrderTime()) < 1){
                                    flag = false;
                                    result.setMsg("该订单已过期");
                                }
                            }
                            //TODO 查询设备是否正常
                            //TODO 开启设备
                            if(flag){
                                long time = queryGoodsTime(handleRo.getGoodsCode());
                                result.setCount(time);
                                result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                                result.setMsg("启动成功");
                            }

                        }

                    }else {
                        flag = false;
                        result.setMsg("未找到订单信息");
                    }
                }

            }else {
                flag = false;
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
     * 停止设备
     * @return
     */
    @POST
    @Path("/admin/stop")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceHandleRo> stopDevice(String json){
        WaterBootResultBean<KissDeviceHandleRo> result = new WaterBootResultBean<KissDeviceHandleRo>();
        try {
            boolean flag = true;
            if(null != json && !"".equals(json)){
                KissDeviceHandleRo handleRo = JSON.parseObject(json , KissDeviceHandleRo.class);
                if(flag && (null == handleRo.getDeviceSerialNumber() || "".equals(handleRo.getDeviceSerialNumber()))){
                    flag = false;
                    result.setMsg("设备序列号错误");
                }

                if(flag){

                    //TODO 查询设备是否正常
                    //TODO 停止设备
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("停止成功");
                }

            }else {
                flag = false;
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
     * 查询商品剩余时间
     * @param goodsCode
     * @return
     */
    private long queryGoodsTime(String goodsCode){
        long time = 0;
        List<KissGoodsRo> roList = null;
        String json = waterRedis.queryString(KissCommonConstant.KISS_COMMON_GOODS_CACHE_LIST);
        if(null != json && !"".equals(json)){
            roList = JSON.parseArray(json , KissGoodsRo.class);
        }else{
            roList = kissGoodsService.queryAllGoodsList();
            waterRedis.set(KissCommonConstant.KISS_COMMON_GOODS_CACHE_LIST , JSON.toJSONString(roList));
        }
        if(null != roList && !roList.isEmpty()){
            for(KissGoodsRo ro : roList){
                if(null != ro && null != ro.getGoodsCode()
                        && goodsCode.equals(ro.getGoodsCode())
                        && null != ro.getGoodsUnit()){
                    time = ro.getGoodsUnit().intValue()*60;
                    break;
                }
            }
        }
        return time;
    }

}
