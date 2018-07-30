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

import io.netty.channel.Channel;

import java.io.Serializable;

/**
 * 渠道对象
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissChannelBean implements Serializable {

    /** 设备序列号 **/
    private String did;

    /** 渠道 **/
    private Channel channel;

    /**
     * 构造方法
     * @param channel
     */
    public KissChannelBean(Channel channel){
        this.channel = channel;
    }

    /**
     * 构造方法
     * @param did
     * @param channel
     */
    public KissChannelBean(String did , Channel channel){
        this.did = did;
        this.channel = channel;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
