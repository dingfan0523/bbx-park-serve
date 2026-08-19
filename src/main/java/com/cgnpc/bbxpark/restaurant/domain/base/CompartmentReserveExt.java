
package com.cgnpc.bbxpark.restaurant.domain.base;

import com.cgnpc.bbxpark.restaurant.domain.CompartmentReserve;

import java.io.Serializable;
/***
 * @Description 包间预定数据模型实体扩展
 * @author huangyongtao
 * @date 2024/7/30 14:39
 */
public class CompartmentReserveExt extends CompartmentReserve implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "compartmentReserve";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_compartment_reserve";

    /**
    *数据库字段：id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：包间id.
    **/
    public static final String C_COMPARTMENT_ID = "compartment_id";
    /**
    *数据库字段：套餐id.
    **/
    public static final String C_COMBO_ID = "combo_id";
    /**
    *数据库字段：包间名称.
    **/
    public static final String C_COMPARTMENT_NAME = "compartment_name";
    /**
    *数据库字段：预订人id.
    **/
    public static final String C_SUBSCRIBER_ID = "subscriber_id";
    /**
    *数据库字段：预订人名称.
    **/
    public static final String C_SUBSCRIBER_NAME = "subscriber_name";
    /**
    *数据库字段：预订人工号.
    **/
    public static final String C_SUBSCRIBER_STAFFID = "subscriber_staffid";
    /**
    *数据库字段：套餐名称.
    **/
    public static final String C_COMBO_NAME = "combo_name";
    /**
    *数据库字段：预定日期.
    **/
    public static final String C_RESERVE_DATE = "reserve_date";
    /**
    *数据库字段：预定开始时间.
    **/
    public static final String C_RESERVE_START_TIME = "reserve_start_time";
    /**
    *数据库字段：预定结束时间.
    **/
    public static final String C_RESERVE_END_TIME = "reserve_end_time";
    /**
    *数据库字段：联系方式.
    **/
    public static final String C_PHONE = "phone";
    /**
    *数据库字段：预定状态.
    **/
    public static final String C_RESERVE_STATUS = "reserve_status";
    /**
    *数据库字段：预定来源.
    **/
    public static final String C_RESERVE_SOURCE = "reserve_source";
    /**
    *数据库字段：到店时间.
    **/
    public static final String C_USE_TIME = "use_time";
    /**
    *数据库字段：取消时间.
    **/
    public static final String C_CANCEL_TIME = "cancel_time";
    /**
    *数据库字段：取消原因.
    **/
    public static final String C_CANCEL_REASON = "cancel_reason";
    /**
    *数据库字段：取消备注.
    **/
    public static final String C_CANCEL_REMARK = "cancel_remark";
    /**
    *数据库字段：租户id.
    **/
    public static final String C_TENANT_ID = "tenant_id";
    /**
    *数据库字段：删除状态(1->未删;0->已删).
    **/
    public static final String C_DELETED = "deleted";
    /**
    *数据库字段：创建人id.
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
    *实体属性：id.
    **/
    public static final String P_ID = "id";
    /**
    *实体属性：包间id.
    **/
    public static final String P_COMPARTMENT_ID = "compartmentId";
    /**
    *实体属性：套餐id.
    **/
    public static final String P_COMBO_ID = "comboId";
    /**
    *实体属性：包间名称.
    **/
    public static final String P_COMPARTMENT_NAME = "compartmentName";
    /**
    *实体属性：预订人id.
    **/
    public static final String P_SUBSCRIBER_ID = "subscriberId";
    /**
    *实体属性：预订人名称.
    **/
    public static final String P_SUBSCRIBER_NAME = "subscriberName";
    /**
    *实体属性：预订人工号.
    **/
    public static final String P_SUBSCRIBER_STAFFID = "subscriberStaffid";
    /**
    *实体属性：套餐名称.
    **/
    public static final String P_COMBO_NAME = "comboName";
    /**
    *实体属性：预定日期.
    **/
    public static final String P_RESERVE_DATE = "reserveDate";
    /**
    *实体属性：预定开始时间.
    **/
    public static final String P_RESERVE_START_TIME = "reserveStartTime";
    /**
    *实体属性：预定结束时间.
    **/
    public static final String P_RESERVE_END_TIME = "reserveEndTime";
    /**
    *实体属性：联系方式.
    **/
    public static final String P_PHONE = "phone";
    /**
    *实体属性：预定状态.
    **/
    public static final String P_RESERVE_STATUS = "reserveStatus";
    /**
    *实体属性：预定来源.
    **/
    public static final String P_RESERVE_SOURCE = "reserveSource";
    /**
    *实体属性：到店时间.
    **/
    public static final String P_USE_TIME = "useTime";
    /**
    *实体属性：取消时间.
    **/
    public static final String P_CANCEL_TIME = "cancelTime";
    /**
    *实体属性：取消原因.
    **/
    public static final String P_CANCEL_REASON = "cancelReason";
    /**
    *实体属性：取消备注.
    **/
    public static final String P_CANCEL_REMARK = "cancelRemark";
    /**
    *实体属性：租户id.
    **/
    public static final String P_TENANT_ID = "tenantId";
    /**
    *实体属性：删除状态(1->未删;0->已删).
    **/
    public static final String P_DELETED = "deleted";
    /**
    *实体属性：创建人id.
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
