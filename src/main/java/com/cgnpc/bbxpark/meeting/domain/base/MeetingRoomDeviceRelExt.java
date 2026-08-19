
package com.cgnpc.bbxpark.meeting.domain.base;

import com.cgnpc.bbxpark.meeting.domain.MeetingRoomDeviceRel;

import java.io.Serializable;
/***
 * @Description 会议室设备关联数据模型实体扩展
 * @author huangyongtao
 * @date 2024/8/23 15:19
 */
public class MeetingRoomDeviceRelExt extends MeetingRoomDeviceRel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "meetingRoomDeviceRel";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_meeting_room_device_rel";

    /**
    *数据库字段：主键id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：会议室id.
    **/
    public static final String C_ROOM_ID = "room_id";
    /**
    *数据库字段：设备id.
    **/
    public static final String C_DEVICE_ID = "device_Id";
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
    *实体属性：设备id.
    **/
    public static final String P_DEVICE_ID = "deviceId";
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
