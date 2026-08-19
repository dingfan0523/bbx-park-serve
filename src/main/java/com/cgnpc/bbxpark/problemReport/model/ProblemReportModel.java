package com.cgnpc.bbxpark.problemReport.model;

import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/3/24
 * @desc 报事报修实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemReportModel  implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    @ApiModelProperty(value = "报事报修主键id")
    private Long id;

    /**
     * 问题需求描述
     */
    @ApiModelProperty(value = "问题需求描述")
    private String problemDesc;

    /**
     * 问题类型：1报事报修
     */
    @ApiModelProperty(value = "问题类型1报事报修")
    private Integer problemType;


    /**
     * 问题状态（1.新产生，2.已查看，3.已确认）
     */
    @ApiModelProperty(value = "问题状态（1.新产生，2.已查看，3.已确认）")
    private Integer status;

    /**
     * 空间位置id
     */
    @ApiModelProperty(value = "空间位置id")
    private Long spaceId;

    /**
     * 空间位置名称
     */
    @ApiModelProperty(value = "空间位置名称")
    private String spaceName;

    /**
     * 问题联系人
     */
    @ApiModelProperty(value = "问题联系人")
    private String problemContact;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "问题联系人电话")
    private String contactPhone;

    /**
     * 问题确认结果(1.转工单，2.无需处理)
     */
    @ApiModelProperty(value = "问题确认结果(1.转工单，2.无需处理)")
    private Integer problemResultType;

    /**
     * 评分
     */
    @ApiModelProperty(value = "评分")
    private Integer score;

    /**
     * 工作评价
     */
    @ApiModelProperty(value = "工作评价")
    private String evaluation;

    /**
     * 评价人
     */
    @ApiModelProperty(value = "评价人")
    private String evaluationUname;

    /**
     * 评价人工号
     */
    @ApiModelProperty(value = "评价人工号")
    private String evaluationUstaffid;

    /**
     * 评价时间
     */
    private Date evaluationTime;

    @ApiModelProperty(value = "上报人名称")
    private String createBy;

    @ApiModelProperty(value = "上报人工号")
    private String creatorId;

    @ApiModelProperty(value = "上报时间")
    private Date createTime;

    @ApiModelProperty(value = "图片列表")
    private List<FileModel> fileList;

    @ApiModelProperty(value = "设备列表")
    private List<ProblemDeviceModel> problemDeviceList;

    @ApiModelProperty(value = "处理记录列表")
    private List<ProblemHandleRecordModel> problemHandleRecordList;

    @ApiModelProperty(value = "工单id")
    private Long workOrderId;

    @ApiModelProperty(value = "是否有处理权限true-是 false-不是")
    private Boolean flag;
}
