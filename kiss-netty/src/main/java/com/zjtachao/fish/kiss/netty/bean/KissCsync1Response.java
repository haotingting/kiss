/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.netty.bean;

import java.io.Serializable;

/**
 * csync1设备
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissCsync1Response implements Serializable {


    /** 序列号 **/
    private static final long serialVersionUID = -5068270406648348093L;

    /** cysnc1 **/
    private String ack = "csync1";

    /** 错误 **/
    private Integer err = 0;

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public Integer getErr() {
        return err;
    }

    public void setErr(Integer err) {
        this.err = err;
    }
}
