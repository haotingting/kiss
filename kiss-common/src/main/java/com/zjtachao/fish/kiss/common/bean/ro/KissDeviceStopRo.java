/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.bean.ro;

import com.zjtachao.fish.water.common.base.bean.WaterBootBaseRo;

/**
 * 共享按摩椅设备关闭
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissDeviceStopRo extends WaterBootBaseRo {

    /** 序列号 **/
    private static final long serialVersionUID = -7161947656504711647L;

    /** 设备开启 **/
    private String cmd = "dc2";

    /** 设备编号 **/
    private String did;

    /** 消息编号 **/
    private String messageId;

    /** 当前连接对象 **/
    private String channelId;

    /** 错误信息 **/
    private Integer err;

    /** 用户token **/
    private String kissToken;

    public String getKissToken() {
        return kissToken;
    }

    public void setKissToken(String kissToken) {
        this.kissToken = kissToken;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getErr() {
        return err;
    }

    public void setErr(Integer err) {
        this.err = err;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }
}
