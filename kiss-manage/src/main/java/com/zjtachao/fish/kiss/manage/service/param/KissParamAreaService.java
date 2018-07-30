/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.param;

import com.zjtachao.fish.kiss.common.bean.ro.KissParamAreaRo;

import java.util.List;

/**
 * 地区参数服务
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissParamAreaService {

    /**
     * 查询省
     * @return
     */
    List<KissParamAreaRo> queryAllProvince();

    /**
     * 通过省查询市
     * @return
     */
    List<KissParamAreaRo> queryCityByProvince(String provinceCode);

    /**
     * 通过市查询省
     * @return
     */
    List<KissParamAreaRo> queryCountyByCity(String cityCode);

    /**
     * 查询根据级别查询
     * @param level
     * @return
     */
    List<KissParamAreaRo> queryAreaByLevel(Integer level);
}
