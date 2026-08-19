package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc 问题状态枚举
 */
@Getter
@AllArgsConstructor
public enum ProblemStatusEnum {
    NEW(1, "新产生"),
    VIEWED(2, "已查看"),
    CONFIRMED(3, "已确认");

    private final int code;
    private final String description;


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getName(int code) {
        for (ProblemStatusEnum status : ProblemStatusEnum.values()) {
            if (status.getCode() == code) {
                return status.getDescription();
            }
        }
        throw new IllegalArgumentException("未知code: " + code);
    }
}
