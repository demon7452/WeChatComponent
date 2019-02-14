package com.xiong.wechat.lib.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:properties/wechat.properties")
public class WeChatProperty {

    @Value("${app_id}")
    private String appId;

    @Value("${app_secret}")
    private String appSecret;

    @Value("${encoding_aes_key}")
    private String encodingAesKey;

    @Value("${callback_url_token}")
    private String callbackUrlToken;

    @Value("${access_token_url}")
    private String accessTokenUrl;

    @Value("${qrcode_create_url}")
    private String qrCodeCreateUrl;

    @Value("${qrcode_show_url}")
    private String qrCodeShowUrl;

    @Value("${template_message_url}")
    private String templateMessageUrl;

    @Value("${single_wechat_user_info_url}")
    private String singleWeChatUserInfoUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public String getQrCodeCreateUrl() {
        return qrCodeCreateUrl;
    }

    public void setQrCodeCreateUrl(String qrCodeCreateUrl) {
        this.qrCodeCreateUrl = qrCodeCreateUrl;
    }

    public String getQrCodeShowUrl() {
        return qrCodeShowUrl;
    }

    public void setQrCodeShowUrl(String qrCodeShowUrl) {
        this.qrCodeShowUrl = qrCodeShowUrl;
    }

    public String getTemplateMessageUrl() {
        return templateMessageUrl;
    }

    public void setTemplateMessageUrl(String templateMessageUrl) {
        this.templateMessageUrl = templateMessageUrl;
    }

    public String getSingleWeChatUserInfoUrl() {
        return singleWeChatUserInfoUrl;
    }

    public void setSingleWeChatUserInfoUrl(String singleWeChatUserInfoUrl) {
        this.singleWeChatUserInfoUrl = singleWeChatUserInfoUrl;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public String getCallbackUrlToken() {
        return callbackUrlToken;
    }

    public void setCallbackUrlToken(String callbackUrlToken) {
        this.callbackUrlToken = callbackUrlToken;
    }
}
