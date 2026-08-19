
package com.cgnpc.bbxpark.meeting.domain.base;


import com.cgnpc.bbxpark.meeting.domain.MeetingRoom;

import java.io.Serializable;
/***
 * @Description 会议室数据模型实体扩展
 * @author huangyongtao
 * @date 2024/8/23 15:19
 */
public class MeetingRoomExt extends MeetingRoom implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "meetingRoom";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_meeting_room";

    /**
    *数据库字段：主键id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：会议室名称.
    **/
    public static final String C_ROOM_NAME = "room_name";
    /**
    *数据库字段：会议室容量.
    **/
    public static final String C_ROOM_VOLUME = "room_volume";
    /**
    *数据库字段：空间位置id.
    **/
    public static final String C_SPACE_ID = "space_id";
    /**
    *数据库字段：第三方会议室id.
    **/
    public static final String C_THIRD_ROOM_ID = "third_room_id";
    /**
    *数据库字段：会议室备注.
    **/
    public static final String C_ROOM_REMARK = "room_remark";
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
    *实体属性：会议室名称.
    **/
    public static final String P_ROOM_NAME = "roomName";
    /**
    *实体属性：会议室容量.
    **/
    public static final String P_ROOM_VOLUME = "roomVolume";
    /**
    *实体属性：空间位置id.
    **/
    public static final String P_SPACE_ID = "spaceId";
    /**
    *实体属性：第三方会议室id.
    **/
    public static final String P_THIRD_ROOM_ID = "thirdRoomId";
    /**
    *实体属性：会议室备注.
    **/
    public static final String P_ROOM_REMARK = "roomRemark";
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
