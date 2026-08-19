
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 评优评奖数据模型实体
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
@TableName("bbx_award")
public class Award extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*奖项名称.
	**/
	private String awardName;
	/**
	*审核方式;1->是;0->否.
	**/
	private Integer auditType;
	/**
	*审核人id.
	**/
	private String auditUid;
	/**
	*审核人名称.
	**/
	private String auditUname;
	/**
	*审核人工号.
	**/
	private String auditStaffid;
	/**
	*评奖日期.
	**/
	private Date awardDate;
	/**
	*展示开始时间.
	**/
	private Date displayStartTime;
	/**
	*展示结束时间.
	**/
	private Date displayEndTime;
	/**
	*奖项简介.
	**/
	private String remark;
	/**
	*展示顺序（1：按排序；2：按拼音）.
	**/
	private Integer sortRule;
	/**
	*状态（10：待提交；20：待审核；30：待展示；40：展示中；50展示结束）.
	**/
	private Integer status;
}