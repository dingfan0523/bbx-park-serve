package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 派单方式枚举
 * @author huangyongtao
 * @date 2025/11/4 15:18
 */
@Getter
@AllArgsConstructor
public enum DispatchTypeEnum {

    /**
     * 分组人员抢单
     */
    GROUPING(10, "分组人员抢单"),

    /**
     * 组长派单
     */
    LEADER(20, "组长派单"),
    /**
     * 直接指派
     */
    ASSIGN( 30, "直接指派");
    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (DispatchTypeEnum configEnum : DispatchTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (DispatchTypeEnum configEnum : DispatchTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}