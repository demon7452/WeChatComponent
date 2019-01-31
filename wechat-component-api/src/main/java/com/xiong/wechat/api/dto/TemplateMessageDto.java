package com.xiong.wechat.api.dto;

import java.util.HashMap;

/**
 * WeChat template message
 */
public class TemplateMessageDto {

    private String touser;

    private String template_id;

    private String url;

    private MiniProgram miniprogram;

    private HashMap<String,DataItem> data;

    private TemplateMessageDto() {
        data = new HashMap<>();
    }

    public static TemplateMessageDto build(){
        return new TemplateMessageDto();
    }

    public String getTouser() {
        return touser;
    }

    public TemplateMessageDto setTouser(String touser) {
        this.touser = touser;
        return this;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public TemplateMessageDto setTemplate_id(String template_id) {
        this.template_id = template_id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public TemplateMessageDto setUrl(String url) {
        this.url = url;
        return this;

    }

    public MiniProgram getMiniprogram() {
        return miniprogram;
    }

    public TemplateMessageDto setMiniprogram(String appid, String pagepath) {
        this.miniprogram.setAppid(appid);
        this.miniprogram.setPagepath(pagepath);
        return this;

    }

    public HashMap<String, DataItem> getData() {
        return data;
    }

    public TemplateMessageDto addData(String key, Object value) {
        return addData(key, value, null);
    }

    public TemplateMessageDto addData(String key, Object value, String color) {
        this.data.put(key, new DataItem(value, color));
        return this;
    }

    public static class MiniProgram{
        private String appid;

        private String pagepath;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPagepath() {
            return pagepath;
        }

        public void setPagepath(String pagepath) {
            this.pagepath = pagepath;
        }
    }

    public static class DataItem {
        private Object value;
        private String color;

        public Object getValue() {
            return value;
        }
        public void setValue(Object value) {
            this.value = value;
        }
        public String getColor() {
            return color;
        }
        public void setColor(String color) {
            this.color = color;
        }

        private DataItem(Object value, String color) {
            this.value = value;
            this.color = color;
        }
    }

}
