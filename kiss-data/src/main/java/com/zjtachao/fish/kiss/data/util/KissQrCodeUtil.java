/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.util;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;


/**
 * 二维码处理类
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
public class KissQrCodeUtil {

    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(KissQrCodeUtil.class);


    /**
     * 读取二维码
     * @param path
     * @return
     */
    public static String readQrcode(String path) {
        String msg = null;
        try {
            logger.info("开始识别图片："+path);
            MultiFormatReader formatReader = new MultiFormatReader();
            // 二维码图片位置
            File file = new File(path);
            // 读取图片
            BufferedImage image = ImageIO.read(file);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new KissBufferedImageLuminanceSource(image)));
            //定义二维码的参数
            HashMap hints = new HashMap();
            //设置编码字符集
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //处理读取结果
            Result result = formatReader.decode(binaryBitmap, hints);
            msg = result.getText();
        } catch (Exception e) {
            logger.error("读取二维码错误"+e.getMessage() , e);
        }
        return msg;
    }
}
