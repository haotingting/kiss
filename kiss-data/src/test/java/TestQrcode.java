/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 请在此处添加注释
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class TestQrcode {

    /**
     * 通过Url地址或者图片解码
     *
     * @param url
     * @return
     */
    public static Map<String, Object> decode(String url) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        InputStream inputStream = null;
        if (StringUtils.isNotBlank(url)) {
            try {
                URL exurl = new URL(url);
                HttpURLConnection httpConn = (HttpURLConnection) exurl
                        .openConnection();
                httpConn.connect();
                inputStream = httpConn.getInputStream();
            } catch (Exception e) {
                resultMap.put("status", 500);
                resultMap.put("message", "解码失败，Url地址有误没有获取到文件");
                return resultMap;
            }
        }
        return resultMap;
    }


    public static void main(String [] args){
        TestQrcode.decode("http://192.168.1.18/kiss/qrimg/123456.jpg");

    }

}
