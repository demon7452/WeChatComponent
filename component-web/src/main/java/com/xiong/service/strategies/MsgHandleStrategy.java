package com.xiong.service.strategies;

import java.util.Map;

public abstract class MsgHandleStrategy {

    public abstract String handle(Map<String, String> callbackMessage);
}
