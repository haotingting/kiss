/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.mapper;

import com.zjtachao.fish.kiss.common.bean.domain.KissGoods;
import com.zjtachao.fish.kiss.common.bean.so.KissGoodsSo;

import java.util.List;

/**
 * 商品管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissGoodsMapper {

    /**
     * 查询单条产品
     * @param so
     * @return
     */
    public KissGoods queryGoodsByCode(KissGoodsSo so);

    /**
     * 查询所有商品
     * @param so
     * @return
     */
    public List<KissGoods> queryAllGoodsList(KissGoodsSo so);

    /**
     * 查询总数
     * @param so
     * @return
     */
    public Long queryGoodsCount(KissGoodsSo so);

    /**
     * 查询列表
     * @param so
     * @return
     */
    public List<KissGoods> queryGoodsList(KissGoodsSo so);

    /**
     * 添加产品
     * @param goods
     */
    public void addGoods(KissGoods goods);

    /**
     * 修改产品
     * @param goods
     */
    public void updateGoods(KissGoods goods);

    /**
     * 删除产品
     * @param goods
     */
    public void deleteGoods(KissGoods goods);

    /**
     * 修改产品状态
     * @param goods
     */
    public void updateGoodsStatus(KissGoods goods);

}
