package com.cgnpc.bbxpark.meeting.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @create zhaoshuo
 * @time 4525/2/13
 * @desc 部门会议详情统计实体
 */
@Data
public class DepartmentMeetingStats implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "部门ID")
    @ExcelIgnore
    private String departmentId;      // 部门ID
    @ApiModelProperty(value = "使用部门")
    @ExcelProperty(value = "使用部门", index = 0)
    private String departmentName;  // 部门名称
    @ApiModelProperty(value = "会议总数")
    @ExcelProperty(value = "会议数(含无效)", index = 1)
    private Integer totalMeetings;  // 会议总数
    @ApiModelProperty(value = "无效会议数")
    @ExcelProperty(value = "无效会议数", index = 2)
    private Integer invalidMeetings;// 无效会议数
    @ApiModelProperty(value = "总时长分钟")
    @ExcelProperty(value = "会议时长(小时)", index = 3)
    private BigDecimal totalDuration;     // 总时长小时
    @ExcelIgnore
    private Long totalDurations;     // 总时长分钟
    @ApiModelProperty(value = "签到人次")
    @ExcelProperty(value = "签到人次", index = 4)
    private Integer signInCount;    // 签到人次
}
