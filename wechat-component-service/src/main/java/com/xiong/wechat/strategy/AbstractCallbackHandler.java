package com.xiong.wechat.strategy;

import java.util.Map;

public abstract class AbstractCallbackHandler {

    public abstract String handleCallBack(Map<String, String> callbackMessage);
}
