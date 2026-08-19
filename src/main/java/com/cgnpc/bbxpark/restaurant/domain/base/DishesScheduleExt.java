
package com.cgnpc.bbxpark.restaurant.domain.base;

import com.cgnpc.bbxpark.restaurant.domain.DishesSchedule;

import java.io.Serializable;

public class DishesScheduleExt extends DishesSchedule implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4518286904142578272L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "bbxDishesSchedule";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_dishes_schedule";

    /**
    *数据库字段：id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：餐厅id.
    **/
    public static final String C_RESTAURANT_ID = "restaurant_id";
    /**
    *数据库字段：餐线id.
    **/
    public static final String C_MEAL_LINE_ID = "meal_line_id";
    /**
    *数据库字段：菜品id.
    **/
    public static final String C_DISHES_ID = "dishes_id";
    /**
    *数据库字段：菜品名称.
    **/
    public static final String C_NAME = "name";
    /**
    *数据库字段：菜品图片.
    **/
    public static final String C_IMAGE_URL = "image_url";
    /**
    *数据库字段：类别(字典).
    **/
    public static final String C_TYPE = "type";
    /**
    *数据库字段：用餐时间(字典).
    **/
    public static final String C_MEAL_TIME = "meal_time";
    /**
    *数据库字段：出品日期(date).
    **/
    public static final String C_PRODUCTION_DATE = "production_date";
    /**
    *数据库字段：星期（1,2,3,4,5,6,7->对应周一到周日）.
    **/
    public static final String C_WEEK = "week";
    /**
    *数据库字段：单价.
    **/
    public static final String C_PRICE = "price";
    /**
    *数据库字段：克重.
    **/
    public static final String C_WEIGHT = "weight";
    /**
    *数据库字段：辣度建议(0,1,2,3,4,5).
    **/
    public static final String C_PUNGENCY_DEGREE = "pungency_degree";
    /**
    *数据库字段：原料信息.
    **/
    public static final String C_INFORMATION = "information";
    /**
    *数据库字段：状态(1->上架;0->下架).
    **/
    public static final String C_STATUS = "status";
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
    *实体属性：餐厅id.
    **/
    public static final String P_RESTAURANT_ID = "restaurantId";
    /**
    *实体属性：餐线id.
    **/
    public static final String P_MEAL_LINE_ID = "mealLineId";
    /**
    *实体属性：菜品id.
    **/
    public static final String P_DISHES_ID = "dishesId";
    /**
    *实体属性：菜品名称.
    **/
    public static final String P_NAME = "name";
    /**
    *实体属性：菜品图片.
    **/
    public static final String P_IMAGE_URL = "imageUrl";
    /**
    *实体属性：类别(字典).
    **/
    public static final String P_TYPE = "type";
    /**
    *实体属性：用餐时间(字典).
    **/
    public static final String P_MEAL_TIME = "mealTime";
    /**
    *实体属性：出品日期(date).
    **/
    public static final String P_PRODUCTION_DATE = "productionDate";
    /**
    *实体属性：星期（1,2,3,4,5,6,7->对应周一到周日）.
    **/
    public static final String P_WEEK = "week";
    /**
    *实体属性：单价.
    **/
    public static final String P_PRICE = "price";
    /**
    *实体属性：克重.
    **/
    public static final String P_WEIGHT = "weight";
    /**
    *实体属性：辣度建议(0,1,2,3,4,5).
    **/
    public static final String P_PUNGENCY_DEGREE = "pungencyDegree";
    /**
    *实体属性：原料信息.
    **/
    public static final String P_INFORMATION = "information";
    /**
    *实体属性：状态(1->上架;0->下架).
    **/
    public static final String P_STATUS = "status";
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
