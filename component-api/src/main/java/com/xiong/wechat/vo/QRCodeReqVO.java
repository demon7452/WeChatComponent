package com.xiong.wechat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value="QRCodeReqVO对象",description="二维码请求参数对象")
public class QRCodeReqVO implements Serializable {

    private static final long serialVersionUID = 6324136394910494629L;
    /**
     * 该二维码有效时间，以秒为单位。
     * 默认为2分钟
     */
    @ApiModelProperty(value = "expire_seconds,二维码有效时间，以秒为单位,默认为10分钟")
    private long expire_seconds = 600L;

    /**
     * 二维码类型，
     * QR_SCENE为临时的整型参数值
     * QR_STR_SCENE为临时的字符串参数值
     * QR_LIMIT_SCENE为永久的整型参数值
     * QR_LIMIT_STR_SCENE为永久的字符串参数值
     */
    @ApiModelProperty(value = "action_name * 二维码类型，\n" +
            "     * QR_SCENE为临时的整型参数值\n" +
            "     * QR_STR_SCENE为临时的字符串参数值\n" +
            "     * QR_LIMIT_SCENE为永久的整型参数值\n" +
            "     * QR_LIMIT_STR_SCENE为永久的字符串参数值")
    private String action_name = "QR_STR_SCENE";

    /**
     * 二维码详细信息
     */
    @ApiModelProperty(value = "action_info二维码详细信息")
    private ActionInfo action_info;


    public long getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(long expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public ActionInfo getAction_info() {
        return action_info;
    }

    public void setAction_info(ActionInfo action_info) {
        this.action_info = action_info;
    }

    public static class ActionInfo{

        private Scene scene;

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

    public static class Scene{
        /**
         * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
         */
        @ApiModelProperty(value = "scene_str场景值ID（字符串形式的ID），字符串类型，长度限制为1到64")
        private String scene_str;

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




