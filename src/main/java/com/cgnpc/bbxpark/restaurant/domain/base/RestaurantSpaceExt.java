
package com.cgnpc.bbxpark.restaurant.domain.base;

import com.cgnpc.bbxpark.restaurant.domain.RestaurantSpace;

import java.io.Serializable;


public class RestaurantSpaceExt extends RestaurantSpace implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3164371123281663034L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "restaurantSpace";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_restaurant_space";

    /**
    *数据库字段：id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：餐厅id.
    **/
    public static final String C_RESTAURANT_ID = "restaurant_id";
    /**
    *数据库字段：空间id.
    **/
    public static final String C_SPACE_ID = "space_id";
    /**
    *数据库字段：空间名称.
    **/
    public static final String C_SPACE_NAME = "space_name";
    /**
    *数据库字段：人流图片.
    **/
    public static final String C_FLOW_IMAGE_URL = "flow_image_url";
    /**
    *数据库字段：餐线图片.
    **/
    public static final String C_MEAL_LINE_IMAGE_URL = "meal_line_image_url";
    /**
    *数据库字段：租户id.
    **/
    public static final String C_TENANT_ID = "tenant_id";
    /**
    *数据库字段：乐观锁.
    **/
    public static final String C_REVISION = "revision";
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
    *实体属性：空间id.
    **/
    public static final String P_SPACE_ID = "spaceId";
    /**
    *实体属性：空间名称.
    **/
    public static final String P_SPACE_NAME = "spaceName";
    /**
    *实体属性：人流图片.
    **/
    public static final String P_FLOW_IMAGE_URL = "flowImageUrl";
    /**
    *实体属性：餐线图片.
    **/
    public static final String P_MEAL_LINE_IMAGE_URL = "mealLineImageUrl";
    /**
    *实体属性：租户id.
    **/
    public static final String P_TENANT_ID = "tenantId";
    /**
    *实体属性：乐观锁.
    **/
    public static final String P_REVISION = "revision";
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
