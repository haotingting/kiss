/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.promo;

import com.zjtachao.fish.kiss.common.bean.ro.KissPromoRo;
import com.zjtachao.fish.kiss.common.bean.so.KissPromoSo;

import java.util.List;
import java.util.Set;

/**
 * 优惠码管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissPromoService {

    /**
     * 查询单条数据
     * @param code
     * @return
     */
    public KissPromoRo queryPromoByCode(String code);

    /**
     * 通过编码查询数量
     * @param codeSet
     * @return
     */
    Long queryPromoCountByCodes(Set<String> codeSet);

    /**
     * 新增优惠码
     * @param ro
     */
    public void addPromo(KissPromoRo ro);

    /**
     * 删除优惠券
     * @param code
     */
    public void deletePromo(String code);

    /**
     * 查询总数
     * @param so
     * @return
     */
    public Long queryPromoCount(KissPromoSo so);

    /**
     * 查询列表
     * @param so
     * @return
     */
    public List<KissPromoRo> queryPromoList(KissPromoSo so);
}
