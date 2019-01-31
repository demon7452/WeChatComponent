package com.xiong.wechat.service;

import com.alibaba.fastjson.JSON;
import com.xiong.wechat.api.WeChatCommonApi;
import com.xiong.wechat.api.dto.WeChatUserDto;
import com.xiong.wechat.lib.constants.WeChatConstant;
import com.xiong.wechat.lib.properties.WeChatProperty;
import com.xiong.wechat.lib.util.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.nio.charset.Charset;

import static com.xiong.wechat.lib.constants.WeChatConstant.PARAM_OPEN_ID;


@Service
public class WeChatCommonImpl implements WeChatCommonApi {

    private Logger logger = LoggerFactory.getLogger(WeChatCommonImpl.class);

    @Resource
    private WeChatProperty weChatProperty;

    @Resource
    private AccessToken accessToken;

    @Override
    public WeChatUserDto getWeChatUserInfo(String openId) {

        StringHttpMessageConverter messageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate localRest = new RestTemplateBuilder().additionalMessageConverters(messageConverter).build();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getSingleWeChatUserInfoUrl())
                .queryParam(WeChatConstant.PARAM_ACCESS_TOKEN, accessToken.getValue())
                .queryParam(PARAM_OPEN_ID, openId);

        ResponseEntity<String> responseEntity = localRest.getForEntity(builder.build().toUri(), String.class);

        logger.info("weChat user info:{}", responseEntity.getBody());

        return JSON.parseObject(responseEntity.getBody(), WeChatUserDto.class);
    }
}
