/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.device.impl;

import com.zjtachao.fish.kiss.common.bean.domain.KissDevice;
import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceRo;
import com.zjtachao.fish.kiss.common.bean.so.KissDeviceSo;
import com.zjtachao.fish.kiss.manage.mapper.KissDeviceMapper;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import com.zjtachao.fish.water.common.data.WaterDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 设备服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Service
public class KissDeviceServiceImpl implements com.zjtachao.fish.kiss.manage.service.device.KissDeviceService {

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
    public List<KissDeviceRo> queryDeviceBySerialNumber(String number){
        KissDeviceSo so = new KissDeviceSo();
        so.setDeviceSerialNumber(number);
        List<KissDevice> deviceList = kissDeviceMapper.queryDeviceList(so);
        List<KissDeviceRo> roList = waterDozer.convertList(deviceList , KissDeviceRo.class);
        return roList;
    }


    /**
     * 查询总数
     * @param so
     * @return
     */
    @Override
    public Long queryDeviceCount(KissDeviceSo so){
        Long count = kissDeviceMapper.queryDeviceCount(so);
        return count;
    }

    /**
     * 查询列表
     * @param so
     * @return
     */
    @Override
    public List<KissDeviceRo> queryDeviceList(KissDeviceSo so){
        List<KissDevice> list = kissDeviceMapper.queryDeviceList(so);
        List<KissDeviceRo> roList = waterDozer.convertList(list , KissDeviceRo.class);
        return roList;
    }

    /**
     * 添加设备
     * @param ro
     */
    @Override
    public void addDevice(KissDeviceRo ro){
        KissDevice device = waterDozer.convert(ro , KissDevice.class);
        Date date = new Date();
        device.setCreateTime(date);
        device.setModifyTime(date);
        device.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode());
        kissDeviceMapper.addDevice(device);
    }

    /**
     * 修改设备
     * @param ro
     */
    @Override
    public void updateDevice(KissDeviceRo ro){
        KissDeviceSo so = new KissDeviceSo();
        so.setDeviceCode(ro.getDeviceCode());
        KissDevice device = this.kissDeviceMapper.queryDeviceByCode(so);
        if(null != device){
            device.setDeviceSerialNumber(ro.getDeviceSerialNumber());
            device.setSiteCode(ro.getSiteCode());
            device.setDeviceName(ro.getDeviceName());
            device.setDeviceMode(ro.getDeviceMode());
            device.setRemark(ro.getRemark());
            Date date = new Date();
            device.setModifyTime(date);
            kissDeviceMapper.updateDevice(device);
        }
    }

    /**
     * 删除设备
     * @param code
     */
    @Override
    public void deleteDevice(String code){
        KissDevice device = new KissDevice();
        device.setDeviceCode(code);
        Date date = new Date();
        device.setModifyTime(date);
        device.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        kissDeviceMapper.deleteDevice(device);
    }

    /**
     * 修改状态
     * @param code
     * @param status
     */
    @Override
    public void updateDeviceStatus(String code, Integer status){
        KissDevice device = new KissDevice();
        device.setDeviceCode(code);
        device.setDeviceStatus(status);
        Date date = new Date();
        device.setModifyTime(date);
        kissDeviceMapper.updateDeviceStatus(device);
    }

}
