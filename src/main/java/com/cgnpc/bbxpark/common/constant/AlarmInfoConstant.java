package com.cgnpc.bbxpark.common.constant;

public final class AlarmInfoConstant {

    /**
     * 告警状态(1待确认、2已确认、3已结束)
     * 告警状态(1新产生、2已确认、3已结束)
     */
    public final static Integer ALARM_STATUS_1 = 1;
    public final static Integer ALARM_STATUS_3 = 3;
    public final static Integer ALARM_STATUS_2 = 2;

    /**
     * 1规则管理、2场景编排
     */
    public final static Integer ALARM_SOURCE_1 = 1;
    public final static Integer ALARM_SOURCE_2 = 2;

    /**
     * 启用状态(0启用、停用)
     */
    public final static Integer ALARM_IGNORE_CONFIG_0 = 0;
    public final static Integer ALARM_IGNORE_CONFIG_1 = 1;


    /**
     * 告警结束类型(1故障排除2设备下线3系统误报4忽略告警5手动结束6设备删除7设备禁用)
     */
    public static final Integer ALARM_END_TYPE_1 = 1;
    public static final Integer ALARM_END_TYPE_2 = 2;
    public static final Integer ALARM_END_TYPE_3 = 3;
    public static final Integer ALARM_END_TYPE_4 = 4;
    public static final Integer ALARM_END_TYPE_5 = 5;
    public static final Integer ALARM_END_TYPE_6 = 6;
    public static final Integer ALARM_END_TYPE_7 = 7;


    /**
     * 告警确认结果(1真实告警、2误报、3运维导致、4级别调整)
     */
    public static final Integer ALARM_CONFIRM_RESULT_1 = 1 ;
    public static final Integer ALARM_CONFIRM_RESULT_2 = 2 ;
    public static final Integer ALARM_CONFIRM_RESULT_3 = 3 ;
    public static final Integer ALARM_CONFIRM_RESULT_4 = 4 ;



}
