package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 工单任务类型枚举
 * @author huangyongtao
 * @date 2025/11/4 15:18
 */
@Getter
@AllArgsConstructor
public enum WorkTaskTypeEnum {
    /**
     * 维保设备
     */
    MAINTAIN_DEVICE(1, "维保设备"),

    /**
     * 巡检点
     */
    INSPECTION_POINT(2, "巡检点"),
    /**
     * 巡更路线
     */
    PATROL_ROUTE( 3, "巡更路线"),

    /**
     * 盘点设备
     */
    INVENTORY_DEVICE( 4, "盘点设备"),

    /**
     * 盘点材料
     */
    INVENTORY_MATERIAL( 5, "盘点材料"),

    /**
     * 任务
     */
    TASK( 6, "任务");
    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (WorkTaskTypeEnum configEnum : WorkTaskTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (WorkTaskTypeEnum configEnum : WorkTaskTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}