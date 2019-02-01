package com.xiong.wechat.service;

import com.alibaba.fastjson.JSON;
import com.xiong.wechat.api.WeChatCommonApi;
import com.xiong.wechat.api.dto.QRCodeReqDto;
import com.xiong.wechat.api.dto.QRCodeRpsDto;
import com.xiong.wechat.api.dto.WeChatUserDto;
import com.xiong.wechat.lib.constants.WeChatConstant;
import com.xiong.wechat.lib.properties.WeChatProperty;
import com.xiong.wechat.lib.util.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@Service
public class WeChatCommonImpl implements WeChatCommonApi {

    private Logger logger = LoggerFactory.getLogger(WeChatCommonImpl.class);

    @Resource
    private WeChatProperty weChatProperty;

    @Resource
    private AccessToken accessToken;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public QRCodeRpsDto getQRCode(QRCodeReqDto qrCodeReqDto) {

        QRCodeRpsDto qrCodeRpsDto = generateQRCodeTicket(qrCodeReqDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        HttpEntity<Resource> httpEntity = new HttpEntity<>(httpHeaders);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getQrCodeShowUrl())
                .queryParam(WeChatConstant.PARAM_TICKET, qrCodeRpsDto.getTicket());

        ResponseEntity<byte[]> response = restTemplate.exchange(builder.build().encode().toUri().toString(), HttpMethod.GET, httpEntity, byte[].class);

        qrCodeRpsDto.setQrCodeBytes(response.getBody());

        return qrCodeRpsDto;
    }

    @Override
    public WeChatUserDto getWeChatUserInfo(String openId) {

        StringHttpMessageConverter messageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate localRest = new RestTemplateBuilder().additionalMessageConverters(messageConverter).build();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getSingleWeChatUserInfoUrl())
                .queryParam(WeChatConstant.PARAM_ACCESS_TOKEN, accessToken.getValue())
                .queryParam(WeChatConstant.PARAM_OPEN_ID, openId);

        ResponseEntity<String> responseEntity = localRest.getForEntity(builder.build().toUri(), String.class);

        logger.info("weChat user info:{}", responseEntity.getBody());

        return JSON.parseObject(responseEntity.getBody(), WeChatUserDto.class);
    }


    private QRCodeRpsDto generateQRCodeTicket(QRCodeReqDto qrCodeReqDto) {
        logger.info("generate qr code ticket request:{}", JSON.toJSONString(qrCodeReqDto));

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getQrCodeCreateUrl())
                .queryParam(WeChatConstant.PARAM_ACCESS_TOKEN, accessToken.getValue());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(qrCodeReqDto), headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(builder.build().toUri(), entity, String.class);

        logger.info("qr code ticket:{}", responseEntity.getBody());
        return JSON.parseObject(responseEntity.getBody(), QRCodeRpsDto.class);
    }
}