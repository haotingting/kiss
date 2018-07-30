/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.service.goods;

import com.zjtachao.fish.kiss.common.bean.ro.KissGoodsRo;
import com.zjtachao.fish.kiss.common.bean.so.KissGoodsSo;

import java.util.List;

/**
 * 商品服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissGoodsService {
    /**
     * 查询所有商品
     * @return
     */
    List<KissGoodsRo> queryAllGoodsList();

    /**
     * 查询商品
     * @param code
     * @return
     */
    public KissGoodsRo queryGoodsByCode(String code);

    /**
     * 查询总数
     * @param so
     * @return
     */
    Long queryGoodsCount(KissGoodsSo so);

    /**
     * 查询列表
     * @param so
     * @return
     */
    List<KissGoodsRo> queryGoodsList(KissGoodsSo so);

}
