/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.service.device.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissDevice;
import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceRo;
import com.zjtachao.fish.kiss.common.bean.so.KissDeviceSo;
import com.zjtachao.fish.kiss.data.mapper.KissDeviceMapper;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 查询设备状态
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissDeviceServiceImpl implements com.zjtachao.fish.kiss.data.service.device.KissDeviceService {

    /** 对象转换 **/
    @Autowired
    private WaterDozer waterDozer;

    /** 设备mapper **/
    @Autowired
    private KissDeviceMapper kissDeviceMapper;

    /**
     * 查询单条
     * @param code
     * @return
     */
    @Override
    public KissDeviceRo queryDeviceByCode(String code){
        KissDeviceSo so = new KissDeviceSo();
        so.setDeviceCode(code);
        KissDevice device = kissDeviceMapper.queryDeviceByCode(so);
        KissDeviceRo ro = waterDozer.convert(device , KissDeviceRo.class);
        return ro;
    }

    /**
     * 通过序列号查询
     * @param number
     * @return
     */
    @Override
    public KissDeviceRo queryDeviceBySerialNumber(String number){
        KissDeviceSo so = new KissDeviceSo();
        so.setDeviceSerialNumber(number);
        KissDevice device = kissDeviceMapper.queryDeviceBySerialNumber(so);
        KissDeviceRo ro = waterDozer.convert(device , KissDeviceRo.class);
        return ro;
    }

}
