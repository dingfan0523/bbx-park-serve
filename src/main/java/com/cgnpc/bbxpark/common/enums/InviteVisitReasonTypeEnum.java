package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 邀约参观类型枚举
 * @author huangyongtao
 * @date 2025/8/4 16:46
 */
@Getter
@AllArgsConstructor
public enum InviteVisitReasonTypeEnum {

    /**
     * 参观调研
     */
    VISIT(1, "参观调研"),

    /**
     * 参加会议
     */
    MEETING(2, "参加会议"),

    /**
     * 业务培训
     */
    TRAINING( 3, "业务培训"),

    /**
     * 工作沟通
     */
    WORK( 4, "工作沟通"),

    /**
     * 其他
     */
    OTHER( 5, "其他");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (InviteVisitReasonTypeEnum configEnum : InviteVisitReasonTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (InviteVisitReasonTypeEnum configEnum : InviteVisitReasonTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}