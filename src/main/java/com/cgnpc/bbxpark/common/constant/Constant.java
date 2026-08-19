package com.cgnpc.bbxpark.common.constant;

/**
 * 常量
 */
public final class Constant {
    /**
     * 私有构造函数
     */
    private Constant() {
    }

    /**
     * 服务根路径
     */
    public static final String ROOT_PATH = "";

    /**
     * 基本版本路径
     */
    public static final String BASE_PATH = ROOT_PATH + "/v1";


    /**
     * 园区空间根节点父id
     */
    public static final Long SPACE_ROOT_ID = 0L;

    /**
     * 精确到天的日期格式
     */
    public static final String DATE_DAY_FORMAT = "yyyy-MM-dd";

    /**
     * 系统管理员账号
     */
    public static final String SYSTEM_ACCOUNT = "admin";

    /**
     * 系统参数配置-运维角色
     * 通过该code可以找到系统参数中配置的运维角色名称,然后可以查找运维角色下的用户
     */
    public static final String OPS_ROLE_NAME_CONFIG = "omConfig";

    /**
     * 系统参数配置-包间管理员角色
     * 通过该code可以找到系统参数中配置的运维角色名称,然后可以查找运维角色下的用户
     */
    public static final String COMPARTMENT_ROLE_CONFIG = "compartmentRoleConfig";
    /**
     * 系统参数配置-会服人员角色
     * 通过该code可以找到系统参数中配置的会服人员角色名称,然后可以查找会服人员角色下的用户
     */
    public static final String MEETING_ATTENDANT_ROLE_CONFIG = "meetingAttendantRoleConfig";

    /**
     * 系统参数配置-综管角色
     * 通过该code可以找到系统参数中配置的综管角色名称,然后可以查找综管角色下的用户
     */
    public static final String GENERAL_MANAGEMENT_ROLE_CONFIG = "generalManagementRoleConfig";

    public static final String PARK_TOTAL_PEOPLE = "parkTotalPeople";

    /**
     * 数据变更的异常信息
     */
    public static final String CHANGE_ERROR_MESSAGE = "该条数据状态已变更，请确认！";

    /**
     * 投诉建议邮件标题
     */
    public static final String COMPLAINTSUGGESTION_MAIL_TITLE = "投诉建议回复处理提醒";

    /**
     * 投诉建议邮件回复内容
     */
    public static final String COMPLAINTSUGGESTION_MAIL_CONTENT = "<p>reply，您好，assignment，给您分配了一条待回复的投诉建议，期望您的回复，点击下面链接即可在线回复处理。</p><br/><br/><br/><p>链接：url</p>";

    /**
     * 投诉建议邮件修改回复内容
     */
    public static final String COMPLAINTSUGGESTION_MAIL_EDIT_CONTENT = "<p>reply，您好，你最近回复的投诉建议内容，需要修改。修改原因：remark，期待您的回复修复，点击下面链接即可在线回复处理。</p><br/><br/><br/><p>链接：url</p>";

    /**
     * 智慧会议消息提醒内容
     */
    public static final String MEETING_SIGN_CONTENT = "您好，您预约的会议“meetingReserveName”，距离会议结束时间还有5分钟，请及时签到。";
}
