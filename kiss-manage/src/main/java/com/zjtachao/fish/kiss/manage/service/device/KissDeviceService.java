/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.device;

import com.zjtachao.fish.kiss.common.bean.ro.KissDeviceRo;
import com.zjtachao.fish.kiss.common.bean.so.KissDeviceSo;

import java.util.List;

/**
 * 设备管理
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissDeviceService {
    /**
     * 查询单条
     * @param code
     * @return
     */
    KissDeviceRo queryDeviceByCode(String code);

    /**
     * 通过序列号查询
     * @param number
     * @return
     */
    List<KissDeviceRo> queryDeviceBySerialNumber(String number);

    /**
     * 查询总数
     * @param so
     * @return
     */
    Long queryDeviceCount(KissDeviceSo so);

    /**
     * 查询列表
     * @param so
     * @return
     */
    List<KissDeviceRo> queryDeviceList(KissDeviceSo so);

    /**
     * 添加设备
     * @param ro
     */
    void addDevice(KissDeviceRo ro);

    /**
     * 修改设备
     * @param ro
     */
    void updateDevice(KissDeviceRo ro);

    /**
     * 删除设备
     * @param code
     */
    void deleteDevice(String code);

    /**
     * 修改状态
     * @param code
     * @param status
     */
    void updateDeviceStatus(String code, Integer status);
}
