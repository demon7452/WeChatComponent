package com.xiong.wechat.strategy.callback;

import com.alibaba.fastjson.JSON;
import com.xiong.wechat.api.dto.EventCallbackDto;
import com.xiong.wechat.lib.enums.EventTypeEnum;
import com.xiong.wechat.lib.enums.MsgTypeEnum;
import com.xiong.wechat.lib.reply.BaseReply;
import com.xiong.wechat.lib.reply.TextReply;
import com.xiong.wechat.lib.util.XmlUtil;
import com.xiong.wechat.strategy.AbstractCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static com.xiong.wechat.lib.constants.CallbackParameterNameConstant.*;
import static com.xiong.wechat.lib.constants.WeChatConstant.DEFAULT_SUBSCRIBE_REPLY;
import static com.xiong.wechat.lib.constants.WeChatConstant.DEFAULT_SUCCESS;

@Component("eventCallbackHandler")
public class EventCallbackHandler extends AbstractCallbackHandler {

    private Logger logger = LoggerFactory.getLogger(EventCallbackHandler.class);

    @Override
    public String handleCallBack(Map<String, String> callbackMessage) {

        EventCallbackDto eventCallbackDto = convertToEventCallbackDto(callbackMessage);

        EventTypeEnum eventTypeEnum = eventCallbackDto.getEventTypeEnum();

        if (null == eventTypeEnum) {
            logger.error("no match event type!");
            return DEFAULT_SUCCESS;
        }

        switch (eventTypeEnum) {
            case SUBSCRIBE:
                return handleSubscribeEvent(eventCallbackDto);
            case SCAN:
                return DEFAULT_SUCCESS;
            case UNSUBSCRIBE:
                return DEFAULT_SUCCESS;// TODO: 19-2-25
            default:
                return DEFAULT_SUCCESS;
        }
    }

    private String handleSubscribeEvent(EventCallbackDto eventCallbackDto) {

        logger.info("user subscribe event:{}", JSON.toJSONString(eventCallbackDto));

        BaseReply textReply = TextReply.build()
                .setContent(DEFAULT_SUBSCRIBE_REPLY)
                .setToUserOpenId(eventCallbackDto.getFromUserName())
                .setWeChatOpenId(eventCallbackDto.getToUserName());

        return XmlUtil.objectToXml(textReply);
    }

    private EventCallbackDto convertToEventCallbackDto(Map<String, String> callbackMessage) {

        EventCallbackDto eventCallbackDto = new EventCallbackDto();

        eventCallbackDto.setToUserName(callbackMessage.get(TO_USER_NAME));
        eventCallbackDto.setFromUserName(callbackMessage.get(FROM_USER_NAME));
        eventCallbackDto.setCreateTime(new Date(Long.parseLong(callbackMessage.get(CREATE_TIME))));
        eventCallbackDto.setMsgTypeEnum(MsgTypeEnum.getEnumByKey(callbackMessage.get(MSG_TYPE)));
        eventCallbackDto.setEventTypeEnum(EventTypeEnum.getEnumByKey(callbackMessage.get(EVENT)));
        eventCallbackDto.setEventKey(callbackMessage.get(EVENT_KEY));
        eventCallbackDto.setTicket(callbackMessage.get(TICKET));

        return eventCallbackDto;
    }
}
