/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.bean.so;

import com.zjtachao.fish.water.common.base.bean.WaterBootBaseSo;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;

/**
 * 用户站点所有查询条件
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissUserSiteRelSo extends WaterBootBaseSo {

    /** 查询条件 **/
    private static final long serialVersionUID = -7527129570801179296L;

    /** 用户编码 **/
    private String userCode;

    /** 删除标志 **/
    private String deleteFlag = WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode();


    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
