/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.kiss.data.controller.captcha;

import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;
import org.springframework.stereotype.Controller;


import com.zjtachao.fish.kiss.common.constant.KissCommonConstant;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Controller
@Path("/captcha")
public class KissCaptcha extends WaterBootBaseController {

    /**
     * 获得验证码
     */
    @GET
    @Path("/get")
    public void captcha(@CookieParam(KissCommonConstant.KISS_COMMON_USER_LOGIN_CAPTCHA_COOKIE_KEY) String sessionid,
                        @HeaderParam("User-Agent")String userAgent,
                        @Context HttpServletRequest request,
                        @Context HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            int width = 100, height = 35;
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            Random random = new Random();
//			g.setColor(getRandColor(200, 250));
            g.setColor(new Color(255, 255, 255));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 30));
//			g.setColor(getRandColor(160, 200));
            g.setColor(new Color(255, 255, 255));
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }
            String sRand = "";
            String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            for (int i = 0; i < 4; i++) {
                String rand = String.valueOf(str.charAt(random.nextInt(62)));
                sRand += rand;
//				g.setColor(new Color(20 + random.nextInt(110), 20 + random
//						.nextInt(110), 20 + random.nextInt(110)));
                g.setColor(new Color(32, 135, 255));
                g.drawString(rand, 20 * i + 6, 28);
            }

            //session.setAttribute("rcdSbtChkCode", sRand);
            logger.info("验证码："+ sRand);
            g.dispose();

            //图片验证码
            String uuid = null;
            if(null == sessionid || "".equals(sessionid)){
                uuid = WaterUUIDUtil.getUUID();
            }else{
                uuid = sessionid;
            }

            //addCookie
            response = addCookie(KissCommonConstant.KISS_COMMON_USER_LOGIN_CAPTCHA_COOKIE_KEY, uuid , -1, response);

            //设置验证码
            waterRedis.set(KissCommonConstant.KISS_COMMON_USER_LOGIN_CAPTCHA_CACHE_PREFIX+uuid, sRand, 60*5);

            ImageIO.write(image, "JPEG", response.getOutputStream());
        }catch(Exception ex){
            logger.error("获取验证码错误!" ,ex);
        }
    }

    /**
     *
     * 获取随机颜色
     * @param fc
     * @param bc
     * @return
     */
    @SuppressWarnings("unused")
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
