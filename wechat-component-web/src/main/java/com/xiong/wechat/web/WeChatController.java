package com.xiong.wechat.web;

import com.alibaba.fastjson.JSON;
import com.xiong.wechat.api.WeChatCommonApi;
import com.xiong.wechat.api.dto.TemplateMessageDto;
import com.xiong.wechat.api.dto.WeChatUserDto;
import com.xiong.wechat.lib.constants.WeChatConstant;
import com.xiong.wechat.lib.properties.WeChatProperty;
import com.xiong.wechat.lib.util.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;

import static com.xiong.wechat.lib.enums.WeChatErrorEnum.ERROR_TRANSFER;

@Controller
@RequestMapping("wechat")
public class WeChatController{

    private static Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private WeChatProperty weChatProperty;

    @Resource
    private AccessToken accessToken;

    @Resource
    private WeChatCommonApi weChatCommonApi;

    @GetMapping("user/{openId}")
    @ResponseBody
    public WeChatUserDto getWeChatUserInfo(@PathVariable("openId") String openId){

        return weChatCommonApi.getWeChatUserInfo(openId);
    }

    @PostMapping(value = "send/message")
    @ResponseBody
    public String send(@RequestBody TemplateMessageDto templateMessageDto) {

        ResponseEntity<String> responseEntity = sendTemplateMessage(JSON.toJSONString(templateMessageDto));

        logger.info("send message result:{}", responseEntity.toString());

        if(HttpStatus.OK != responseEntity.getStatusCode()){
            logger.error(ERROR_TRANSFER.getDesc());
            return ERROR_TRANSFER.getDesc();
        }

        return "success";
    }


    private ResponseEntity<String> sendTemplateMessage(String message) {

        logger.info("template message:{}", message);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getTemplateMessageUrl())
                .queryParam(WeChatConstant.PARAM_ACCESS_TOKEN, accessToken.getValue());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(message, headers);

        return restTemplate.postForEntity(builder.build().toUri(), entity, String.class);
    }
}
