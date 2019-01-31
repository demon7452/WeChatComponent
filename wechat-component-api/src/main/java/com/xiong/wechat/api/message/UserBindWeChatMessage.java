package com.xiong.wechat.api.message;

import java.io.Serializable;
import java.util.Date;

public class UserBindWeChatMessage implements Serializable {

    private static final long serialVersionUID = -1950040814485213405L;
    
    private String weChatNumber;

    private String openId;

    private Date bindTime;

    private String callbackParameter;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWeChatNumber() {
        return weChatNumber;
    }

    public void setWeChatNumber(String weChatNumber) {
        this.weChatNumber = weChatNumber;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public String getCallbackParameter() {
        return callbackParameter;
    }

    public void setCallbackParameter(String callbackParameter) {
        this.callbackParameter = callbackParameter;
    }
}
