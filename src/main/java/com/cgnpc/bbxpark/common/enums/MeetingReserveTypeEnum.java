package com.cgnpc.bbxpark.common.enums;

/***
 * @Description 会议类型枚举
 * @author huangyongtao
 * @date 2024/12/24 17:20
 */
public enum MeetingReserveTypeEnum {
    /**
     * 普通会议
     */
    ORDINARY("ordinary", "普通会议"),

    /**
     * 视频会议
     */
    VIDEO("video", "视频会议");

    /**
     * 状态编码
     */
    private String value;

    /**
     * 状态名称
     */
    private String name;

    MeetingReserveTypeEnum(String value, String name){
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
