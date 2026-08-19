package com.cgnpc.bbxpark.common.enums;

/***
 * @Description 会服类型
 * @author huangyongtao
 * @date 2024/12/24 17:27
 */
public enum MeetingAttendantTaskTypeEnum {
    /**
     * 会前布置
     */
    BEFORE (1, "会前布置"),

    /**
     * 会中呼叫
     */
    IN(2, "会中呼叫"),

    /**
     * 会后清洁
     */
    AFTER (3, "会后清洁");

    /**
     * 状态编码
     */
    private Integer value;

    /**
     * 状态名称
     */
    private String name;

    MeetingAttendantTaskTypeEnum(Integer value, String name){
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
