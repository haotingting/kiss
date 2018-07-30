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
 * 场所基础枚举
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissCommonContext {

    /**
     *
     * 设备状态
     * @author <a href="mailto:dh@zjtachao.com">duhao</a>
     * @version $Id$
     * @since 2.0
     */
    public enum DeviceContext{
        /** 未使用**/
        UNUSED(1 ,"未投放"),
        /** 已投放 **/
        ONLINE(2 ,"已投放"),
        /** 已下线**/
        OFFLINE(3 ,"已下线"),
        /** 故障 **/
        HITCH(4 ,"故障"),
        /** 维护 **/
        MAINTENANCE(5 ,"维护"),
        /** 使用中 **/
        WORKING(6 ,"使用中");


        /** 编码 **/
        private int code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private DeviceContext(int code, String name) {
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
            for (DeviceContext c : DeviceContext.values()) {
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

    /**
     *
     * 商品状态
     * @author <a href="mailto:dh@zjtachao.com">duhao</a>
     * @version $Id$
     * @since 2.0
     */
    public enum GoodsContext{
        /** 未发布**/
        UNUSED(0 ,"未发布"),
        /** 已发布 **/
        ONLINE(1 ,"已发布"),
        /** 已下线**/
        OFFLINE(2 ,"已下线");


        /** 编码 **/
        private int code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private GoodsContext(int code, String name) {
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
            for (GoodsContext c : GoodsContext.values()) {
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

    /**
     *
     * 登录方式
     * @author <a href="mailto:dh@zjtachao.com">duhao</a>
     * @version $Id$
     * @since 2.0
     */
    public enum LoginTypeContext{
        /** 微信小程序 **/
        PROGRAM("program" ,"微信小程序"),
        /** 手机号 **/
        MOBILE("mobile" ,"手机号"),
        /** 短信验证码**/
        SMS("sms" ,"短信验证码");


        /** 编码 **/
        private String code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private LoginTypeContext(String code, String name) {
            this.name = name;
            this.code = code;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(String code) {
            String name = null;
            for (LoginTypeContext c : LoginTypeContext.values()) {
                if (c.code.equals(code)) {
                    name = c.getName();
                }
            }
            return name;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     *
     * 用户状态
     * @author <a href="mailto:dh@zjtachao.com">duhao</a>
     * @version $Id$
     * @since 2.0
     */
    public enum UserStatusContext{
        /** 未激活*/
        INACTIVE(0 ,"未激活"),
        /** 正常 **/
        NORMAL(1 ,"正常"),
        /** 禁用**/
        FORBIDDEN(2 ,"禁用");


        /** 编码 **/
        private int code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private UserStatusContext(int code, String name) {
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
            for (UserStatusContext c : UserStatusContext.values()) {
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


    /**
     *
     * 支付方式
     * @author <a href="mailto:dh@zjtachao.com">duhao</a>
     * @version $Id$
     * @since 2.0
     */
    public enum PayWayContext{
        /** 支付宝 **/
        ALIPAY("A" ,"支付宝"),
        /** 微信支付 **/
        WECHAT("W" ,"微信支付"),
        /** 兑换码 **/
        PROMO("P" ,"兑换码");


        /** 编码 **/
        private String code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private PayWayContext(String code, String name) {
            this.name = name;
            this.code = code;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(String code) {
            String name = null;
            for (PayWayContext c : PayWayContext.values()) {
                if (c.code.equals(code)) {
                    name = c.getName();
                }
            }
            return name;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }


    /**
     *
     * 支付来源
     * @author <a href="mailto:dh@zjtachao.com">duhao</a>
     * @version $Id$
     * @since 2.0
     */
    public enum PaySourceContext{
        /** 支付宝 **/
        H5("H5" ,"H5"),
        /** 微信支付 **/
        APP("APP" ,"APP"),
        /** WEB **/
        WEB("WEB" ,"WEB"),
        /** 小程序 **/
        PROGRAM("PROGRAM" ,"小程序");


        /** 编码 **/
        private String code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private PaySourceContext(String code, String name) {
            this.name = name;
            this.code = code;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(String code) {
            String name = null;
            for (PaySourceContext c : PaySourceContext.values()) {
                if (c.code.equals(code)) {
                    name = c.getName();
                }
            }
            return name;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     *
     * 支付状态枚举
     * @author <a href="mailto:zgf@zjtachao.com">zhuguofeng</a>
     * @version $Id$
     * @since 2.0
     */
    public enum PayStatusContext{

        /** 商户服务端交易创建 ,等待api应答 */
        SELLER_CREATE(0,"SELLER_CREATE"),
        /** api调用成功(支付订单创建成功) */
        INVOKE_SUCCESS(1,"INVOKE_SUCCESS"),
        /** api调用失败(支付订单创建失败) */
        INVOKE_FAILED(2,"INVOKE_FAILED"),
        /** api交易创建，等待付款 **/
        WAIT_BUYER_PAY(3 ,"WAIT_BUYER_PAY"),
        /** api未付款超时关闭或全额退款 **/
        TRADE_CLOSED(4 ,"TRADE_CLOSED"),
        /** api交易支付成功  **/
        TRADE_SUCCESS(5 ,"TRADE_SUCCESS"),
        /** api交易结束，不可退款  **/
        TRADE_FINISHED(6 ,"TRADE_FINISHED"),
        /** 交易失败 */
        TRADE_FAIL(7 ,"TRADE_FAIL");



        /** 编码 **/
        private int code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private PayStatusContext(int code, String name) {
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
            for (PayStatusContext c : PayStatusContext.values()) {
                if (c.code == code) {
                    name = c.getName();
                }
            }
            return name;
        }

        /**
         *
         * 根据名称获得编码
         * @return String 名称
         */
        public static int getNumCode(String name) {
            Integer code = null;
            for (PayStatusContext c : PayStatusContext.values()) {
                if (c.getName().equals(name)) {
                    code = c.code;
                    break;
                }
            }
            return code;
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

    /**
     *
     * 优惠码状态
     * @author <a href="mailto:dh@zjtachao.com">duhao</a>
     * @version $Id$
     * @since 2.0
     */
    public enum PromoStatusContext{
        /** 未使用*/
        NORMAL(0 ,"未使用"),
        /** 已使用 **/
        USED(1 ,"已使用"),
        /** 已禁用 **/
        FORBIDDEN(2 ,"已禁用");


        /** 编码 **/
        private int code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private PromoStatusContext(int code, String name) {
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
            for (PromoStatusContext c : PromoStatusContext.values()) {
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


    /**
     *
     * 角色枚举
     * @author <a href="mailto:dh@zjtachao.com">duhao</a>
     * @version $Id$
     * @since 2.0
     */
    public enum RoleTypeContext{
        /** 超级管理员 **/
        SUPER_ADMIN("super_admin" ,"超级管理员" , 10),
        /** 普通管理员 **/
        GENERAL_ADMIN("general_admin" ,"普通管理员" , 9),
        /** 代理商 **/
        AGENT_ADMIN("agent_admin" ,"代理商" , 8),
        /** 场所管理员 **/
        SITE_ADMIN("site_admin" ,"场所管理员" , 7);


        /** 编码 **/
        private String code;
        /** 名称 **/
        private String name;
        /** 级别 **/
        private int level;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private RoleTypeContext(String code, String name , int level) {
            this.name = name;
            this.code = code;
            this.level = level;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(String code) {
            String name = null;
            for (RoleTypeContext c : RoleTypeContext.values()) {
                if (c.code.equals(code)) {
                    name = c.getName();
                    break;
                }
            }
            return name;
        }

        /**
         *
         * 获得级别
         * @param code code
         * @return String 名称
         */
        public static int getLevel(String code) {
            int level = 0;
            for (RoleTypeContext c : RoleTypeContext.values()) {
                if (c.code.equals(code)) {
                    level = c.getLevel();
                    break;
                }
            }
            return level;
        }


        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getLevel(){
            return level;
        }

    }


}
