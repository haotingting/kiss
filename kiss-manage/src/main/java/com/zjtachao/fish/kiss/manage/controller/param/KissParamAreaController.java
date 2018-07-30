/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.controller.param;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissParamAreaRo;
import com.zjtachao.fish.kiss.manage.service.param.KissParamAreaService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 地区参数管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/param/area")
public class KissParamAreaController extends WaterBootBaseController {

    /** 地区参数表 **/
    @Autowired
    private KissParamAreaService kissParamAreaService;

    /** 省的编码 **/
    @Value("${com.zjtachao.fish.kiss.manage.redis.param.area.province}")
    private String paramAreaProvince;

    /** 市的编码 **/
    @Value("${com.zjtachao.fish.kiss.manage.redis.param.area.city}")
    private String paramAreaCity;

    /** 区的编码 **/
    @Value("${com.zjtachao.fish.kiss.manage.redis.param.area.county}")
    private String paramAreaCounty;

    /** 所有城市 **/
    @Value("${com.zjtachao.fish.kiss.manage.redis.param.area.city-all}")
    private String paramAreaCityAll;

    /** 所有区 **/
    @Value("${com.zjtachao.fish.kiss.manage.redis.param.area.county-all}")
    private String paramAreaCountyAll;


    /**
     * 查询省
     * @return
     */
    @GET
    @Path("/query/province")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissParamAreaRo> queryProvince(){
        WaterBootResultBean<KissParamAreaRo> result = new WaterBootResultBean<KissParamAreaRo>();
        try {
            List<KissParamAreaRo> roList = null;
            String json = waterRedis.get(paramAreaProvince);
            if(null != json && !"".equals(json)){
                roList = JSON.parseArray(json ,KissParamAreaRo.class);
            }else{
                roList = kissParamAreaService.queryAllProvince();
                waterRedis.set(paramAreaProvince , JSON.toJSONString(roList));
            }
            result.setRst(roList);
            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
            result.setMsg("查询成功");
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 查询所有市
     * @return
     */
    @GET
    @Path("/query/all/city")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissParamAreaRo> queryAllCity(){
        WaterBootResultBean<KissParamAreaRo> result = new WaterBootResultBean<KissParamAreaRo>();
        try {
            List<KissParamAreaRo> roList = null;
            String json = waterRedis.get(paramAreaCityAll);
            if(null != json && !"".equals(json)){
                roList = JSON.parseArray(json ,KissParamAreaRo.class);
            }else{
                roList = kissParamAreaService.queryAreaByLevel(2);
                waterRedis.set(paramAreaCityAll , JSON.toJSONString(roList));
            }
            result.setRst(roList);
            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
            result.setMsg("查询成功");
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 查询市
     * @return
     */
    @GET
    @Path("/query/city/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissParamAreaRo> queryCity(@PathParam("code") String code){
        WaterBootResultBean<KissParamAreaRo> result = new WaterBootResultBean<KissParamAreaRo>();
        try {
            List<KissParamAreaRo> roList = null;
            String json = waterRedis.get(paramAreaCity+code);
            if(null != json && !"".equals(json)){
                roList = JSON.parseArray(json ,KissParamAreaRo.class);
            }else{
                roList = kissParamAreaService.queryCityByProvince(code);
                waterRedis.set(paramAreaCity+code , JSON.toJSONString(roList));
            }
            result.setRst(roList);
            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
            result.setMsg("查询成功");
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 查询所有区
     * @return
     */
    @GET
    @Path("/query/all/county")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissParamAreaRo> queryAllCounty(){
        WaterBootResultBean<KissParamAreaRo> result = new WaterBootResultBean<KissParamAreaRo>();
        try {
            List<KissParamAreaRo> roList = null;
            String json = waterRedis.get(paramAreaCountyAll);
            if(null != json && !"".equals(json)){
                roList = JSON.parseArray(json ,KissParamAreaRo.class);
            }else{
                roList = kissParamAreaService.queryAreaByLevel(3);
                waterRedis.set(paramAreaCountyAll , JSON.toJSONString(roList));
            }
            result.setRst(roList);
            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
            result.setMsg("查询成功");
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

    /**
     * 查询区
     * @return
     */
    @GET
    @Path("/query/county/{code}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissParamAreaRo> queryCounty(@PathParam("code") String code){
        WaterBootResultBean<KissParamAreaRo> result = new WaterBootResultBean<KissParamAreaRo>();
        try {
            List<KissParamAreaRo> roList = null;
            String json = waterRedis.get(paramAreaCounty+code);
            if(null != json && !"".equals(json)){
                roList = JSON.parseArray(json ,KissParamAreaRo.class);
            }else{
                roList = roList = kissParamAreaService.queryCountyByCity(code);
                waterRedis.set(paramAreaCounty+code , JSON.toJSONString(roList));
            }
            result.setRst(roList);
            result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
            result.setMsg("查询成功");
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

}
