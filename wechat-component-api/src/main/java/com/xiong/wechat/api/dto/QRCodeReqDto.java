package com.xiong.wechat.api.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class QRCodeReqDto implements Serializable {

    private static final long serialVersionUID = 6324136394910494629L;

    private Map<String, Object> sceneMap;
    /**
     * 该二维码有效时间，以秒为单位。
     * 默认为10分钟
     */
    private long expire_seconds = 600L;

    /**
     * 二维码类型，
     * QR_SCENE为临时的整型参数值
     * QR_STR_SCENE为临时的字符串参数值
     * QR_LIMIT_SCENE为永久的整型参数值
     * QR_LIMIT_STR_SCENE为永久的字符串参数值
     */
    private String action_name = "QR_STR_SCENE";

    /**
     * 二维码详细信息
     */
    private ActionInfo action_info;

    private QRCodeReqDto() {
        sceneMap = new HashMap<>();
    }

    public static QRCodeReqDto build(){
        return new QRCodeReqDto();
    }

    public long getExpire_seconds() {
        return expire_seconds;
    }

    public QRCodeReqDto setExpire_seconds(long expire_seconds) {
        this.expire_seconds = expire_seconds;
        return this;
    }

    public String getAction_name() {
        return action_name;
    }

    public QRCodeReqDto setAction_name(String action_name) {
        this.action_name = action_name;
        return this;
    }

    public ActionInfo getAction_info() {
        return action_info;
    }

    public QRCodeReqDto setAction_info(ActionInfo action_info) {
        this.action_info = action_info;
        return this;
    }

    public QRCodeReqDto setPenetrateParameter(String penetrateParameter) {
        Scene scene = new Scene(penetrateParameter);
        setAction_info(new ActionInfo(scene));
        return this;
    }

    public QRCodeReqDto addJsonPenetrateParameter(String key, Object value) {
        sceneMap.put(key,value);
        return setPenetrateParameter(JSON.toJSONString(sceneMap));
    }

    public static class ActionInfo implements Serializable {

        private static final long serialVersionUID = 2367502783126437941L;

        private Scene scene;

        public ActionInfo() {
        }

        public ActionInfo(Scene scene) {
            this.scene = scene;
        }

        public Scene getScene() {
            return scene;
        }

        public void setScene(Scene scene) {
            this.scene = scene;
        }
    }

    public static class Scene implements Serializable{


        private static final long serialVersionUID = -7214624400068137942L;

        /**
         * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
         */
        private String scene_str;

        public Scene() {
        }

        public Scene(String scene_str) {
            this.scene_str = scene_str;
        }

        public String getScene_str() {
            return scene_str;
        }

        public void setScene_str(String scene_str) {
            this.scene_str = scene_str;
        }
    }
}




