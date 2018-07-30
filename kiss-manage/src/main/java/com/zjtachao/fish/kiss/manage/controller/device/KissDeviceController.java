/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.controller.device;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceRo;
import com.zjtachao.fish.kiss.common.bean.so.KissDeviceSo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.controller.base.KissManageBaseController;
import com.zjtachao.fish.kiss.manage.service.device.KissDeviceService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.tool.WaterRegexValidateUtil;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 设备管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/device")
public class KissDeviceController extends KissManageBaseController {

    /** 设备服务*/
    @Autowired
    private KissDeviceService kissDeviceService;


    /**
     * 查询单条设备
     * @return
     */
    @GET
    @Path("/query/single/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceRo> querySingle(@PathParam("code") String code,
                                                         @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
        try {
            //查询数据
            KissDeviceRo ro = kissDeviceService.queryDeviceByCode(code);
            if(null != ro && null != ro.getDeviceCode()){
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
    public WaterBootResultBean<KissDeviceRo> queryList(String json,
                                                       @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
        try {
            if(null != json && !"".equals(json)){
                KissDeviceSo so = JSON.parseObject(json , KissDeviceSo.class);
                boolean allowFlag = isAllowHandle(userToken  , KissCommonContext.RoleTypeContext.SITE_ADMIN.getCode(), so.getSiteCode());
                if(allowFlag){
                    //查询数量
                    Long count = kissDeviceService.queryDeviceCount(so);
                    so.setDataCount(count);
                    List<KissDeviceRo> roList = kissDeviceService.queryDeviceList(so);
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
     * 新增设备
     * @return
     */
    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceRo> add(String json,
                                                 @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissDeviceRo ro = null;
            if(null != json && !"".equals(json)){
                ro = JSON.parseObject(json , KissDeviceRo.class);
                if(null != ro){
                    if(flag && (null == ro.getDeviceSerialNumber() || "".equals(ro.getDeviceSerialNumber()))){
                        flag = false;
                        msg = "序列号有误";
                    }
                    if(flag && (!WaterRegexValidateUtil.checkNumericAndLetter(ro.getDeviceSerialNumber()))){
                        flag = false;
                        msg = "序列号只能包含数字和字母";
                    }
                    if(flag && (null == ro.getSiteCode() || "".equals(ro.getSiteCode()))){
                        flag = false;
                        msg = "设备场所有误";
                    }
                    if(flag && (null == ro.getDeviceName() || "".equals(ro.getDeviceName()))){
                        flag = false;
                        msg = "设备名称有误";
                    }
                    if(flag && (null == ro.getDeviceMode() || "".equals(ro.getDeviceMode()))){
                        flag = false;
                        msg = "设备型号有误";
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
                    List<KissDeviceRo> serialRoList = kissDeviceService.queryDeviceBySerialNumber(ro.getDeviceSerialNumber());
                    if(null == serialRoList || serialRoList.isEmpty()){
                        String uuid = WaterUUIDUtil.getUUID();
                        ro.setDeviceCode(uuid);
                        //设备状态
                        ro.setDeviceStatus(KissCommonContext.DeviceContext.UNUSED.getCode());
                        kissDeviceService.addDevice(ro);
                        result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        msg = "新增成功";
                    }else {
                        msg = "该序列号设备已存在";
                    }
                }else {
                    flag = false;
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
     * 修改设备
     * @return
     */
    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceRo> update(String json,
                                                    @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
        try {
            boolean flag = true;
            String msg = null;
            KissDeviceRo ro = null;
            if(null != json && !"".equals(json)){
                ro = JSON.parseObject(json , KissDeviceRo.class);
                if(null != ro){
                    if(flag && (null == ro.getDeviceSerialNumber() || "".equals(ro.getDeviceSerialNumber()))){
                        flag = false;
                        msg = "序列号有误";
                    }
                    if(flag && (!WaterRegexValidateUtil.checkNumericAndLetter(ro.getDeviceSerialNumber()))){
                        flag = false;
                        msg = "序列号只能包含数字和字母";
                    }
                    if(flag && (null == ro.getSiteCode() || "".equals(ro.getSiteCode()))){
                        flag = false;
                        msg = "设备场所有误";
                    }
                    if(flag && (null == ro.getDeviceName() || "".equals(ro.getDeviceName()))){
                        flag = false;
                        msg = "设备名称有误";
                    }
                    if(flag && (null == ro.getDeviceMode() || "".equals(ro.getDeviceMode()))){
                        flag = false;
                        msg = "设备型号有误";
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
                    //查询设备序列号是否允许修改
                    boolean serialFlag = true;
                    List<KissDeviceRo> serialList = kissDeviceService.queryDeviceBySerialNumber(ro.getDeviceSerialNumber());
                    if(null != serialList &&  !serialList.isEmpty()){
                        for(KissDeviceRo serialRo : serialList){
                            if(null != serialRo && null != serialRo.getDeviceCode()
                                    && !ro.getDeviceCode().equals(ro.getDeviceCode())){
                                serialFlag = false;
                                break;
                            }
                        }
                    }
                    if(serialFlag){
                        kissDeviceService.updateDevice(ro);
                        result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        msg = "修改成功";
                    }else {
                        msg = "该设备的序列号已存在";
                    }
                }else {
                    flag = false;
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
     * 删除设备
     * @return
     */
    @GET
    @Path("/delete/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissDeviceRo> delete(@PathParam("code") String code,
                                                    @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
        try {
            //查询数据
            KissDeviceRo ro = kissDeviceService.queryDeviceByCode(code);
            if(null != ro && null != ro.getDeviceCode()){
                //权限
                boolean allow = isAllowEdit(userToken , KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode() , ro.getSiteCode());
                if(allow){
                    kissDeviceService.deleteDevice(code);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("删除成功！");
                }else {
                    result.setMsg("该用户无权限操作");
                }
            }else {
                result.setMsg("未找到需要操作的设备");
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
    public WaterBootResultBean<KissDeviceRo> upateStatus(
            @PathParam("code") String code,
            @PathParam("status") Integer status,
            @CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_COOKIE_KEY)String userToken){
        WaterBootResultBean<KissDeviceRo> result = new WaterBootResultBean<KissDeviceRo>();
        try {
            //查询数据
            KissDeviceRo ro = kissDeviceService.queryDeviceByCode(code);
            if(null != ro && null != ro.getDeviceCode()){
                //权限
                boolean allow = isAllowEdit(userToken , KissCommonContext.RoleTypeContext.AGENT_ADMIN.getCode() , ro.getSiteCode());
                if(allow){
                    kissDeviceService.updateDeviceStatus(code , status);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("修改状态成功！");
                }else {
                    result.setMsg("该用户无权限操作");
                }
            }else {
                result.setMsg("未找到需要操作的设备");
            }

        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }


}
