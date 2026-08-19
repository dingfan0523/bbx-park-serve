package com.cgnpc.dingtalk.enums;

/**
 * 消息类型枚举
 */
public enum MsgTypeEnum {

    /**
     * 链接消息
     */
    LINK("link"),

    /**
     * 文本消息
     */
    TEXT("text"),

    IMAGE("image"),

    FILE("file"),

    MARKDOWN("markdown"),

    OA("oa"),

    CARD("card");

    MsgTypeEnum(String msgType) {
        this.msgType = msgType;
    }

    private String msgType;

    public String getMsgType() {
        return msgType;
    }

}
