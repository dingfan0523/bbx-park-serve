
package com.cgnpc.bbxpark.space.dto.model;


import com.cgnpc.bbxpark.space.domain.TenantMember;

import java.io.Serializable;
import java.util.List;

public class TenantMemberExt extends TenantMember implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4665341282102665835L;

	/**
    *实体名.
    **/
	public static final String LOWER_ENTITY_NAME = "tenantMember";

    /**
    *表名.
    **/
    public static final String TABLE_NAME = "uic_tenant_member";

    /**
    *数据库字段：租户成员信息标识.
    **/
    public static final String C_ID = "id";
    /**
    *数据库字段：创建时间.
    **/
    public static final String C_CREATE_TIME = "create_time";
    /**
    *数据库字段：创建者.
    **/
    public static final String C_CREATOR_ID = "creator_id";
    /**
    *数据库字段：状态，0启用1禁用.
    **/
    public static final String C_STATUS = "status";
    /**
    *数据库字段：租户标识.
    **/
    public static final String C_TENANT_ID = "tenant_id";
    /**
    *数据库字段：修改时间.
    **/
    public static final String C_UPDATE_TIME = "update_time";
    /**
    *数据库字段：修改者.
    **/
    public static final String C_UPDATOR_ID = "updator_id";
    /**
    *数据库字段：用户标识.
    **/
    public static final String C_USER_ID = "user_id";

    /**
     * 数据库字段：身份.
     **/
    public static final String C_IDENTITY = "identity";

    /**
    *实体属性：租户成员信息标识.
    **/
    public static final String P_ID = "id";
    /**
    *实体属性：创建时间.
    **/
    public static final String P_CREATE_TIME = "createDate";
    /**
    *实体属性：创建者.
    **/
    public static final String P_CREATOR_ID = "createUserId";
    /**
    *实体属性：状态，0启用1禁用.
    **/
    public static final String P_STATUS = "status";
    /**
    *实体属性：租户标识.
    **/
    public static final String P_TENANT_ID = "tenantId";
    /**
    *实体属性：修改时间.
    **/
    public static final String P_UPDATE_TIME = "modifyDate";
    /**
    *实体属性：修改者.
    **/
    public static final String P_UPDATOR_ID = "modifyUserId";
    /**
    *实体属性：用户标识.
    **/
    public static final String P_USER_ID = "userId";

    /**
     * 主键ids.
     */
    public static final String P_IDS = "ids";

    /**
     * 身份.
     */
    public static final String P_IDENTITY = "identity";

    /**
     * <!-- 租户成员信息标识 -->.
     */
    private List<String> ids;

    /**
     * @return ids属性
     */
    public List<String> getIds() {
        return ids;
    }

     /**
     * @param ids 设置ids属性
     */
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
