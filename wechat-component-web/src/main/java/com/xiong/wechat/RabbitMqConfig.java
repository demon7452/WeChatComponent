package com.xiong.wechat;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.xiong.wechat.lib.constants.RabbitMqConstant.*;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue userBindWeChatQueue(){
        return new Queue(USER_BIND_WECHAT_QUEUE, true);
    }

    @Bean
    public TopicExchange userBindWeChatTopicExchange(){
        return new TopicExchange(USER_BIND_WECHAT_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingUserBindWeChat(){
        return BindingBuilder.bind(userBindWeChatQueue()).to(userBindWeChatTopicExchange()).with(USER_BIND_WECHAT_ROUTING_KEY);
    }
}
