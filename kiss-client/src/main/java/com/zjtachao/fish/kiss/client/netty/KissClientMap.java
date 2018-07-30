/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.client.netty;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请在此处添加注释
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissClientMap {

    /** map对象 **/
    private static Map<String,Channel> map = new ConcurrentHashMap<String, Channel>();


    public static void add(String clientId,Channel channel){
        map.put(clientId,channel);
    }
    public static Channel get(String clientId){
        return map.get(clientId);
    }

    /**
     * 获得所有
     * @return
     */
    public static List<Channel> getAll(){
        List<Channel> list = new ArrayList<>(map.values());
        return list;
    }

    public static void remove(String cliendId){
        map.remove(cliendId);
    }

    public static int size(){
        return map.size();
    }

    public static boolean contain(String clientId){
        return map.containsKey(clientId);
    }
}
