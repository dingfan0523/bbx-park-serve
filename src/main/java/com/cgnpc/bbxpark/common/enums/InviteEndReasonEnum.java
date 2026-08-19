package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 邀约结束原因枚举
 * @author huangyongtao
 * @date 2025/8/4 16:46
 */
@Getter
@AllArgsConstructor
public enum InviteEndReasonEnum {

    /**
     * 邀约取消
     */
    CANCEL(1, "邀约取消"),

    /**
     * 审批不通过
     */
    APPROVE( 2, "审批不通过"),

    /**
     * 超时未审批
     */
    TIMEOUT( 3, "超时未审批"),

    /**
     * 正常结束
     */
    NORMAL( 4, "正常结束");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (InviteEndReasonEnum configEnum : InviteEndReasonEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (InviteEndReasonEnum configEnum : InviteEndReasonEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}