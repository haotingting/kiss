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
 * 登录返回
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissLoginResponse implements Serializable {

    /** 登录返回 **/
    private static final long serialVersionUID = 8674062636251133224L;

    private String ack;

    private Integer err;

    private Integer par;

    private Integer par1;

    private Integer par2;

    private Integer par3;

    private Integer par4;

    private Integer par5;

    private Integer par6;

    private Integer par7;

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

    public Integer getPar() {
        return par;
    }

    public void setPar(Integer par) {
        this.par = par;
    }

    public Integer getPar1() {
        return par1;
    }

    public void setPar1(Integer par1) {
        this.par1 = par1;
    }

    public Integer getPar2() {
        return par2;
    }

    public void setPar2(Integer par2) {
        this.par2 = par2;
    }

    public Integer getPar3() {
        return par3;
    }

    public void setPar3(Integer par3) {
        this.par3 = par3;
    }

    public Integer getPar4() {
        return par4;
    }

    public void setPar4(Integer par4) {
        this.par4 = par4;
    }

    public Integer getPar5() {
        return par5;
    }

    public void setPar5(Integer par5) {
        this.par5 = par5;
    }

    public Integer getPar6() {
        return par6;
    }

    public void setPar6(Integer par6) {
        this.par6 = par6;
    }

    public Integer getPar7() {
        return par7;
    }

    public void setPar7(Integer par7) {
        this.par7 = par7;
    }
}
