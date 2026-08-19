
package com.cgnpc.bbxpark.restaurant.domain.base;

import com.cgnpc.bbxpark.restaurant.domain.Restaurant;

import java.io.Serializable;


public class RestaurantExt extends Restaurant implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4772569859076105707L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "restaurant";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_restaurant";

    /**
    *数据库字段：id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：餐厅名称.
    **/
    public static final String C_NAME = "name";
    /**
    *数据库字段：餐厅图片.
    **/
    public static final String C_IMAGE_URL = "image_url";
    /**
    *数据库字段：容纳人数.
    **/
    public static final String C_CAPACITY = "capacity";
    /**
    *数据库字段：餐厅标签(jsonArray格式).
    **/
    public static final String C_TAGS = "tags";
    /**
    *数据库字段：餐厅电话.
    **/
    public static final String C_TELPHONE = "telphone";
    /**
    *数据库字段：介绍.
    **/
    public static final String C_INTRODUCE = "introduce";
    /**
    *数据库字段：通知.
    **/
    public static final String C_NOTIFICATION = "notification";
    /**
    *数据库字段：状态(1->启用;0->禁用).
    **/
    public static final String C_STATUS = "status";
    /**
    *数据库字段：租户id.
    **/
    public static final String C_TENANT_ID = "tenant_id";
    /**
    *数据库字段：乐观锁.
    **/
    public static final String C_REVISION = "revision";
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
    *实体属性：餐厅名称.
    **/
    public static final String P_NAME = "name";
    /**
    *实体属性：餐厅图片.
    **/
    public static final String P_IMAGE_URL = "imageUrl";
    /**
    *实体属性：容纳人数.
    **/
    public static final String P_CAPACITY = "capacity";
    /**
    *实体属性：餐厅标签(jsonArray格式).
    **/
    public static final String P_TAGS = "tags";
    /**
    *实体属性：餐厅电话.
    **/
    public static final String P_TELPHONE = "telphone";
    /**
    *实体属性：介绍.
    **/
    public static final String P_INTRODUCE = "introduce";
    /**
    *实体属性：通知.
    **/
    public static final String P_NOTIFICATION = "notification";
    /**
    *实体属性：状态(1->启用;0->禁用).
    **/
    public static final String P_STATUS = "status";
    /**
    *实体属性：租户id.
    **/
    public static final String P_TENANT_ID = "tenantId";
    /**
    *实体属性：乐观锁.
    **/
    public static final String P_REVISION = "revision";
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
