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

import com.zjtachao.fish.water.common.tool.WaterMd5Util;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 优惠码生成
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissCommonPromoUtil {

    /**
     * 生成随机编码
     * @param count
     * @return
     */
    public static Set<String> genPromoCode(int count){
        Set<String> codes = new HashSet<String>();
        while(true){
            if(codes.size() != count){
                String uuid = WaterUUIDUtil.getUUID();
                String code = WaterMd5Util.Md516(uuid).toUpperCase();
                codes.add(code);
            }else {
                break;
            }

        }

        return codes;
    }

    public static void main(String [] args){
        Set<String> codes = genPromoCode(100);
        for(String code : codes){
            System.out.println(code);
        }
    }

}
