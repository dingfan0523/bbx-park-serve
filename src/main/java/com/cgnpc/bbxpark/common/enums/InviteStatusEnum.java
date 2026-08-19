package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 邀约状态枚举
 * @author huangyongtao
 * @date 2025/8/4 16:46
 */
@Getter
@AllArgsConstructor
public enum InviteStatusEnum {

    /**
     * 待审批
     */
    APPROVE(10, "待审批"),

    /**
     * 待来访
     */
    VISIT( 20, "待来访"),

    /**
     * 访问中
     */
    VISITING( 30, "访问中"),

    /**
     * 已结束
     */
    END( 40, "已结束");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (InviteStatusEnum configEnum : InviteStatusEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (InviteStatusEnum configEnum : InviteStatusEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}