/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.context;

/**
 * 通用context
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissNettyContext {

    /**
     *
     * 通用响应context
     * @author <a href="mailto:dh@zjtachao.com">duhao</a>
     * @version $Id$
     * @since 2.0
     */
    public enum CommonResponseContext{
        /** 没有错误 **/
        NORMAL(0 ,"没有错误"),
        /** 该设备没有登记 **/
        NOT_REGISTER(-1 ,"该设备没有登记"),
        /** 登录密码错误 **/
        LOGIN_ERROR(-2 ,"登录密码错误/未登录"),
        /** 缺少参数 **/
        LACK_PARAM(-3 ,"缺少参数"),
        /** 服务器出错 **/
        SERVER_ERROR(-4 ,"服务器出错"),
        /** 字段类型错误 **/
        FILED_ERROR(-5 ,"字段类型错误"),
        /** 无效的JSON格式 **/
        INVALID_JSON(-6 ,"无效的JSON格式"),
        /** 该设备未上线 **/
        OFFLINE(-7 ,"该设备未上线"),
        /** 设备id不匹配 **/
        DEVICE_ID_ERROR(-8 ,"设备id不匹配"),
        /** 通信超时 **/
        CONNECT_TIMEOUT(-100 ,"通信超时"),
        /** 应答数据错误 **/
        RESPONSE_DATA_TIMEOUT(-101 ,"应答数据错误"),
        /** 设备不在线 **/
        DEVICE_OFFLINE(-102 ,"设备不在线"),
        /** 服务器错误 **/
        SERVER_INTERIOR_ERROR(-103 ,"服务器错误");


        /** 编码 **/
        private int code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private CommonResponseContext(int code, String name) {
            this.name = name;
            this.code = code;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(int code) {
            String name = null;
            for (CommonResponseContext c : CommonResponseContext.values()) {
                if (c.code == code) {
                    name = c.getName();
                }
            }
            return name;
        }
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

}
