package com.xiong.service;

import com.xiong.service.api.CallbackHandlerApi;
import com.xiong.service.strategies.EventMsgHandleStrategy;
import com.xiong.util.enums.MsgTypeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static com.xiong.util.constants.CallbackParameterNameConstant.MSG_TYPE;

@Service
public class CallbackHandlerImpl implements CallbackHandlerApi {

    @Resource
    private EventMsgHandleStrategy eventMsgHandleStrategy;

    @Override
    public String handle(Map<String, String> callbackMessage) {

        String msgType = callbackMessage.get(MSG_TYPE);

        MsgTypeEnum msgTypeEnum = MsgTypeEnum.getEnumByKey(msgType);

        if(null == msgTypeEnum){
            return "no match message type:" + msgType;
        }

        switch (msgTypeEnum){
            case EVENT:
                return eventMsgHandleStrategy.handle(callbackMessage);
            case TEXT:
                // 处理文本消息
        }
        return "no handler";

    }
}
