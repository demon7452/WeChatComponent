package com.xiong.wechat.web;

import com.xiong.wechat.scheduler.AccessTokenScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Resource
    private AccessTokenScheduler accessTokenScheduler;

    @RequestMapping("manual/update/token")
    @ResponseBody
    public ResponseEntity<String> manualUpdateToken(){
        return accessTokenScheduler.updateAccessToken();
    }
}
