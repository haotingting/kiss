/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.service.goods.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissGoods;
import com.zjtachao.fish.kiss.common.bean.ro.KissGoodsRo;
import com.zjtachao.fish.kiss.common.bean.so.KissGoodsSo;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.data.mapper.KissGoodsMapper;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissGoodsServiceimpl implements com.zjtachao.fish.kiss.data.service.goods.KissGoodsService {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 商品管理 **/
    @Autowired
    private KissGoodsMapper kissGoodsMapper;

    /**
     * 查询所有商品
     * @return
     */
    @Override
    public List<KissGoodsRo> queryAllGoodsList(){
        KissGoodsSo so = new KissGoodsSo();
        so.setGoodsStatus(KissCommonContext.GoodsContext.ONLINE.getCode());
        List<KissGoods> list = kissGoodsMapper.queryAllGoodsList(so);
        List<KissGoodsRo> roList = waterDozer.convertList(list , KissGoodsRo.class);
        return roList;
    }


    /**
     * 查询商品
     * @param code
     * @return
     */
    @Override
    public KissGoodsRo queryGoodsByCode(String code){
        KissGoodsSo so = new KissGoodsSo();
        so.setGoodsCode(code);
        KissGoods goods = kissGoodsMapper.queryGoodsByCode(so);
        KissGoodsRo ro = waterDozer.convert(goods , KissGoodsRo.class);
        return ro;
    }

    /**
     * 查询总数
     * @param so
     * @return
     */
    public Long queryGoodsCount(KissGoodsSo so){
        Long count = kissGoodsMapper.queryGoodsCount(so);
        return count;
    }

    /**
     * 查询列表
     * @param so
     * @return
     */
    public List<KissGoodsRo> queryGoodsList(KissGoodsSo so){
        List<KissGoods> list = kissGoodsMapper.queryGoodsList(so);
        List<KissGoodsRo> roList = waterDozer.convertList(list , KissGoodsRo.class);
        return roList;
    }

}
