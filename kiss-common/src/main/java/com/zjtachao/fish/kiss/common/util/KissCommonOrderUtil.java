/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.common.util;

import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import com.zjtachao.fish.water.common.tool.WaterValidateUtil;

import java.util.Date;

/**
 * 订单号码工具类
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissCommonOrderUtil {

    /** 时间参数 **/
    private static final String DATE_FORMAT = "yyyyMM";

    /**
     * 验证订单号码
     * @param orderNumber
     * @param minDate
     * @return
     */
    public static boolean validateOrderNumber(String orderNumber , String minDate){
        boolean flag = false;
        if(null != orderNumber && orderNumber.length() >= 20){
            String tbDate = orderNumber.substring(0,6);
            if(WaterValidateUtil.validateNumber(tbDate)){
                int current = Integer.parseInt(tbDate);
                int minVal = Integer.parseInt(minDate);
                String maxDate = WaterDateUtil.date2Str(new Date() , DATE_FORMAT);
                int maxVal = Integer.parseInt(maxDate);
                if(current >= minVal && current <= maxVal){
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 获取订单编号
     * @param orderNumber
     * @param minDate
     * @return
     */
    public static String getOrderNumberPrefix(String orderNumber , String minDate){
        String result = null;
        boolean flag = validateOrderNumber(orderNumber , minDate);
        if(flag){
            result = orderNumber.substring(0,6);
        }
        return result;
    }

}
