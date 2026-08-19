package com.cgnpc.bbxpark.common.enums;

/***
 * @Description 会服属性类型
 * @author huangyongtao
 * @date 2024/12/24 17:27
 */
public enum MeetingAttendantTaskAttributeTypeEnum {
    /**
     * 会议普通服务
     */
    ORDINARY (1, "会议普通服务"),

    /**
     * 视频会议调试服务
     */
    VIDEO(2, "视频会议调试服务"),

    /**
     * 会议录音服务
     */
    RECORDING (3, "会议录音服务"),

    /**
     * 会议排座服务
     */
    SEAT (4, "会议排座服务"),


    /**
     * 会议打印服务
     */
    PRINT (5, "会议打印服务"),

    /**
     * 会中呼叫服务
     */
    CALL (6, "会中呼叫服务"),

    /**
     * 会后清洁服务
     */
    CLEAR (7, "会后清洁服务");

    /**
     * 状态编码
     */
    private Integer value;

    /**
     * 状态名称
     */
    private String name;

    MeetingAttendantTaskAttributeTypeEnum(Integer value, String name){
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
