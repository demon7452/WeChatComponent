package com.xiong.wechat;

import com.xiong.wechat.lib.util.AccessToken;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@EnableRabbit
public class WeChatApplication extends SpringBootServletInitializer {

    @Bean(value = "restTemplate")
    public RestTemplate createRestTemplate(){
        return new RestTemplate();
    }

    @Bean(value = "accessToken")
    public AccessToken initiateAccessToken(){
        return new AccessToken();
    }

    public static void main(String[] args) {
        SpringApplication.run(WeChatApplication.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WeChatApplication.class);
    }
}
