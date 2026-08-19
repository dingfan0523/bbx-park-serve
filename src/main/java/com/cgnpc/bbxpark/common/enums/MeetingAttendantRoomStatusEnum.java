package com.cgnpc.bbxpark.common.enums;

/**
 * 会服人员-会议室状态
 */
public enum MeetingAttendantRoomStatusEnum {
    /**
     * 正常/已生效
     */
    NORMAL (1, "已生效"),

    /**
     * 新增-待生效
     */
    WAITING_ADD(2, "新增-待生效"),

    /**
     * 删除-待生效
     */
    WAITING_DEL (3, "删除-待生效");

    /**
     * 状态编码
     */
    private Integer value;

    /**
     * 状态名称
     */
    private String name;

    MeetingAttendantRoomStatusEnum(Integer value, String name){
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
