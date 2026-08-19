package com.cgnpc.bbxpark.problemReport.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @create zhaoshuo
 * @time 2025/3/25
 * @desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemHandleRecordModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    @ApiModelProperty(value = "报事报修id")
    private Long problemId;

    /**
     * 环节（1新产生，2已查看，3已确认）
     */
    @ApiModelProperty(value = "环节（1新产生，2已查看，3已确认）")
    private Integer link;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operator;

    /**
     * 操作人工号
     */
    @ApiModelProperty(value = "操作人工号")
    private String operatorStaffid;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;

    /**
     * 确认结果(1.转工单，2.无需处理)
     */
    @ApiModelProperty(value = "确认结果(1.转工单，2.无需处理)")
    private Integer resultType;

    /**
     * 分配处理人姓名
     */
    @ApiModelProperty(value = "分配处理人姓名")
    private String handleUname;

    /**
     * 处理人工号
     */
    @ApiModelProperty(value = "处理人工号")
    private String handleStaffid;

    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    private String cause;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String descr;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 处理人联系方式
     */
    @ApiModelProperty(value = "处理人联系方式")
    private String handleUmobile;
}
