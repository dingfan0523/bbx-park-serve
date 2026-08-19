
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@TableName("bbx_burial_point")
@Data
public class BurialPoint extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3307978118957015864L;

	/**
	*租户id.
	**/
	private Long tenantId;
	/**
	*埋点功能编码;例：投诉访问，投诉恢复.
	**/
	private String burialPointFunctionCode;
	/**
	*事件类型;例：点击事件、浏览事件.
	**/
	private String eventType;
	/**
	*触发时间.
	**/
	private Date triggerTime;
	/**
	*停留时长.
	**/
	private Integer durationStay;
	/**
	*用户id.
	**/
	private String userId;


}
