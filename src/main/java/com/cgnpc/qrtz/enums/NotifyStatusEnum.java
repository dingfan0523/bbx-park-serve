package com.cgnpc.qrtz.enums;

/******************************
 * 用途说明: 超时通知状态枚举
 * 作者姓名: P636016 XIAOJINHUI
 * 创建时间: 2023/9/21 16:46:18
 ******************************/
public enum NotifyStatusEnum {
    SUCCESS("success","成功"),
    FAILURE("failure","失败"),
    EMAIL("email","邮件"),
    DING_TALK("dingTalk","鹭钉"),
    OVERTIME("overtime","超时提醒"),
    EXPIRE("expire","预警提醒");

    NotifyStatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
