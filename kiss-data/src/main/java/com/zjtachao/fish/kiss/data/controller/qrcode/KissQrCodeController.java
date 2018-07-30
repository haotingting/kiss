/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.controller.qrcode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjtachao.fish.kiss.common.bean.ro.KissQrcodeGenRo;
import com.zjtachao.fish.kiss.common.bean.ro.KissQrcodeRo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.data.util.KissQrCodeUtil;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import com.zjtachao.fish.water.common.tool.WaterHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 微信小程序二维码生成
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/qrcode")
public class KissQrCodeController extends WaterBootBaseController {

    /** appid **/
    @Value("${com.zjtachao.fish.kiss.data.program.appid}")
    private String appid;

    /** appsecret **/
    @Value("${com.zjtachao.fish.kiss.data.program.appsecret}")
    private String appsecret;

    /** tokenurl **/
    @Value("${com.zjtachao.fish.kiss.data.program.tokenurl}")
    private String tokenurl;

    /** qrcode-wx **/
    @Value("${com.zjtachao.fish.kiss.data.program.qrcode-wx}")
    private String qrcodeWx;

    /** qrcode-normal **/
    @Value("${com.zjtachao.fish.kiss.data.program.qrcode-normal}")
    private String qrcodeNormal;

    /** qrcode-path **/
    @Value("${com.zjtachao.fish.kiss.data.program.qrcode-path}")
    private String qrcodePath;

    /** qrcode-width **/
    @Value("${com.zjtachao.fish.kiss.data.program.qrcode-width}")
    private String qrcodeWidth;

    /** qrlocalpath **/
    @Value("${com.zjtachao.fish.kiss.data.program.qrlocalpath}")
    private String qrlocalpath;

    /** qrrelativeurl **/
    @Value("${com.zjtachao.fish.kiss.data.program.qrrelativeurl}")
    private String qrrelativeurl;

    /**
     * 获得小程序码
     * @return
     */
    @GET
    @Path("/program/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissQrcodeRo> program(@PathParam("code") String code){
        WaterBootResultBean<KissQrcodeRo> result = new WaterBootResultBean<KissQrcodeRo>();
        try {
            //获取token
            String token = getToken();
            if(null != token && !"".equals(token)){
                //获得二维码
                KissQrcodeGenRo genRo = new KissQrcodeGenRo();
                String path = qrcodePath.replaceAll("#CODE#" , code);
                genRo.setPath(path);
                genRo.setWidth(Integer.parseInt(qrcodeWidth));
                String url = qrcodeWx.replaceAll("#ACCESS_TOKEN#" , token);
                String localPath = qrlocalpath+"/"+code+".jpg";
                logger.info("生成二维码："+JSON.toJSONString(genRo));
                boolean flag = WaterHttpUtil.httpPostForImg(url , JSON.toJSONString(genRo) , localPath);
                if(flag){
                    //String qrText = KissQrCodeUtil.readQrcode(localPath);
                    String qrurl = qrrelativeurl +"/"+code+".jpg";
                    KissQrcodeRo resultRo = new KissQrcodeRo();
                    resultRo.setQrcodePath(qrurl);
                    //resultRo.setQrcodeText(qrText);
                    resultRo.setDeviceSerialNumber(code);
                    result.setRst(resultRo);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("查询成功");
                }else {
                    result.setMsg("未获取到图片");
                }

            }else {
                result.setMsg("查询失败");
            }

        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 获得小程序码
     * @return
     */
    @GET
    @Path("/normal/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissQrcodeRo> normal(@PathParam("code") String code){
        WaterBootResultBean<KissQrcodeRo> result = new WaterBootResultBean<KissQrcodeRo>();
        try {
            //获取token
            String token = getToken();
            if(null != token && !"".equals(token)){
                //获得二维码
                KissQrcodeGenRo genRo = new KissQrcodeGenRo();
                String path = qrcodePath.replaceAll("#CODE#" , code);
                genRo.setPath(path);
                genRo.setWidth(Integer.parseInt(qrcodeWidth));
                String url = qrcodeNormal.replaceAll("#ACCESS_TOKEN#" , token);
                String localPath = qrlocalpath+"/"+code+".jpg";
                logger.info("生成二维码："+JSON.toJSONString(genRo));
                boolean flag = WaterHttpUtil.httpPostForImg(url , JSON.toJSONString(genRo) , localPath);
                if(flag){
                    String qrText = KissQrCodeUtil.readQrcode(localPath);
                    String qrurl = qrrelativeurl +"/"+code+".jpg";
                    KissQrcodeRo resultRo = new KissQrcodeRo();
                    resultRo.setQrcodePath(qrurl);
                    resultRo.setQrcodeText(qrText);
                    resultRo.setDeviceSerialNumber(code);
                    result.setRst(resultRo);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("查询成功");
                }else {
                    result.setMsg("未获取到图片");
                }
            }else {
                result.setMsg("查询失败");
            }

        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 获取token
     * @return
     */
    private String getToken(){
        String token  = waterRedis.get(KissCommonConstant.KISS_COMMON_APP_TOKEN);
        if(null == token || "".equals(token)){
            //调用微信获得token
            String url = tokenurl.replaceAll("#APPID#" , appid).replaceAll("#APPSECRET#" , appsecret);
            String tokenJson = WaterHttpUtil.httpGet(url);
            JSONObject jsonObj = JSON.parseObject(tokenJson);
            if(null != jsonObj && jsonObj.containsKey("access_token")){
                token = jsonObj.getString("access_token");
                waterRedis.set(KissCommonConstant.KISS_COMMON_APP_TOKEN , token , 5000);
            }else {
                logger.warn("获取token失败："+tokenJson);
            }
        }
        return token;
    }

}
