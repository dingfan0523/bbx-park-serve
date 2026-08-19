
package com.cgnpc.bbxpark.meeting.domain.base;

import com.cgnpc.bbxpark.meeting.domain.MeetingReserveSign;

import java.io.Serializable;
/***
 * @Description 会议预约签到数据模型实体扩展
 * @author huangyongtao
 * @date 2024/8/23 15:20
 */
public class MeetingReserveSignExt extends MeetingReserveSign implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "meetingReserveSign";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_meeting_reserve_sign";

    /**
    *数据库字段：主键id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：会议预约id.
    **/
    public static final String C_RESERVE_ID = "reserve_id";
    /**
    *数据库字段：签到人id.
    **/
    public static final String C_SIGN_UID = "sign_uid";
    /**
    *数据库字段：签到人名称.
    **/
    public static final String C_SIGN_UNAME = "sign_uname";
    /**
    *数据库字段：签到人工号.
    **/
    public static final String C_SIGN_STAFFID = "sign_staffid";
    /**
    *数据库字段：签到时间.
    **/
    public static final String C_SIGN_TIME = "sign_time";
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
    *实体属性：会议预约id.
    **/
    public static final String P_RESERVE_ID = "reserveId";
    /**
    *实体属性：签到人id.
    **/
    public static final String P_SIGN_UID = "signUid";
    /**
    *实体属性：签到人名称.
    **/
    public static final String P_SIGN_UNAME = "signUname";
    /**
    *实体属性：签到人工号.
    **/
    public static final String P_SIGN_STAFFID = "signStaffid";
    /**
    *实体属性：签到时间.
    **/
    public static final String P_SIGN_TIME = "signTime";
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

    /**
     * 主键ids.
     */
    public static final String P_IDS = "ids";
}
