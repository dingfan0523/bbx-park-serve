package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 工单任务项类型枚举
 * @author huangyongtao
 * @date 2025/11/4 15:18
 */
@Getter
@AllArgsConstructor
public enum WorkTaskItemTypeEnum {
    /**
     * 维保项目
     */
    MAINTAIN_ITEM(1, "维保项目"),

    /**
     * 巡检点
     */
    PATROL_POINT(2, "巡更点");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (WorkTaskItemTypeEnum configEnum : WorkTaskItemTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (WorkTaskItemTypeEnum configEnum : WorkTaskItemTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}