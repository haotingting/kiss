/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.controller.goods;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.kiss.common.bean.ro.KissGoodsRo;
import com.zjtachao.fish.kiss.common.bean.so.KissGoodsSo;
import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.data.service.goods.KissGoodsService;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 商品
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/goods")
public class KissGoodsController extends WaterBootBaseController {

    /** 商品服务 **/
    @Autowired
    private KissGoodsService kissGoodsService;


    /**
     * 查询列表
     * @return
     */
    @POST
    @Path("/query/list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<KissGoodsRo> queryList(String json){
        WaterBootResultBean<KissGoodsRo> result = new WaterBootResultBean<KissGoodsRo>();
        try {
            result.setMsg("参数错误");
            if(null != json && !"".equals(json)){
                KissGoodsSo so = JSON.parseObject(json , KissGoodsSo.class);
                if(null != so && null != so.getSiteCode() && !"".equals(so.getSiteCode())){
                    //设置状态
                    so.setGoodsStatus( KissCommonContext.GoodsContext.ONLINE.getCode());
                    //查询数量
                    Long count = kissGoodsService.queryGoodsCount(so);
                    so.setDataCount(count);
                    List<KissGoodsRo> roList = kissGoodsService.queryGoodsList(so);
                    result.setRst(roList);
                    result.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    result.setMsg("查询成功");
                }
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return result;
    }

}
