package com.cgnpc.bbxpark.common.enums;

import lombok.Getter;

/**
 * 会议室来源枚举
 * @author dingfan
 * @version 1.0
 * @date 2024/12/23 17:04
 */
@Getter
public enum MeetingRoomSourceEnum {
    /**
     * 本系统内新增
     */
    ADD("add", "本系统内新增"),
    /**
     * 集团会议系统同步
     */
    SYNC("sync", "集团会议系统同步");

    /**
     * 编码
     */
    private final String code;

    /**
     * 名称
     */
    private final String name;

    MeetingRoomSourceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
