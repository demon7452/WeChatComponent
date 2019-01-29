package com.xiong.web;

import com.alibaba.fastjson.JSON;
import com.xiong.service.api.CallbackHandlerApi;
import com.xiong.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

@Controller
public class WeChatCallbackController {

    private Logger logger = LoggerFactory.getLogger(WeChatCallbackController.class);

    @Resource
    private CallbackHandlerApi callbackHandlerApi;

    @PostMapping("receive/wechat/callback")
    public void receiveWeChatCallBack(HttpServletRequest request, HttpServletResponse response){

        try {
            // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> callbackMap = XmlUtil.xmlToMap(request.getInputStream());
            logger.info("weChat callback map:{}", JSON.toJSONString(callbackMap));

            String replyMessage = callbackHandlerApi.handle(callbackMap);
            logger.info("reply to weCaht:{}", replyMessage);

            // 向微信响应消息
            PrintWriter out = response.getWriter();
            out.print(replyMessage);
            out.close();

        } catch (Exception e) {
            logger.error("handle weChat callback error!", e);
        }
    }
}
