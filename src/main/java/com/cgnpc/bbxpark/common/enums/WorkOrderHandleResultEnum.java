package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @create zhaoshuo
 * @time 2025/4/16
 * @desc 工单处理结果枚举
 */
@Getter
@AllArgsConstructor
public enum WorkOrderHandleResultEnum {


    TYPE_1(1, "已解决"),
    TYPE_2(2, "确认存在异常，需要管理员介入");

    /**
     * 状态编码
     */
    private final Integer code;

    /**
     * 状态名称
     */
    private final String name;

    public static Integer getCode(String name) {
        for (WorkOrderHandleResultEnum configEnum : WorkOrderHandleResultEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (WorkOrderHandleResultEnum configEnum : WorkOrderHandleResultEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}
