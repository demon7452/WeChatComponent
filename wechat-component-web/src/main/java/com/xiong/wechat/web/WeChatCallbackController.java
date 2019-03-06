package com.xiong.wechat.web;

import com.alibaba.fastjson.JSON;
import com.xiong.wechat.api.WeChatCommonApi;
import com.xiong.wechat.lib.properties.WeChatProperty;
import com.xiong.wechat.lib.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
public class WeChatCallbackController {

    private Logger logger = LoggerFactory.getLogger(WeChatCallbackController.class);

    @Resource
    private WeChatProperty weChatProperty;

    @Resource
    private WeChatCommonApi weChatCommonApi;

    @PostMapping("receive/wechat/callback")
    public void receiveWeChatCallBack(HttpServletRequest request, HttpServletResponse response) {

        try {
            // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> callbackMap = XmlUtil.xmlToMap(request.getInputStream());
            logger.info("weChat callback map:{}", JSON.toJSONString(callbackMap));

            String replyMessage = weChatCommonApi.handleCallBack(callbackMap);
            logger.info("reply to weChat:{}", replyMessage);

            // 向微信响应消息
            PrintWriter out = response.getWriter();
            out.print(replyMessage);
            out.close();

        } catch (Exception e) {
            logger.error("handle weChat callback error!", e);
        }
    }

    /**
     * 公众号域名绑定
     * @param request 微信请求
     */
    @GetMapping("receive/wechat/callback")
    public void verifyWeChatCallBackUrl(HttpServletRequest request, HttpServletResponse response) {

        logger.info("weChat callback url verify request:{}", JSON.toJSONString(request.getParameterMap()));

//        // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
//        String signature = request.getParameter("signature");
//        // 时间戳
//        String timestamp = request.getParameter("timestamp");
//        // 随机数
//        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        if(StringUtils.isEmpty(echostr)){
            return;
        }

        try {
            response.getWriter().write(echostr);
        } catch (IOException e) {
            logger.error("weChat callback url verify failed", e);
        }
    }
}
