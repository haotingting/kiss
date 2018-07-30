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

import com.zjtachao.fish.kiss.common.bean.domain.KissPromo;
import com.zjtachao.fish.kiss.common.bean.so.KissPromoSo;

import java.util.List;

/**
 * 优惠码管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissPromoMapper {

    /**
     * 通过优惠码查询数据
     * @param so
     * @return
     */
    public KissPromo queryPromoByCode(KissPromoSo so);

    /**
     * 通过优惠码查询重复数据
     * @param so
     * @return
     */
    public Long queryPromoCountByCodes(KissPromoSo so);

    /**
     * 查询数量
     * @param so
     * @return
     */
    public Long queryPromoCount(KissPromoSo so);

    /**
     * 查询列表
     * @param so
     */
    public List<KissPromo> queryPromoList(KissPromoSo so);

    /**
     * 添加
     * @param promo
     */
    public void addPromo(KissPromo promo);

    /**
     * 删除
     * @param promo
     */
    public void deletePromo(KissPromo promo);

}
