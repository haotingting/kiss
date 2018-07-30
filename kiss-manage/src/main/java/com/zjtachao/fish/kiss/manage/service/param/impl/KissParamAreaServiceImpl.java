/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.param.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissParamArea;
import com.zjtachao.fish.kiss.common.bean.ro.KissParamAreaRo;
import com.zjtachao.fish.kiss.common.bean.so.KissParamAreaSo;
import com.zjtachao.fish.kiss.manage.mapper.KissParamAreaMapper;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地区服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissParamAreaServiceImpl implements com.zjtachao.fish.kiss.manage.service.param.KissParamAreaService {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 地域操作 **/
    @Autowired
    private KissParamAreaMapper kissParamAreaMapper;

    /**
     * 查询省
     * @return
     */
    public List<KissParamAreaRo> queryAllProvince(){
        KissParamAreaSo so = new KissParamAreaSo();
        List<KissParamArea> list = kissParamAreaMapper.queryAllProvince(so);
        List<KissParamAreaRo> roList = waterDozer.convertList(list , KissParamAreaRo.class);
        return roList;
    }

    /**
     * 通过省查询市
     * @return
     */
    @Override
    public List<KissParamAreaRo> queryCityByProvince(String provinceCode){
        KissParamAreaSo so = new KissParamAreaSo();
        so.setParentCode(provinceCode);
        List<KissParamArea> list = kissParamAreaMapper.queryCityByProvince(so);
        List<KissParamAreaRo> roList = waterDozer.convertList(list , KissParamAreaRo.class);
        return roList;
    }

    /**
     * 通过市查询省
     * @return
     */
    @Override
    public List<KissParamAreaRo> queryCountyByCity(String cityCode){
        KissParamAreaSo so = new KissParamAreaSo();
        so.setParentCode(cityCode);
        List<KissParamArea> list = kissParamAreaMapper.queryCountyByCity(so);
        List<KissParamAreaRo> roList = waterDozer.convertList(list , KissParamAreaRo.class);
        return roList;
    }

    /**
     * 查询根据级别查询
     * @param level
     * @return
     */
    public List<KissParamAreaRo> queryAreaByLevel(Integer level){
        KissParamAreaSo so = new KissParamAreaSo();
        so.setAreaLevel(level);
        List<KissParamArea> list = kissParamAreaMapper.queryAreaByLevel(so);
        List<KissParamAreaRo> roList = waterDozer.convertList(list , KissParamAreaRo.class);
        return roList;
    }


}
