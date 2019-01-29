package com.xiong.web;

import com.alibaba.fastjson.JSON;
import com.xiong.util.constants.WeChatConstant;
import com.xiong.util.properties.WeChatProperty;
import com.xiong.wechat.vo.QRCodeReqVO;
import com.xiong.wechat.vo.QRCodeRpsVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Controller
public class WeChatController{

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private WeChatProperty weChatProperty;

    @GetMapping("show/qrcode")
    public void show(HttpServletResponse response) throws Exception {

        QRCodeReqVO qrCodeReqVO = new QRCodeReqVO();
        qrCodeReqVO.setAction_info(new QRCodeReqVO.ActionInfo(new QRCodeReqVO.Scene("userId:123456")));

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        os.write(getQRCode(qrCodeReqVO).getQrCodeBytes());
        os.flush();
        os.close();
    }


    @GetMapping(value = "get/qrCode")
    @ResponseBody
    public QRCodeRpsVO getQRCode(@RequestBody QRCodeReqVO qrCodeReqVO) {

        QRCodeRpsVO qrCodeRpsVO = generateQRCodeTicket(qrCodeReqVO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        HttpEntity<Resource> httpEntity = new HttpEntity<>(httpHeaders);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getQrCodeShowUrl())
                .queryParam(WeChatConstant.PARAM_TICKET, qrCodeRpsVO.getTicket());

        ResponseEntity<byte[]> response = restTemplate.exchange(builder.build().encode().toUri().toString(), HttpMethod.GET, httpEntity, byte[].class);

        qrCodeRpsVO.setQrCodeBytes(response.getBody());

        return qrCodeRpsVO;
    }


    private QRCodeRpsVO generateQRCodeTicket(QRCodeReqVO qrCodeReqVO){
        String accessToken = stringRedisTemplate.opsForValue().get(WeChatConstant.ACCESS_TOKEN_CACHE_KEY);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weChatProperty.getQrCodeCreateUrl())
                .queryParam(WeChatConstant.PARAM_ACCESS_TOKEN, accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(qrCodeReqVO), headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(builder.build().toUri(), entity, String.class);


        return JSON.parseObject(responseEntity.getBody(), QRCodeRpsVO.class);
    }
}
