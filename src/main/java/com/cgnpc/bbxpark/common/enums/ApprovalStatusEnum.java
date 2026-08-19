package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审批状态枚举
 */
@Getter
@AllArgsConstructor
public enum ApprovalStatusEnum {

    /**
     * 已取消
     */
    CANCEL(-10, "已取消"),

    /**
     * 待审批
     */
    WAITING(10, "待审批"),

    /**
     * 审批通过
     */
    PASS(20, "审批通过"),

    /**
     * 审批不通过
     */
    REFUSE(30, "审批不通过");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;


    public static Integer getCode(String name) {
        for (ApprovalStatusEnum configEnum : ApprovalStatusEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (ApprovalStatusEnum configEnum : ApprovalStatusEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}