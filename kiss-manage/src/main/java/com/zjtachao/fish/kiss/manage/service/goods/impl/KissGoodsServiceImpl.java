/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.goods.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissGoods;
import com.zjtachao.fish.kiss.common.bean.ro.KissGoodsRo;
import com.zjtachao.fish.kiss.common.bean.so.KissGoodsSo;
import com.zjtachao.fish.kiss.manage.mapper.KissGoodsMapper;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissGoodsServiceImpl implements com.zjtachao.fish.kiss.manage.service.goods.KissGoodsService {



    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 商品管理 **/
    @Autowired
    private KissGoodsMapper kissGoodsMapper;

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
     * 查询所有商品
     * @return
     */
    @Override
    public List<KissGoodsRo> queryAllGoodsList(){
        KissGoodsSo so = new KissGoodsSo();
        List<KissGoods> list = kissGoodsMapper.queryAllGoodsList(so);
        List<KissGoodsRo> roList = waterDozer.convertList(list , KissGoodsRo.class);
        return roList;
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

    /**
     * 添加产品
     * @param ro
     */
    @Override
    public void addGoods(KissGoodsRo ro){
        KissGoods goods = waterDozer.convert(ro , KissGoods.class);
        Date date = new Date();
        goods.setCreateTime(date);
        goods.setModifyTime(date);
        goods.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissGoodsMapper.addGoods(goods);
    }

    /**
     * 修改产品
     * @param ro
     */
    @Override
    public void updateGoods(KissGoodsRo ro){
        KissGoodsSo so = new KissGoodsSo();
        so.setGoodsCode(ro.getGoodsCode());
        KissGoods goods = kissGoodsMapper.queryGoodsByCode(so);
        if(null != goods){
            goods.setSiteCode(ro.getSiteCode());
            goods.setGoodsType(ro.getGoodsType());
            goods.setGoodsName(ro.getGoodsName());
            goods.setGoodsPrice(ro.getGoodsPrice());
            goods.setGoodsUnit(ro.getGoodsUnit());
            goods.setGoodsDesc(ro.getGoodsDesc());
            goods.setGoodsOrder(ro.getGoodsOrder());
            goods.setRemark(ro.getRemark());
            Date date = new Date();
            goods.setModifyTime(date);
            kissGoodsMapper.updateGoods(goods);
        }
    }

    /**
     * 删除产品
     * @param code
     */
    @Override
    public void deleteGoods(String code){
        KissGoods goods = new KissGoods();
        goods.setGoodsCode(code);
        Date date = new Date();
        goods.setModifyTime(date);
        goods.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        kissGoodsMapper.deleteGoods(goods);
    }

    /**
     * 修改产品状态
     * @param code
     * @param status
     */
    @Override
    public void updateGoodsStatus(String code, Integer status){
        KissGoods goods = new KissGoods();
        goods.setGoodsCode(code);
        goods.setGoodsStatus(status);
        goods.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        Date date = new Date();
        goods.setModifyTime(date);
        kissGoodsMapper.updateGoodsStatus(goods);
    }


}
