/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.promo.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissPromo;
import com.zjtachao.fish.kiss.common.bean.ro.KissPromoRo;
import com.zjtachao.fish.kiss.common.bean.so.KissPromoSo;
import com.zjtachao.fish.kiss.common.context.KissCommonContext;
import com.zjtachao.fish.kiss.manage.mapper.KissPromoMapper;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 优惠码服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissPromoServiceImpl implements com.zjtachao.fish.kiss.manage.service.promo.KissPromoService {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 优惠码服务 **/
    @Autowired
    private KissPromoMapper kissPromoMapper;

    /**
     * 查询单条数据
     * @param code
     * @return
     */
    public KissPromoRo queryPromoByCode(String code){
        KissPromoSo so = new KissPromoSo();
        so.setPromoCode(code);
        KissPromo promo = kissPromoMapper.queryPromoByCode(so);
        KissPromoRo ro = waterDozer.convert(promo , KissPromoRo.class);
        return ro;
    }

    /**
     * 通过编码查询数量
     * @param codeSet
     * @return
     */
    @Override
    public Long queryPromoCountByCodes(Set<String> codeSet){
        Long result = null;
        if(null != codeSet && !codeSet.isEmpty()){
            StringBuffer codes = new StringBuffer();
            boolean flag = true;
            for(String code : codeSet){
                if(flag){
                    flag = false;
                }else {
                    codes.append(",");
                }
                codes.append(code);
            }
            KissPromoSo so = new KissPromoSo();
            so.setCodes(codes.toString());
            result = kissPromoMapper.queryPromoCountByCodes(so);
        }
        return result;
    }

    /**
     * 新增优惠码
     * @param ro
     */
    public void addPromo(KissPromoRo ro){
        KissPromo promo = waterDozer.convert(ro , KissPromo.class);
        promo.setPromoStatus(KissCommonContext.PromoStatusContext.NORMAL.getCode());
        Date date = new Date();
        promo.setCreateTime(date);
        promo.setModifyTime(date);
        promo.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissPromoMapper.addPromo(promo);
    }

    /**
     * 删除优惠券
     * @param code
     */
    public void deletePromo(String code){
        KissPromo promo = new KissPromo();
        promo.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        promo.setModifyTime(new Date());
        promo.setPromoCode(code);
        kissPromoMapper.deletePromo(promo);
    }

    /**
     * 查询总数
     * @param so
     * @return
     */
    public Long queryPromoCount(KissPromoSo so){
        Long count = kissPromoMapper.queryPromoCount(so);
        return count;
    }

    /**
     * 查询列表
     * @param so
     * @return
     */
    public List<KissPromoRo> queryPromoList(KissPromoSo so){
        List<KissPromo> list = kissPromoMapper.queryPromoList(so);
        List<KissPromoRo> roList = waterDozer.convertList(list , KissPromoRo.class);
        return roList;
    }

}
