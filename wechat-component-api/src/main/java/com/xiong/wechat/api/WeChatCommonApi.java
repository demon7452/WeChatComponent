package com.xiong.wechat.api;

import com.xiong.wechat.api.dto.WeChatUserDto;

public interface WeChatCommonApi {

    WeChatUserDto getWeChatUserInfo(String openId);
}
