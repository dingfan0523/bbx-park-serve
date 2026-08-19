package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 消息来源枚举类
 * @author huangyongtao
 * @date 2024/10/25 10:35
 */
@Getter
@AllArgsConstructor
public enum MessageSourceEnum {

    /**
     * 智慧餐厅
     */
    RESTAURANT("restaurant", "智慧餐厅", 3),

    /**
     * 智慧工单
     */
    ORDER("order", "智慧工单", 6),

    /**
     * 投诉建议
     */
    COMPLAINT("complaint", "投诉建议", 2),

    /**
     * 智慧会议
     */
    MEETING("meeting", "智慧会议", 7),

    /**
     * 告警消息
     */
    ALARM("alarm", "告警消息", 5),

    /**
     * 会服
     */
    MEETING_ATTENDANT("meetingAttendant", "会服消息", 8),

    /**
     * 报事报修
     */
    PROBLEM_REPORT("problemReport","报事报修",9),

    /**
     * 关注人提醒
     */
    ATTENTION("attention","关注人",10),

    /**
     * 访客接待提醒
     */
    INVITE_RECEIVE("inviteReceive","访客接待",11),

    /**
     * 访客审批提醒
     */
    INVITE_APPROVAL("inviteApproval","访客审批",12),

    /**
     * 访客邀约提醒
     */
    INVITE_VISIT("inviteVisit","访客邀约",13);

    /**
     * 状态编码
     */
    private final String code;

    /**
     * 状态名称
     */
    private final String name;

    /**
     * 消息类型
     */
    private final Integer type;

    public static String getCode(String name) {
        for (MessageSourceEnum configEnum : MessageSourceEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return "";
    }

    public static String getName(String code) {
        for (MessageSourceEnum configEnum : MessageSourceEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }

    public static Integer getType(String code) {
        for (MessageSourceEnum configEnum : MessageSourceEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getType();
            }
        }
        return null;
    }
}