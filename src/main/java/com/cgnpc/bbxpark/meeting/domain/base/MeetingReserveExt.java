
package com.cgnpc.bbxpark.meeting.domain.base;


import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;

import java.io.Serializable;
/***
 * @Description 会议预约数据模型实体扩展
 * @author huangyongtao
 * @date 2024/8/23 15:10
 */
public class MeetingReserveExt extends MeetingReserve implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4991312748537580806L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "meetingReserve";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_meeting_reserve";

    /**
    *数据库字段：主键id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：会议室id.
    **/
    public static final String C_ROOM_ID = "room_id";
    /**
    *数据库字段：会议主题.
    **/
    public static final String C_RESERVE_NAME = "reserve_name";
    /**
    *数据库字段：会议室名称.
    **/
    public static final String C_ROOM_NAME = "room_name";
    /**
    *数据库字段：会议开始时间.
    **/
    public static final String C_START_TIME = "start_time";
    /**
    *数据库字段：会议结束时间.
    **/
    public static final String C_END_TIME = "end_time";
    /**
    *数据库字段：预约人id.
    **/
    public static final String C_RESERVE_UID = "reserve_uid";
    /**
    *数据库字段：预约人名称.
    **/
    public static final String C_RESERVE_UNAME = "reserve_uname";
    /**
    *数据库字段：预约人工号.
    **/
    public static final String C_RESERVE_STAFFID = "reserve_staffid";
    /**
    *数据库字段：会议实际开始时间.
    **/
    public static final String C_REAL_START_TIME = "real_start_time";
    /**
    *数据库字段：会议实际结束时间.
    **/
    public static final String C_REAL_END_TIME = "real_end_time";
    /**
    *数据库字段：会议无效;0：否；1：是.
    **/
    public static final String C_IN_VALID = "in_valid";
    /**
    *数据库字段：会议无效备注.
    **/
    public static final String C_IN_VALID_REMARK = "in_valid_remark";
    /**
    *数据库字段：园区ID-租户号.
    **/
    public static final String C_TENANT_ID = "tenant_id";
    /**
    *数据库字段：创建人.
    **/
    public static final String C_CREATOR_ID = "creator_id";
    /**
    *数据库字段：创建时间.
    **/
    public static final String C_CREATE_TIME = "create_time";
    /**
    *数据库字段：更新人.
    **/
    public static final String C_UPDATOR_ID = "updator_id";
    /**
    *数据库字段：更新时间.
    **/
    public static final String C_UPDATE_TIME = "update_time";

    /**
    *实体属性：主键id.
    **/
    public static final String P_ID = "id";
    /**
    *实体属性：会议室id.
    **/
    public static final String P_ROOM_ID = "roomId";
    /**
    *实体属性：会议主题.
    **/
    public static final String P_RESERVE_NAME = "reserveName";
    /**
    *实体属性：会议室名称.
    **/
    public static final String P_ROOM_NAME = "roomName";
    /**
    *实体属性：会议开始时间.
    **/
    public static final String P_START_TIME = "startTime";
    /**
    *实体属性：会议结束时间.
    **/
    public static final String P_END_TIME = "endTime";
    /**
    *实体属性：预约人id.
    **/
    public static final String P_RESERVE_UID = "reserveUid";
    /**
    *实体属性：预约人名称.
    **/
    public static final String P_RESERVE_UNAME = "reserveUname";
    /**
    *实体属性：预约人工号.
    **/
    public static final String P_RESERVE_STAFFID = "reserveStaffid";
    /**
    *实体属性：会议实际开始时间.
    **/
    public static final String P_REAL_START_TIME = "realStartTime";
    /**
    *实体属性：会议实际结束时间.
    **/
    public static final String P_REAL_END_TIME = "realEndTime";
    /**
    *实体属性：会议无效;0：否；1：是.
    **/
    public static final String P_IN_VALID = "inValid";
    /**
    *实体属性：会议无效备注.
    **/
    public static final String P_IN_VALID_REMARK = "inValidRemark";
    /**
    *实体属性：园区ID-租户号.
    **/
    public static final String P_TENANT_ID = "tenantId";
    /**
    *实体属性：创建人.
    **/
    public static final String P_CREATOR_ID = "creatorId";
    /**
    *实体属性：创建时间.
    **/
    public static final String P_CREATE_TIME = "createTime";
    /**
    *实体属性：更新人.
    **/
    public static final String P_UPDATOR_ID = "updatorId";
    /**
    *实体属性：更新时间.
    **/
    public static final String P_UPDATE_TIME = "updateTime";
}
