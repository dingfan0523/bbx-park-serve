
package com.cgnpc.bbxpark.complaint.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 投诉建议主表;数据模型实体
 * @author huangyongtao
 * @date 2024/7/12 14:02
 */
@Data
@TableName("bbx_complaint_suggestion")
public class ComplaintSuggestion extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*分配人工号.
	**/
	private String assignmentStaffid;
	/**
	*分配时间.
	**/
	private Date assignmentTime;
	/**
	*分配人id.
	**/
	private String assignmentUid;
	/**
	*分配人名称.
	**/
	private String assignmentUname;
	/**
	*审核人内容.
	**/
	private String auditRemark;
	/**
	*审核人工号.
	**/
	private String auditStaffid;
	/**
	*审核时间.
	**/
	private Date auditTime;
	/**
	*审核人id.
	**/
	private String auditUid;
	/**
	*审核名称.
	**/
	private String auditUname;
	/**
	*评价内容.
	**/
	private String commentRemark;
	/**
	*评价分数.
	**/
	private Long commentScore;
	/**
	*评价时间.
	**/
	private Date commentTime;
	/**
	*完成时间.
	**/
	private Date completeTime;
	/**
	*内容.
	**/
	private String content;
	/**
	*图片地址.
	**/
	private String imageUrl;
	/**
	*回复时间.
	**/
	private Date replyDate;
	/**
	*回复内容.
	**/
	private String replyRemark;
	/**
	*回复人工号.
	**/
	private String replyStaffid;
	/**
	*回复人id.
	**/
	private String replyUid;
	/**
	*回复人名称.
	**/
	private String replyUname;
	/**
	*状态;10：待回复；20：已分配（待处理）；30：待审核；40：审核驳回；50：已完成；60：已评价
	**/
	private Integer status;
	/**
	*提交人工号.
	**/
	private String submitStaffid;
	/**
	*提交时间.
	**/
	private Date submitTime;
	/**
	*提交人id.
	**/
	private String submitUid;
	/**
	*提交人名称.
	**/
	private String submitUname;
	/**
	*标题.
	**/
	private String title;
	/**
	*类型;complaint：投诉；suggestion：建议.
	**/
	private String type;
    /**
     *部门id.
     **/
    private String departmentId;
    /**
     *部门名称.
     **/
    private String departmentName;


}
