package com.cgnpc.bbxpark.common.enums;

/***
 * @Description 会服属性枚举
 * @author huangyongtao
 * @date 2024/12/24 17:27
 */
public enum MeetingAttendantTaskAttributeEnum {
    /**
     * 普通服务
     */
    ORDINARY (1, "普通服务"),

    /**
     * 默认服务
     */
    DEFAULT(2, "默认服务");


    /**
     * 状态编码
     */
    private Integer value;

    /**
     * 状态名称
     */
    private String name;

    MeetingAttendantTaskAttributeEnum(Integer value, String name){
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
