package com.xiong.wechat.strategy.callback;

import com.alibaba.fastjson.JSON;
import com.xiong.wechat.api.dto.EventCallbackDto;
import com.xiong.wechat.lib.enums.EventTypeEnum;
import com.xiong.wechat.lib.enums.MsgTypeEnum;
import com.xiong.wechat.strategy.AbstractCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static com.xiong.wechat.lib.constants.CallbackParameterNameConstant.*;

@Component("eventCallbackHandler")
public class EventCallbackHandler extends AbstractCallbackHandler {

    private Logger logger = LoggerFactory.getLogger(EventCallbackHandler.class);

    @Override
    public String handleCallBack(Map<String, String> callbackMessage) {

        EventCallbackDto eventCallbackDto = convertToEventCallbackDto(callbackMessage);

        String replyMessage = "no match event type:";

        EventTypeEnum eventTypeEnum = eventCallbackDto.getEventTypeEnum();
        if (null == eventTypeEnum) {
            return replyMessage;
        }

        switch (eventTypeEnum) {
            case SUBSCRIBE:
                replyMessage = handleSubscribeEvent(eventCallbackDto);
                break;
            case SCAN:
                replyMessage = handleScanEvent(eventCallbackDto);
                break;
            case UNSUBSCRIBE:
                // TODO: 19-1-26
                break;
        }

        return replyMessage;
    }

    private String handleSubscribeEvent(EventCallbackDto eventCallbackDto) {

        logger.info("user subscribe event:{}", JSON.toJSONString(eventCallbackDto));
        return buildUserBindWeChatReply(eventCallbackDto);
    }

    private String handleScanEvent(EventCallbackDto eventCallbackDto) {
        logger.info("user scan event:{}", JSON.toJSONString(eventCallbackDto));
        return buildUserBindWeChatReply(eventCallbackDto);
    }

    private String buildUserBindWeChatReply(EventCallbackDto eventCallbackDto) {

        return "success";
//        TextReplyVO textReplyVO = new TextReplyVO();
//        textReplyVO.setFromUserName(eventMsgReqDto.getToUserName());
//        textReplyVO.setToUserName(eventMsgReqDto.getFromUserName());
//        textReplyVO.setCreateTime(System.currentTimeMillis());
//        textReplyVO.setMsgType(MsgTypeEnum.TEXT.getKey());
//        textReplyVO.setContent("回复文本消息");// TODO: 19-1-26
//        return XmlUtil.objectToXml(textReplyVO);
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
