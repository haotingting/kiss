/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.manage.service.user;

import com.zjtachao.fish.kiss.common.bean.ro.KissUserRoleRelRo;

/**
 * 用户角色
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public interface KissUserRoleRelService {
    /**
     * 添加管理角色
     * @param code
     * @param role
     */
    void add(String code, String role);

    /**
     * 取消角色
     * @param code
     * @param role
     */
    void delete(String code, String role);

    /**
     * 通过ro查询
     * @param code
     * @param role
     * @return
     */
    public KissUserRoleRelRo findByCode(String code , String  role);

    /**
     * 取消角色
     * @param code
     */
    public void deleteByUserCode(String code);
}
