package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 消息推送渠道举类
 * @author huangyongtao
 * @date 2024/10/25 10:35
 */
@Getter
@AllArgsConstructor
public enum MessagePushChannelEnum {

    /**
     * 站内信
     */
    INFO("info", "站内信"),

    /**
     * 钉钉
     */
    DING("ding", "钉钉"),

    /**
     * 短信
     */
    SMS("sms", "短信"),

    /**
     * 邮件
     */
    MAIL("mail", "邮件"),

    /**
     * 大屏(websocket推送)
     */
    SCREEN("screen","大屏");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    public static String getCode(String name) {
        for (MessagePushChannelEnum configEnum : MessagePushChannelEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return "";
    }

    public static String getName(String code) {
        for (MessagePushChannelEnum configEnum : MessagePushChannelEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}