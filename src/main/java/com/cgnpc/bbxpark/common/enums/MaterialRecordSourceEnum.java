package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 材料记录来源枚举
 * @author huangyongtao
 * @date 2025/9/23 16:46
 */
@Getter
@AllArgsConstructor
public enum MaterialRecordSourceEnum {

    /**
     * 手动入库
     */
    HAND(1, "手动入库"),

    /**
     * 工单维修
     */
    WORK( 2, "工单维修");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (MaterialRecordSourceEnum configEnum : MaterialRecordSourceEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (MaterialRecordSourceEnum configEnum : MaterialRecordSourceEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}