
package com.cgnpc.bbxpark.restaurant.domain.base;

import com.cgnpc.bbxpark.restaurant.domain.DishesGallery;

import java.io.Serializable;

public class DishesGalleryExt extends DishesGallery implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3129639956233804510L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "bbxDishesGallery";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "bbx_dishes_gallery";

    /**
    *数据库字段：id.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：菜品名称.
    **/
    public static final String C_NAME = "name";
    /**
    *数据库字段：图片url.
    **/
    public static final String C_IMAGE_URL = "image_url";
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
    *数据库字段：满意度.
    **/
    public static final String C_SATISFACTION = "satisfaction";
    /**
    *数据库字段：租户id.
    **/
    public static final String C_TENANT_ID = "tenant_id";
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
    *实体属性：菜品名称.
    **/
    public static final String P_NAME = "name";
    /**
    *实体属性：图片url.
    **/
    public static final String P_IMAGE_URL = "imageUrl";
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
    *实体属性：满意度.
    **/
    public static final String P_SATISFACTION = "satisfaction";
    /**
    *实体属性：租户id.
    **/
    public static final String P_TENANT_ID = "tenantId";
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
