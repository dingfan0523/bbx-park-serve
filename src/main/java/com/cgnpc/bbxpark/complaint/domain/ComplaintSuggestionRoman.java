
package com.cgnpc.bbxpark.complaint.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 投诉建议流转表;数据模型实体
 * @author huangyongtao
 * @date 2024/7/12 14:05
 */
@Data
@TableName("bbx_complaint_suggestion_roman")
public class ComplaintSuggestionRoman extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	 *父id.
	 **/
	private Long pid;

	/**
	*投诉建议id.
	**/
	private Long complaintSuggestionId;
	/**
	*流转内容.
	**/
	private String romanRemark;
	/**
	*流转人工号.
	**/
	private String romanStaffid;
	/**
	*流转时间.
	**/
	private Date romanTime;
	/**
	*流转类型;reply：回复；audit：审核.
	**/
	private String romanType;
	/**
	*流转人id.
	**/
	private String romanUid;
	/**
	*流转人名称.
	**/
	private String romanUname;

}
