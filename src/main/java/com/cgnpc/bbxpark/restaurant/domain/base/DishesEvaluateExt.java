
package com.cgnpc.bbxpark.restaurant.domain.base;

import com.cgnpc.bbxpark.restaurant.domain.DishesEvaluate;

import java.io.Serializable;

public class DishesEvaluateExt extends DishesEvaluate implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4611036326410763244L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "bbxDishesEvaluate";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_dishes_evaluate";

    /**
    *数据库字段：id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：菜品id.
    **/
    public static final String C_DISHES_ID = "dishes_id";
    /**
    *数据库字段：菜品名称.
    **/
    public static final String C_NAME = "name";
    /**
    *数据库字段：满意度.
    **/
    public static final String C_SATISFACTION = "satisfaction";
    /**
    *数据库字段：味道.
    **/
    public static final String C_TASTE = "taste";
    /**
    *数据库字段：评价人id.
    **/
    public static final String C_APPRAISER_ID = "appraiser_id";
    /**
    *数据库字段：评价人名称.
    **/
    public static final String C_APPRAISER_NAME = "appraiser_name";
    /**
    *数据库字段：评价人工号.
    **/
    public static final String C_APPRAISER_STAFFID = "appraiser_staffid";
    /**
    *数据库字段：匿名状态(0->未匿名;1->匿名).
    **/
    public static final String C_ANONYMITY_STATUS = "anonymity_status";
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
    *实体属性：菜品id.
    **/
    public static final String P_DISHES_ID = "dishesId";
    /**
    *实体属性：菜品名称.
    **/
    public static final String P_NAME = "name";
    /**
    *实体属性：满意度.
    **/
    public static final String P_SATISFACTION = "satisfaction";
    /**
    *实体属性：味道.
    **/
    public static final String P_TASTE = "taste";
    /**
    *实体属性：评价人id.
    **/
    public static final String P_APPRAISER_ID = "appraiserId";
    /**
    *实体属性：评价人名称.
    **/
    public static final String P_APPRAISER_NAME = "appraiserName";
    /**
    *实体属性：评价人工号.
    **/
    public static final String P_APPRAISER_STAFFID = "appraiserStaffid";
    /**
    *实体属性：匿名状态(0->未匿名;1->匿名).
    **/
    public static final String P_ANONYMITY_STATUS = "anonymityStatus";
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
