package com.xiong.wechat.scheduler;

import com.alibaba.fastjson.JSON;
import com.xiong.wechat.api.dto.AccessTokenRpsDto;
import com.xiong.wechat.lib.constants.WeChatConstant;
import com.xiong.wechat.lib.properties.WeChatAccountProperties;
import com.xiong.wechat.lib.properties.WeChatProperty;
import com.xiong.wechat.lib.util.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class AccessTokenScheduler {

    private Logger logger = LoggerFactory.getLogger(AccessTokenScheduler.class);

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private WeChatProperty weChatProperty;

    @Resource
    private AccessToken accessToken;

    @Resource
    private WeChatAccountProperties weChatAccountProperties;
    /**
     * 微信AccessToken 会在2小时失效
     * 定时任务每100分钟执行一次，刷新AccessToken
     */
    @Scheduled(fixedRate = 100 * 60 * 1000L)
    public void updateAccessTokenSchedule(){
        updateAccessToken();
    }


    public ResponseEntity<String> updateAccessToken(){

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(weChatProperty.getAccessTokenUrl())
                .queryParam(WeChatConstant.PARAM_GRANT_TYPE, WeChatConstant.ACCESS_TOKEN_GRANT_TYPE)
                .queryParam(WeChatConstant.PARAM_APPID, weChatAccountProperties.getAppId())
                .queryParam(WeChatConstant.PARAM_SECRET, weChatAccountProperties.getAppSecret());

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(), String.class);

        AccessTokenRpsDto accessTokenRpsDto = JSON.parseObject(responseEntity.getBody(), AccessTokenRpsDto.class);
        if((HttpStatus.OK != responseEntity.getStatusCode()) || null == accessTokenRpsDto || StringUtils.isEmpty(accessTokenRpsDto.getAccess_token())){
            logger.error("failed to get access token from weChat! responseEntity:{}", JSON.toJSONString(responseEntity));
            return responseEntity;
        }

        stringRedisTemplate.opsForValue().set(WeChatConstant.ACCESS_TOKEN_CACHE_KEY, accessTokenRpsDto.getAccess_token(), 2, TimeUnit.HOURS);

        accessToken.setValue(accessTokenRpsDto.getAccess_token());

        logger.info("access token response entity:{}", JSON.toJSONString(responseEntity));
        return responseEntity;
    }
}
