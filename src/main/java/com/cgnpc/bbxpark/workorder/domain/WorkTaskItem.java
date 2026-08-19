
package com.cgnpc.bbxpark.workorder.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 工单任务项数据模型实体
 * @author huangyongtao
 * @date 2025/11/4 16:22
 */
@Data
@TableName("bbx_work_task_item")
public class WorkTaskItem extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*工单id.
	**/
	private Long workId;
	/**
	*业务id.
	**/
	private Long businessId;
	/**
	*业务类型:1->维保项目;2->巡更点.
	**/
	private Integer businessType;
	/**
	*名称.
	**/
	private String name;
	/**
	*编码.
	**/
	private String code;
	/**
	*类型（10：安保；20：保洁；30：消控；40：环境；50：设备）.
	**/
	private Integer type;
	/**
	*方式（10：拍照；20：其他）.
	**/
	private Integer way;
	/**
	*位置id.
	**/
	private Long spaceId;
	/**
	*空间全路径.
	**/
	private String spaceFullPath;
	/**
	*巡更要求.
	**/
	private String remark;
	/**
	*重点检查(1->是;0->否).
	**/
	private Integer keyPoint;
	/**
	*启用状态;1->是;0->否.
	**/
	private Integer status;
	/**
	*任务组.
	**/
	private Integer taskGroup;

	/**
	 *异常状态（1->是;0->否）.
	 **/
	private Integer errorStatus;

	/**
	 *异常说明.
	 **/
	private String errorRemark;

    /**
     *问题状态（1->是;0->否）.
     **/
    private Integer problemStatus;
}