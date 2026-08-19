package com.cgnpc.bbxpark.common.enums;

/***
 * @Description 会服状态
 * @author huangyongtao
 * @date 2024/12/24 17:27
 */
public enum MeetingAttendantTaskStatusEnum {
    /**
     * 未处理
     */
    UNHANDLE (1, "未处理"),

    /**
     * 已确认
     */
    CONFIRM(2, "已确认"),

    /**
     * 已完成
     */
    COMPLETE (3, "已完成");

    /**
     * 状态编码
     */
    private Integer value;

    /**
     * 状态名称
     */
    private String name;

    MeetingAttendantTaskStatusEnum(Integer value, String name){
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
