package com.xiong.wechat.web;

import com.alibaba.fastjson.JSON;
import com.xiong.wechat.api.WeChatCommonApi;
import com.xiong.wechat.lib.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

@Controller
public class WeChatCallbackController {

    private Logger logger = LoggerFactory.getLogger(WeChatCallbackController.class);

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
            logger.info("reply to weCaht:{}", replyMessage);

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
     * @return echostr
     */
    @GetMapping("receive/wechat/callback")
    @ResponseBody
    public String verifyWeChatCallBackUrl(HttpServletRequest request) {
        String echostr = request.getParameter("echostr");
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");

        logger.info("wechat verify request:{}", JSON.toJSONString(request.getParameterMap()));
        return echostr;
    }
}
