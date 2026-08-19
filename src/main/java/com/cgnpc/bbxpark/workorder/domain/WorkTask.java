
package com.cgnpc.bbxpark.workorder.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 工单任务数据模型实体
 * @author huangyongtao
 * @date 2025/11/4 16:21
 */
@Data
@TableName("bbx_work_task")
public class WorkTask extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*工单id.
	**/
	private Long workId;
	/**
	*业务类型:1->维保设备;2->巡检点；3->巡更路线；4->盘点设备；5->盘点材料；6->任务.
	**/
	private Integer businessType;
	/**
	*业务id.
	**/
	private Long businessId;
	/**
	*任务名称.
	**/
	private String name;
	/**
	*任务编码.
	**/
	private String code;
	/**
	*任务分类.
	**/
	private Integer category;
	/**
	*空间id.
	**/
	private Long spaceId;
	/**
	*空间全路径.
	**/
	private String spaceFullPath;
	/**
	*任务组.
	**/
	private Integer taskGroup;
	/**
	*任务要求.
	**/
	private String remark;
	/**
	*冗余字段1.
	**/
	private String redundancyOne;
	/**
	*冗余字段2.
	**/
	private String redundancyTwo;
	/**
	 *冗余时间字段1.
	 **/
	private Date redundancyTimeOne;

	/**
	*异常状态（1->是;0->否）.
	**/
	private Integer errorStatus;

	/**
	*异常说明.
	**/
	private String errorRemark;

	/**
	*异常空间id.
	**/
	private Long errorSpaceId;

	/**
	*异常空间全路径名称.
	**/
	private String errorSpaceName;

    /**
     *问题状态（1->是;0->否）.
     **/
    private Integer problemStatus;

    /**
     *路线距离（km）.
     **/
    private Double distance;
    /**
     *预计用时(分钟).
     **/
    private Double useTime;
}