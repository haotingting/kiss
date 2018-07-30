/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.controller.pay;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.*;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.data.service.device.KissDeviceService;
import com.zjtachao.fish.kiss.data.service.goods.KissGoodsService;
import com.zjtachao.fish.kiss.data.service.order.KissOrderService;
import com.zjtachao.fish.kiss.data.service.promo.KissPromoService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import com.zjtachao.fish.water.common.tool.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/pay")
public class KissPayController extends WaterBootBaseController{

    /** 时间格式化 **/
    private String dateFormat = "yyyyMMddHHmmss";

    /** 商品服务 **/
    @Autowired
    private KissGoodsService kissGoodsService;

    /** 设备服务 **/
    @Autowired
    private KissDeviceService kissDeviceService;

    /** 订单服务 **/
    @Autowired
    private KissOrderService kissOrderService;

    /** 优惠码服务 **/
    @Autowired
    private KissPromoService kissPromoService;

    /** 最小日期 **/
    @Value("${com.zjtachao.fish.kiss.data.normal.param.min-date}")
    private String minOrderDate;

    /** appid **/
    @Value("${com.zjtachao.fish.kiss.data.program.appid}")
    private String appid;

    /** appsecret **/
    @Value("${com.zjtachao.fish.kiss.data.program.appsecret}")
    private String appsecret;

    /** machid **/
    @Value("${com.zjtachao.fish.kiss.data.program.machid}")
    private String machid;

    /** machsecret **/
    @Value("${com.zjtachao.fish.kiss.data.program.machsecret}")
    private String machsecret;

    /** notifyurl **/
    @Value("${com.zjtachao.fish.kiss.data.program.notifyurl}")
    private String notifyurl;

    /** payurl **/
    @Value("${com.zjtachao.fish.kiss.data.program.payurl}")
    private String payurl;

    /**
     * 支付成功通知
     * @return
     */
    @POST
    @Path("/program/notify")
    @Produces(MediaType.APPLICATION_XML)
    public String loginNormal(String xml) {
        String return_code = "FAIL";
        String return_msg = "";
        try {
            logger.info("微信小程序支付通知："+xml);
            if(null != xml && !"".equals(xml)){
                Map<String , String> map = WaterWxPayUtil.xmlToMap(xml);
                if(null != map && null != map.get("return_code")
                        && "SUCCESS".equals(map.get("return_code"))
                        && null != map.get("result_code")
                        && "SUCCESS".equals(map.get("result_code"))){
                    String orderNumber = map.get("out_trade_no");
                    String payNumber = map.get("transaction_id");
                    if(null != orderNumber && !"".equals(orderNumber)
                            && null != payNumber && !"".equals(payNumber)){
                        //更新状态
                        kissOrderService.updateOrderPaySuccess(orderNumber , payNumber , minOrderDate);
                        return_code = "SUCCESS";
                        return_msg = "OK";
                    }else {
                        return_msg = "参数格式交易错误";
                    }
                }else {
                    return_msg = "参数格式交易错误";
                }
            }else {
                return_msg = "参数为空";
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            return_msg = "未知错误";
        }
        String info = programNotify(return_code , return_msg);
        return info;
    }

    /**
     * 优惠码
     * @return
     */
    @POST
    @Path("/admin/promo")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissPromoOrderResponseRo> loginNormal(String json,
                                                                      @Context HttpServletResponse response,
                                                                      @Context HttpServletRequest request,
                                                                      @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String logintoken){
        WaterBootResultBean<KissPromoOrderResponseRo> result = new WaterBootResultBean<KissPromoOrderResponseRo>();
        try {
            boolean flag = true;
            if(null != json && !"".equals(json)) {
                KissPayBaseRo ro = JSON.parseObject(json, KissPayBaseRo.class);
                if (null == ro) {
                    flag = false;
                    result.setMsg("参数为空");
                }
                if (flag && (null == ro.getPromoCode() || "".equals(ro.getPromoCode()))) {
                    flag = false;
                    result.setMsg("兑换码为空");
                }
                if (flag && (null == ro.getDeviceSerialNumber() || "".equals(ro.getDeviceSerialNumber()))) {
                    flag = false;
                    result.setMsg("设备编码为空");
                }
                if(flag){
                    //查询设备状态
                    Long time = waterRedis.ttl(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+ro.getDeviceSerialNumber());
                    if(null != time && time.longValue() > 0l){
                        flag = false;
                        result.setMsg("该设备正在使用中，剩余"+time+"秒，请稍候再试");
                    }
                }
                String goodsCode = null;
                KissPromoRo promoRo = null;
                if(flag){
                    promoRo = kissPromoService.queryPromoByCode(ro.getPromoCode());
                    if(null == promoRo){
                        flag = false;
                        result.setMsg("未找到该兑换码");
                    }
                    if(flag && (null != promoRo.getGoodsCode() && !"".equals(promoRo.getGoodsCode()))){
                        goodsCode = promoRo.getGoodsCode();
                    }else {
                        flag = false;
                        result.setMsg("未找到兑换码对应的商品");
                    }
                    if(flag && (null == promoRo.getPromoStatus() || promoRo.getPromoStatus().intValue() != KissCommonContext.PromoStatusContext.NORMAL.getCode())){
                        flag = false;
                        result.setMsg("该兑换码已失效");
                    }

                }
                KissDeviceRo deviceRo = null;
                if(flag){
                     deviceRo = kissDeviceService.queryDeviceBySerialNumber(ro.getDeviceSerialNumber());
                    if(null == deviceRo){
                        flag = false;
                        result.setMsg("未找到该设备");
                    }
                    if(flag && (null == deviceRo.getDeviceStatus() || deviceRo.getDeviceStatus().intValue() != KissCommonContext.DeviceContext.ONLINE.getCode())){
                        flag = false;
                        result.setMsg("该设备不在线");
                    }
                }
                KissGoodsRo goodsRo = null;
                if(flag){
                    goodsRo = kissGoodsService.queryGoodsByCode(goodsCode);
                    if(null == goodsRo){
                        flag = false;
                        result.setMsg("未找到兑换码对应的商品");
                    }
                    if(flag && (null == goodsRo.getGoodsStatus() || goodsRo.getGoodsStatus().intValue() != KissCommonContext.GoodsContext.ONLINE.getCode())){
                        flag = false;
                        result.setMsg("兑换码对应的商品已过期");
                    }
                }
                String userCode = null;
                String userName = null;
                if(flag){
                    String userJson = waterRedis.get(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+logintoken);
                    if(null != userJson && !"".equals(userJson)){
                        KissBaseUserRo userRo = JSON.parseObject(userJson , KissBaseUserRo.class);
                        if(null != userRo && null != userRo.getUserCode()){
                            userCode = userRo.getUserCode();
                            userName = userRo.getUserNickname();
                        }
                    }else {
                        flag = false;
                        result.setMsg("未找到用户登录信息");
                    }
                }
                if(flag){
                    if(!promoRo.getSiteCode().equals(goodsRo.getSiteCode())){
                        flag = false;
                        result.setMsg("该兑换码在本场所不能使用");
                    }
                }
                if(flag){
                    //创建订单
                    KissOrderRo orderRo = buildPromoKissOrder(goodsRo , goodsRo.getSiteCode() , deviceRo.getDeviceCode() , deviceRo.getDeviceSerialNumber(),  userCode , userName , ro.getPromoCode());
                    kissOrderService.addOrder(orderRo);
                    //修改状态
                    kissPromoService.updatePromoStatus(ro.getPromoCode() , userCode);
                    KissPromoOrderResponseRo responseRo = new KissPromoOrderResponseRo();
                    responseRo.setDeviceSerialNumber(deviceRo.getDeviceSerialNumber());
                    responseRo.setGoodsCode(goodsRo.getGoodsCode());
                    responseRo.setOrderNumber(orderRo.getOrderNumber());
                    result.setRst(responseRo);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("兑换成功！");
                }
            }else {
                flag = false;
                result.setMsg("参数有误");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
}
    /**
     * 小程序
     * @return
     */
    @POST
    @Path("/admin/program")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissWechatOrderResponseRo> payProgram(String json,
                                                           @Context HttpServletResponse response,
                                                          @Context HttpServletRequest request,
                                                           @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY) String logintoken){
        WaterBootResultBean<KissWechatOrderResponseRo> result = new WaterBootResultBean<KissWechatOrderResponseRo>();
        try {

            boolean flag = true;
            if(null != json && !"".equals(json)) {
                KissPayBaseRo ro = JSON.parseObject(json, KissPayBaseRo.class);
                if (null == ro) {
                    flag = false;
                    result.setMsg("参数为空");
                }
                if(flag && (null == ro.getGoodsCode() || "".equals(ro.getGoodsCode()))){
                    flag = false;
                    result.setMsg("商品编码为空");
                }
                if(flag && (null == ro.getDeviceSerialNumber() || "".equals(ro.getDeviceSerialNumber()))){
                    flag = false;
                    result.setMsg("设备编码为空");
                }
                String userCode = null;
                String userName = null;
                String openid = null;
                if(flag){
                    String userJson = waterRedis.get(KissCommonConstant.KISS_COMMON_USER_LOGIN_CACHE_PREFIX+logintoken);
                    if(null != userJson && !"".equals(userJson)){
                        KissBaseUserRo userRo = JSON.parseObject(userJson , KissBaseUserRo.class);
                        if(null != userRo && null != userRo.getUserCode()){
                            userCode = userRo.getUserCode();
                            userName = userRo.getUserNickname();
                            openid = userRo.getUserOpenid();
                        }
                    }else {
                        flag = false;
                        result.setMsg("未找到用户登录信息");
                    }
                }

                String siteCode = null;
                String deviceCode = null;
                String deviceSerialNumber = null;
                if(flag){
                    KissDeviceRo deviceRo = kissDeviceService.queryDeviceBySerialNumber(ro.getDeviceSerialNumber());
                    if(null != deviceRo && null != deviceRo.getSiteCode() && null != deviceRo.getDeviceMode()){
                        siteCode = deviceRo.getSiteCode();
                        deviceCode = deviceRo.getDeviceCode();
                        deviceSerialNumber = deviceRo.getDeviceSerialNumber();
                    }else{
                        flag = false;
                        result.setMsg("设备故障");
                    }
                }

                if(flag){
                    //查询设备状态
                    Long time = waterRedis.ttl(KissCommonConstant.KISS_COMMON_DEVICE_WORK_PREFIX+ro.getDeviceSerialNumber());
                    if(null != time && time.longValue() > 0l){
                        flag = false;
                        result.setMsg("该设备正在使用中，剩余"+time+"秒，请稍候再试");
                    }
                }

                //查询商品
                if(flag){
                    KissGoodsRo goodsRo = kissGoodsService.queryGoodsByCode(ro.getGoodsCode());
                    if(null != goodsRo
                            && null != goodsRo.getGoodsStatus()
                            && KissCommonContext.GoodsContext.ONLINE.getCode() == goodsRo.getGoodsStatus().intValue()
                            && null != goodsRo.getGoodsName()
                            && null != goodsRo.getGoodsPrice()
                            && null != goodsRo.getGoodsUnit()
                            && null != goodsRo.getGoodsDesc()){
                        KissOrderRo orderRo = buildWechatKissOrder(goodsRo , siteCode , deviceCode , deviceSerialNumber, userCode , userName);
                        kissOrderService.addOrder(orderRo);
                        KissWechatPayRo payRo = buildWechatPay(goodsRo , orderRo , openid , request);
                        String reponseXml = excuteWechatPay(payRo);
                        logger.info("微信订单生成："+reponseXml);
                        Map<String , String> responseMap = WaterWxPayUtil.xmlToMap(reponseXml);
                        if(null != responseMap
                                && null != responseMap.get("return_code")
                                && "SUCCESS".equals(responseMap.get("return_code"))
                                && null != responseMap.get("result_code")
                                && "SUCCESS".equals(responseMap.get("result_code"))){
                            String responseJson = JSON.toJSONString(responseMap);
                            KissWechatOrderResponseRo wechatOrderResponseRo = JSON.parseObject(responseJson , KissWechatOrderResponseRo.class);
                            wechatOrderResponseRo.setTimestamp(String.valueOf(orderRo.getOrderTime().getTime()/1000));
                            String sign = getSign(wechatOrderResponseRo);
                            wechatOrderResponseRo.setSign(sign);
                            wechatOrderResponseRo.setOrderNumber(orderRo.getOrderNumber());
                            result.setRst(wechatOrderResponseRo);
                            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            result.setMsg("生成成功！");
                        }else {
                            flag = false;
                            result.setMsg("微信支付订单创建失败！");
                        }
                    }else {
                        flag = false;
                        result.setMsg("商品有误");
                    }
                }

            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }


    /**
     * 构建微信支付bean
     * @param goodsRo
     * @param orderRo
     * @param openid
     * @param request
     * @return
     */
    private KissWechatPayRo buildWechatPay(KissGoodsRo goodsRo , KissOrderRo orderRo , String openid ,HttpServletRequest request){
        KissWechatPayRo payRo = new KissWechatPayRo();
        //小程序id
        payRo.setAppid(appid);
        //商户号
        payRo.setMch_id(machid);
        //随机字符串
        payRo.setNonce_str(WaterUUIDUtil.getUUID());
        //商品描述
        payRo.setBody(goodsRo.getGoodsDesc());
        //类型
        payRo.setSign_type(WaterWxPayUtil.MD5);
        //商户订单号
        payRo.setOut_trade_no(orderRo.getOrderNumber());
        //币种类型
        payRo.setFee_type(orderRo.getPayCurrency());
        //总金额
        payRo.setTotal_fee(orderRo.getPayAmount().multiply(new BigDecimal("100")).intValue());
        //ip
        payRo.setSpbill_create_ip(getIpAddress(request));
        //交易开始时间
        Date date = new Date();
        String startTime = WaterDateUtil.date2Str(date , dateFormat);
        Date expireDate = WaterDateUtil.addMinutes(date , 30);
        String expireTime = WaterDateUtil.date2Str(expireDate , dateFormat);
        //交易开始时间
        payRo.setTime_start(startTime);
        //交易结束时间
        payRo.setTime_expire(expireTime);
        //通知url
        payRo.setNotify_url(notifyurl);
        //交易类型
        payRo.setTrade_type("JSAPI");
        //opened
        payRo.setOpenid(openid);
        return payRo;
    }

    /**
     * 构建order
     * @param goodsRo
     * @param siteCode
     * @param deviceCode
     * @param userCode
     * @param userName
     * @return
     */
    private KissOrderRo buildPromoKissOrder(KissGoodsRo goodsRo , String siteCode ,
                                             String deviceCode , String deviceSerialNumber, String userCode , String userName,
                                            String promoCode){
        KissOrderRo orderRo = new KissOrderRo();
        Date date = new Date();
        //订单号码
        orderRo.setOrderNumber(WaterOrderUtil.getOrderNo());
        //订单创建时间
        orderRo.setOrderTime(date);
        //场所编码
        orderRo.setStieCode(siteCode);
        //设备编码
        orderRo.setDeviceCode(deviceCode);
        //设备序列号
        orderRo.setDeviceSerialNumber(deviceSerialNumber);
        //支付方式
        orderRo.setPayWay(KissCommonContext.PayWayContext.PROMO.getCode());
        //支付来源
        orderRo.setPaySource(KissCommonContext.PaySourceContext.PROGRAM.getCode());
        //支付编号
        orderRo.setPayNumber(promoCode);
        //支付币种
        orderRo.setPayCurrency("CNY");
        //支付商品名称
        orderRo.setPayGoods(goodsRo.getGoodsCode());
        //支付金额
        orderRo.setPayAmount(goodsRo.getGoodsPrice());
        //支付数量
        orderRo.setPayQuantity(goodsRo.getGoodsUnit());
        //支付标题
        orderRo.setPayTitle(goodsRo.getGoodsName());
        //支付描述
        orderRo.setPayDesc(goodsRo.getGoodsDesc());
        //支付备注
        orderRo.setPayRemark(goodsRo.getRemark());
        //支付状态
        orderRo.setPayStatus(KissCommonContext.PayStatusContext.SELLER_CREATE.getCode());
        //买方编号
        orderRo.setPayBuyerCode(userCode);
        //买方名称
        orderRo.setPayBuyerName(userName);
        //支付状态
        orderRo.setPayStatus(KissCommonContext.PayStatusContext.TRADE_SUCCESS.getCode());
        //支付时间
        orderRo.setPayTime(date);

        return orderRo;
    }

    /**
     * 构建order
     * @param goodsRo
     * @param siteCode
     * @param deviceCode
     * @param userCode
     * @param userName
     * @return
     */
    private KissOrderRo buildWechatKissOrder(KissGoodsRo goodsRo , String siteCode ,
                                       String deviceCode , String deviceSerialNumber, String userCode , String userName){
        KissOrderRo orderRo = new KissOrderRo();
        Date date = new Date();
        //订单号码
        orderRo.setOrderNumber(WaterOrderUtil.getOrderNo());
        //订单创建时间
        orderRo.setOrderTime(date);
        //场所编码
        orderRo.setStieCode(siteCode);
        //设备编码
        orderRo.setDeviceCode(deviceCode);
        //序列号
        orderRo.setDeviceSerialNumber(deviceSerialNumber);
        //支付方式
        orderRo.setPayWay(KissCommonContext.PayWayContext.WECHAT.getCode());
        //支付来源
        orderRo.setPaySource(KissCommonContext.PaySourceContext.PROGRAM.getCode());
        //支付币种
        orderRo.setPayCurrency("CNY");
        //支付商品名称
        orderRo.setPayGoods(goodsRo.getGoodsCode());
        //支付金额
        orderRo.setPayAmount(goodsRo.getGoodsPrice());
        //支付数量
        orderRo.setPayQuantity(goodsRo.getGoodsUnit());
        //支付标题
        orderRo.setPayTitle(goodsRo.getGoodsName());
        //支付描述
        orderRo.setPayDesc(goodsRo.getGoodsDesc());
        //支付备注
        orderRo.setPayRemark(goodsRo.getRemark());
        //支付状态
        orderRo.setPayStatus(KissCommonContext.PayStatusContext.SELLER_CREATE.getCode());
        //买方编号
        orderRo.setPayBuyerCode(userCode);
        //买方名称
        orderRo.setPayBuyerName(userName);

        return orderRo;
    }

    /**
     * 执行微信支付
     * @param payRo
     * @return
     */
    private String excuteWechatPay(KissWechatPayRo payRo) throws  Exception{
        Map<String , String> map = new HashMap<String , String>();
        map.put("appid", payRo.getAppid());
        map.put("body", payRo.getBody());
        map.put("mch_id",payRo.getMch_id());
        map.put("nonce_str", payRo.getNonce_str());
        map.put("sign_type", payRo.getSign_type());
        map.put("notify_url", payRo.getNotify_url());
        map.put("out_trade_no", payRo.getOut_trade_no());
        map.put("spbill_create_ip", payRo.getSpbill_create_ip());
        map.put("total_fee", String.valueOf(payRo.getTotal_fee()));
        map.put("trade_type", payRo.getTrade_type());
        map.put("openid", payRo.getOpenid());

        String reqBodyXml = WaterWxPayUtil.generateSignedXml(map , machsecret , WaterWxPayUtil.MD5);
        int index = reqBodyXml.indexOf(">");
        String reqBodyXmlStr = reqBodyXml.substring(index+2);
        String resonseXml = WaterHttpUtil.httpPost(payurl , reqBodyXml);
        return resonseXml;
    }

    /**
     *
     * 生成预付单签名
     * @param respBean
     * @return
     */
    private String getSign(KissWechatOrderResponseRo respBean){
        String sign = null;
        try{
            Map<String, String> map = new HashMap<String, String>();
            map.put("appId", appid);
            map.put("package", "prepay_id="+respBean.getPrepay_id());
            map.put("nonceStr", respBean.getNonce_str());
            map.put("timeStamp", respBean.getTimestamp());
            map.put("signType", WaterWxPayUtil.MD5);
            sign = WaterWxPayUtil.generateSignature(map, machsecret, WaterWxPayUtil.MD5);
        }catch (Exception e) {
            logger.error("生成预付单签名错误！",e);
        }
        return sign;
    }

    /**
     * 构建返回数据
     * @param code
     * @param msg
     * @return
     */
    private String programNotify(String code , String msg){
        return "<xml><return_code><![CDATA[" + code + "]]></return_code><return_msg><![CDATA[" + msg + "]]></return_msg></xml>";
    }

    /**
     *
     * 获得ip地址
     * @param request
     * @return
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
