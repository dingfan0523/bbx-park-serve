package com.cgnpc.bbxpark.meeting.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @create zhaoshuo
 * @time 2025/2/10
 * @desc 部门会议统计实体
 */
@Data
public class DepartmentMeetingSum implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 总时间
     */
    @ApiModelProperty(value = "楼内会议总时间")
    private BigDecimal totalDuration;
    private Integer totalDurations;

    /**
     * 总场次
     */
    @ApiModelProperty(value = "楼内会议总场次")
    private Integer totalMeetings;

    /**
     * 是否无效:0->是;1->否
     */
    @ApiModelProperty(value = "是否无效:0->是;1->否")
    private Integer inValidFlag;

    /**
     * 人员总数
     */
    @ApiModelProperty(value = "人员总数")
    private Integer memberSumNum;

    /**
     * 部门总数
     */
    @ApiModelProperty(value = "部门总数")
    private Integer deptSumNum;

    /**
     * 部门平均会议场次
     */
    @ApiModelProperty(value = "部门平均会议场次")
    private BigDecimal avgDepartmentMeeting;
    /**
     * 部门平均会议场次环比增长
     */
    @ApiModelProperty(value = "部门平均会议场次环比增长")
    private String avgMeetingGrowthRate;

    /**
     * 部门平均会议时长
     */
    @ApiModelProperty(value = "部门平均会议时长")
    private BigDecimal avgDuration;
    /**
     * 部门平均会议时长环比增长
     */
    @ApiModelProperty(value = "部门平均会议时长环比增长")
    private String avgDurationGrowthRate;
    /**
     * 无效占比
     */
    @ApiModelProperty(value = "无效占比")
    private String invalid;
    /**
     * 会议室数量
     */
    @ApiModelProperty(value = "会议室数量")
    private Integer meetingRoomCount;
    /**
     * 会议室利用率
     */
    @ApiModelProperty(value = "会议室利用率")
    private String roomUseRatio;

    /**
     * 会议室利用率环比
     */
    @ApiModelProperty(value = "会议室利用率")
    private String roomUseRatioGrowthRate;
    /**
     * 会议室平均会议场次
     */
    @ApiModelProperty(value = "会议室平均会议场次")
    private BigDecimal roomAvgMeetingCount;
}
