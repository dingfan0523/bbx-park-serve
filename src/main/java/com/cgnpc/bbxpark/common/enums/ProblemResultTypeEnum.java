package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @create zhaoshuo
 * @time 2025/3/24
 * @desc 问题处理结果类型枚举
 */
@Getter
@AllArgsConstructor
public enum ProblemResultTypeEnum {
    /**
     * 转工单
     */
    TURN_TO_WORK_ORDER(1,"转工单"),
    /**
     * 无需处理
     */
    NO_ACTION_REQUIRED(2,"无需处理");

    private final Integer code;
    private final String description;

    public Integer getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举常量
     * @param code 枚举对应的code
     * @return 对应的枚举常量
     * @throws IllegalArgumentException 如果code不存在对应的枚举常量
     */
    public static String fromCode(Integer code) {
        for (ProblemResultTypeEnum type : ProblemResultTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDescription();
            }
        }
        throw new IllegalArgumentException("Invalid code for ProblemResultType: " + code);
    }
}
