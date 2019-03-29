package com.xiong.wechat.web;

import com.alibaba.fastjson.JSON;
import com.xiong.wechat.api.WeChatCommonApi;
import com.xiong.wechat.api.dto.AuthAccessTokenDto;
import com.xiong.wechat.api.dto.CustomMenuCreateDto;
import com.xiong.wechat.api.dto.WeChatUserDto;
import com.xiong.wechat.lib.constants.WeChatConstant;
import com.xiong.wechat.lib.properties.WeChatAccountProperties;
import com.xiong.wechat.lib.properties.WeChatProperty;
import com.xiong.wechat.lib.util.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("wechat/custom/menu")
public class CustomMenuController {

    private Logger logger = LoggerFactory.getLogger(CustomMenuController.class);

    private static final String BASE_SCOPE = "snsapi_base";

    private static final String AUTH_REDIRECT_URL = "http://pzjqbh.natappfree.cc/wechat/custom/menu/auth";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private AccessToken accessToken;

    @Resource
    private WeChatProperty weChatProperty;

    @Resource
    private WeChatAccountProperties weChatAccountProperties;

    @Resource
    private WeChatCommonApi weChatCommonApi;

    @PostMapping("create")
    public ResponseEntity<String> createMenu(@RequestBody CustomMenuCreateDto customMenuCreateDto){
        logger.info("custom menu create request:{}", JSON.toJSONString(customMenuCreateDto));

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getCustomMenuCreateUrl())
                .queryParam(WeChatConstant.PARAM_ACCESS_TOKEN, accessToken.getValue());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(customMenuCreateDto), headers);

        return restTemplate.postForEntity(builder.build().toUri(), entity, String.class);
    }

    @GetMapping("get")
    public ResponseEntity<String> getMenu(){

        StringHttpMessageConverter messageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        RestTemplate localRest = new RestTemplateBuilder().additionalMessageConverters(messageConverter).build();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getCustomMenuGetUrl())
                .queryParam(WeChatConstant.PARAM_ACCESS_TOKEN, accessToken.getValue());

        return localRest.getForEntity(builder.build().toUri(), String.class);
    }

    @GetMapping("delete")
    public ResponseEntity<String> deleteMenu(){

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getCustomMenuDeleteUrl())
                .queryParam(WeChatConstant.PARAM_ACCESS_TOKEN, accessToken.getValue());

        return restTemplate.getForEntity(builder.build().toUri(), String.class);
    }

    @GetMapping("auth")
    public ModelAndView auth(HttpServletRequest request){
        logger.info("weChat auth request parameterMap:{}", JSON.toJSONString(request.getParameterMap()));

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getAuthAccessTokenUrl())
                .queryParam(WeChatConstant.PARAM_APPID, weChatAccountProperties.getAppId())
                .queryParam(WeChatConstant.PARAM_SECRET, weChatAccountProperties.getAppSecret())
                .queryParam(WeChatConstant.PARAM_CODE, request.getParameter(WeChatConstant.PARAM_CODE))
                .queryParam(WeChatConstant.PARAM_GRANT_TYPE, "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.build().toUri(), String.class);

        AuthAccessTokenDto authAccessTokenDto = JSON.parseObject(responseEntity.getBody(), AuthAccessTokenDto.class);

        WeChatUserDto weChatUserDto = weChatCommonApi.getWeChatUserInfo(authAccessTokenDto.getOpenid());

        logger.info("weChat auth responseEntity:{}", JSON.toJSONString(authAccessTokenDto));

        return new ModelAndView("redirect:https://www.bing.com");
    }

    @GetMapping("create/kefu")
    public void createAuthMenu(){

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getAuthUrl())
                .queryParam(WeChatConstant.PARAM_APPID, weChatAccountProperties.getAppId())
                .queryParam("redirect_uri", AUTH_REDIRECT_URL)
                .queryParam("response_type", WeChatConstant.PARAM_CODE)
                .queryParam(WeChatConstant.PARAM_SCOPE, BASE_SCOPE)
                .queryParam(WeChatConstant.PARAM_STATE, "123#wechat_redirect");


        CustomMenuCreateDto.Button keFuBtn = CustomMenuCreateDto.Button.build()
                .setName("在线客服")
                .setType("view")
                .setUrl(builder.build().toUri().toString());

        CustomMenuCreateDto createDto = CustomMenuCreateDto.build().addButton(keFuBtn);

        createMenu(createDto);
    }

}
