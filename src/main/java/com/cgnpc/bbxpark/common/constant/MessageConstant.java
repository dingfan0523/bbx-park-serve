package com.cgnpc.bbxpark.common.constant;

/**
 * 消息中心常量
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/29 16:50
 */
public final class MessageConstant {
    /**
     * PC端包间预约取消模板code
     */
    public final static String PC_COMPARTMENT_RESERVE_CANCEL = "44BTJHM6PRFX";
    /**
     * 移动端端包间预约取消模板code
     */
    public final static String APP_COMPARTMENT_RESERVE_CANCEL = "NJTB919F8LV5";
    /**
     * 工单关闭模板code
     */
    public final static String ORDER_CLOSE = "DFHHHEV01X83";
    /**
     * 工单完成模板code
     */
    public final static String ORDER_COMPLETE = "4MRV560W8FEB";
    /**
     * 工单未完成模板code
     */
    public final static String ORDER_UNCOMPLETE = "ORDERUNCOMPLETE";

    /**
     * 工单未完成通知分配人模板code
     */
    public final static String ORDER_UNCOMPLETE_TOALLOT = "ORDERUNCOMPLETETOALLOT";
    /**
     * 设备告警模板code
     */
    public final static String DEVICE_ALARM = "3V1P5ARE9JGF";
    /**
     * 投诉建议回复模板code
     */
    public final static String ADVICE_REPLAY = "YMMOG2OWX3TO";
    /**
     * 会议签到提醒模板code
     */
    public final static String MEETING_SIGN_WARN = "81UAUC7TH7IG";

    /**
     * 临时会议预约成功通知
     */
    public final static String TEMP_MEETING_RESERVE = "PES9RFGW901T";
    /**
     * 临时呼叫通知
     * 通知会服人员
     */
    public final static String TEMP_CALL = "NSHB1C26OKTU";
    /**
     * 会服未确认提醒
     * 三十分钟后开始还未处理会前布置任务的会议
     */
    public final static String WAITING_CONFIRM_WARN = "OU6WJE1UPM89";
    /**
     * 设备告警提醒-
     * 三十分钟后开始但会议室设备有告警提醒预约人
     */
    public final static String DEVICE_WARN = "PAODXZF5DLBS";
    /**
     * 设备告警提醒
     * 三十分钟后开始但会议室设备有告警提醒会服人员
     */
    public final static String DEVICE_WARN_ATTENDANT = "TK1AWAL24KEY";
    /**
     * 会议无人签到提醒
     * 会议到达开始时间还没有人签到提醒预约人
     */
    public final static String MEETING_NO_SIGN_WARN = "D7LNJ4JSEDV2";
    /**
     * 会议无人签到提醒
     * 会议到达开始时间还没有人签到提醒会服人员
     */
    public final static String MEETING_NO_SIGN_WARN_ATTENDANT = "E55O18I0RB1S";

    /**
     * 会议延时提醒
     * 会议还剩三十分钟时提醒发起人延长会议
     */
    public final static String MEETING_DELAY = "YBH8SBUF28RS";

    /**
     * 会议取消通知
     * 会议取消时提醒会服
     */
    public final static String MEETING_CANCEL = "GWTMO2YZVHLU";

    /**
     * 紧急告警通知
     * 触发紧急告警等级的告警级别时发送通知
     */
    public final static String URGENT_ALARM = "URGENTALARMMSG";

    /**
     * 紧急升级通知
     * 紧急升级时发送通知
     */
    public final static String ALARM_UPGRADE = "ALARMUPGRADE";


    /**
     * 紧急报事报修通知-紧急工单提醒
     * 处理报事报修时选择转工单并勾选紧急发送通知
     */
    public final static String URGENCY_PROBLEM = "URGENCYPROBLEM";

    /**
     * 报事报修转工单通知-转工单通知
     * 处理报事报修时选择转工单
     */
    public final static String TRANSFER_WORK_PROBLEM = "TRANSFERWORKPROBLEM";

    /**
     * 紧急报事报修通知-报修关闭通知
     * 处理报事报修时选择无需处理
     */
    public final static String PROBLEM_HANDLE = "PROBLEMHANDLE";

    /**
     * 工单超时提醒
     */
    public final static String ORDER_OUT_TIME_NOTICE = "ORDEROUTTIMENOTICE";
    /**
     * 工单催办通知
     */
    public final static String ORDER_URGE_NOTICE = "ORDERURGENOTICE";
    /**
     * 工单完成通知
     */
    public final static String ORDER_COMPLETED_NOTICE = "ORDERCOMPLETEDNOTICE";
    /**
     * 工单审核不通过通知
     */
    public final static String ORDER_AUDIT_FAIL_NOTICE = "ORDERAUDITFAILNOTICE";
    /**
     * 工单转派通知
     */
    public final static String ORDER_TRANSFER_NOTICE = "ORDERTRANSFERNOTICE";

    /**
     * 关注人提醒-报事报修
     */
    public final static String ATTENTION_REPORT_NOTICE = "ATTENTIONREPORTNOTICE";

    /**
     * 关注人提醒-投诉建议
     */
    public final static String ATTENTION_COMPLAINT_NOTICE = "ATTENTIONCOMPLAINTNOTICE";

    /**
     * 关注人提醒-评价反馈
     */
    public final static String ATTENTION_EVALUATE_NOTICE = "ATTENTIONEVALUATENOTICE";

    /**
     * 访客接待通知
     */
    public final static String INVITE_RECEIVE_NOTICE = "INVITERECEIVENOTICE";

    /**
     * 审批不通过通知
     */
    public final static String INVITE_APPROVAL_NOTICE = "INVITEAPPROVALNOTICE";

    /**
     * 访客来访通知
     */
    public final static String INVITE_VISIT_NOTICE = "INVITEVISITNOTICE";

    /**
     * 邀约取消通知
     */
    public final static String INVITE_CANCEL_NOTICE = "INVITECANCELNOTICE";

//    3V1P5ARE9JGF
//            YMMOG2OWX3TO
//81UAUC7TH7IG
//            HNVY3KOWVHVO
//    JCWCDJJYE786
//            UWBV5YBBUTM5
//    CAXY07S8O4OP
//            W4OB35B4BBAK
}
