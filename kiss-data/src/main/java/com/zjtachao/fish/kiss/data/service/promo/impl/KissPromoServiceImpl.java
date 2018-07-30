/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.service.promo.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissPromo;
import com.zjtachao.fish.kiss.common.bean.ro.KissPromoRo;
import com.zjtachao.fish.kiss.common.bean.so.KissPromoSo;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.data.mapper.KissPromoMapper;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 兑换码service
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissPromoServiceImpl implements com.zjtachao.fish.kiss.data.service.promo.KissPromoService {

    /** 兑换码mapper **/
    @Autowired
    private KissPromoMapper kissPromoMapper;

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /**
     * 通过编码查询promo
     * @param code
     * @return
     */
    @Override
    public KissPromoRo queryPromoByCode(String code){
        KissPromoSo so = new KissPromoSo();
        so.setPromoCode(code);
        KissPromo promo = kissPromoMapper.queryPromoByCode(so);
        return waterDozer.convert(promo , KissPromoRo.class);
    }

    /**
     * 修改状态
     * @param promoCode
     * @param userCode
     */
    @Override
    public void updatePromoStatus(String promoCode, String userCode){
        KissPromo promo = new KissPromo();
        promo.setPromoCode(promoCode);
        promo.setUseCode(userCode);
        promo.setPromoStatus(KissCommonContext.PromoStatusContext.USED.getCode());
        Date date = new Date();
        promo.setModifyTime(date);
        promo.setUseTime(date);
        promo.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissPromoMapper.updatePromoStatus(promo);
    }

}
