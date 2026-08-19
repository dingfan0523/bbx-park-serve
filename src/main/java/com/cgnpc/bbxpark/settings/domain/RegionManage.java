
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 区域管理员管理数据模型实体
 * @date 2025/3/11 11:10
 */
@Data
@TableName("bbx_region_manage")
public class RegionManage extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     *区域管理员id.
     **/
    private String regionUid;

	/**
	*区域管理员名称.
	**/
	private String regionUname;
	/**
	*区域管理员工号.
	**/
	private String regionStaffid;

}
