
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@TableName("bbx_click_volume")
@Data
public class ClickVolume extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3787977634685793904L;

	/**
	*租户id.
	**/
	private Long tenantId;
	/**
	*菜单编码.
	**/
	private String menuCode;
	/**
	*用户id.
	**/
	private String userId;
	/**
	*访问总计.
	**/
	private Integer visitCount = 1;

}
