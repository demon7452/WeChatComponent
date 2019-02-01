package com.xiong.wechat.api;

import com.xiong.wechat.api.dto.QRCodeReqDto;
import com.xiong.wechat.api.dto.QRCodeRpsDto;
import com.xiong.wechat.api.dto.WeChatUserDto;

import java.util.Map;

public interface WeChatCommonApi {

    /**
     * 生成带参数的二维码
     * @param qrCodeReqDto 请求对象
     * @return 二维码字节流
     */
    QRCodeRpsDto getQRCode(QRCodeReqDto qrCodeReqDto);

    /**
     * 处理微信回调信息
     * @param callbackMap 回调信息
     * @return 响应
     */
    String handleCallBack(Map<String, String> callbackMap);

    /**
     * 微信用户详细信息
     * @param openId 微信openId
     * @return 微信用户详细信息
     */
    WeChatUserDto getWeChatUserInfo(String openId);

}
