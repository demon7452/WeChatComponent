package com.xiong.service.strategies;

import com.alibaba.fastjson.JSON;
import com.xiong.dtos.EventMsgReqDto;
import com.xiong.util.XmlUtil;
import com.xiong.util.enums.EventTypeEnum;
import com.xiong.util.enums.MsgTypeEnum;
import com.xiong.wechat.messages.UserBindWeChatMessage;
import com.xiong.wechat.vo.TextReplyVO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

import static com.xiong.util.constants.CallbackParameterNameConstant.*;
import static com.xiong.util.constants.RabbitMqConstant.USER_BIND_WECHAT_EXCHANGE;
import static com.xiong.util.constants.RabbitMqConstant.USER_BIND_WECHAT_ROUTING_KEY;

@Component("eventMsgHandleStrategy")
public class EventMsgHandleStrategy extends MsgHandleStrategy {

    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    public String handle(Map<String, String> callbackMessage) {

        EventMsgReqDto eventMsgReqDto = convertMsgToDto(callbackMessage);

        String replyMessage = "no match event type:";

        EventTypeEnum eventTypeEnum = eventMsgReqDto.getEventTypeEnum();
        if(null == eventTypeEnum){
            return replyMessage;
        }

        switch (eventTypeEnum){
            case SUBSCRIBE:
                replyMessage = handleSubscribeEvent(eventMsgReqDto);
                break;
            case SCAN:
                replyMessage = handleScanEvent(eventMsgReqDto);
                break;
            case UNSUBSCRIBE:
                // TODO: 19-1-26
                break;
        }

        return replyMessage;
    }

    private String handleSubscribeEvent(EventMsgReqDto eventMsgReqDto){

        sendUserBindWeChatMessage(eventMsgReqDto);
        return buildUserBindWeChatReply(eventMsgReqDto);
    }

    private String handleScanEvent(EventMsgReqDto eventMsgReqDto){

        sendUserBindWeChatMessage(eventMsgReqDto);
        return buildUserBindWeChatReply(eventMsgReqDto);
    }

    private void sendUserBindWeChatMessage(EventMsgReqDto eventMsgReqDto) {
        UserBindWeChatMessage message = new UserBindWeChatMessage();
        message.setWeChatNumber(eventMsgReqDto.getToUserName());
        message.setOpenId(eventMsgReqDto.getFromUserName());
        message.setBindTime(eventMsgReqDto.getCreateTime());
        message.setCallbackParameter(eventMsgReqDto.getEventKey());

        amqpTemplate.convertAndSend(USER_BIND_WECHAT_EXCHANGE, USER_BIND_WECHAT_ROUTING_KEY, JSON.toJSONString(message));
    }

    private String buildUserBindWeChatReply(EventMsgReqDto eventMsgReqDto){
        TextReplyVO textReplyVO = new TextReplyVO();
        textReplyVO.setFromUserName(eventMsgReqDto.getToUserName());
        textReplyVO.setToUserName(eventMsgReqDto.getFromUserName());
        textReplyVO.setCreateTime(System.currentTimeMillis());
        textReplyVO.setMsgType(MsgTypeEnum.TEXT.getKey());
        textReplyVO.setContent("回复文本消息");// TODO: 19-1-26
        return XmlUtil.objectToXml(textReplyVO);
    }

    private EventMsgReqDto convertMsgToDto(Map<String, String> callbackMessage){

        EventMsgReqDto eventMsgReqDto = new EventMsgReqDto();

        eventMsgReqDto.setToUserName(callbackMessage.get(TO_USER_NAME));
        eventMsgReqDto.setFromUserName(callbackMessage.get(FROM_USER_NAME));
        eventMsgReqDto.setCreateTime(new Date(Long.parseLong(callbackMessage.get(CREATE_TIME))));
        eventMsgReqDto.setMsgTypeEnum(MsgTypeEnum.getEnumByKey(callbackMessage.get(MSG_TYPE)));
        eventMsgReqDto.setEventTypeEnum(EventTypeEnum.getEnumByKey(callbackMessage.get(EVENT)));
        eventMsgReqDto.setEventKey(callbackMessage.get(EVENT_KEY));
        eventMsgReqDto.setTicket(callbackMessage.get(TICKET));

        return eventMsgReqDto;
    }
}
