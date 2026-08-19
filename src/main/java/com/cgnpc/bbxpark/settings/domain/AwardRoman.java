
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 评优评奖流程数据模型实体
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
@TableName("bbx_award_roman")
public class AwardRoman extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*评优评奖表id.
	**/
	private Long awardId;
	/**
	*状态（1->提交；2->撤回；3->审批；4->信息展示；5->展示结束）.
	**/
	private Integer status;
	/**
	*操作人id.
	**/
	private String operatorId;
	/**
	*操作人名称.
	**/
	private String operatorName;
	/**
	*操作人工号.
	**/
	private String operatorStaffid;
	/**
	*操作结果;1->通过;0->不通过.
	**/
	private Integer operatorResult;
	/**
	*操作.
	**/
	private String operator;
	/**
	*说明备注.
	**/
	private String remark;

}
