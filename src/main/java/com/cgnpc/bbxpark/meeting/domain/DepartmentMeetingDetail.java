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
 * @desc 部门会议详情实体
 */
@Data
public class DepartmentMeetingDetail implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "使用部门")
    @ExcelProperty(value = "使用部门", index = 0)
    private String departmentName;
    @ApiModelProperty(value = "会议名称")
    @ExcelProperty(value = "会议室名称", index = 1)
    private String reserveName;
    @ApiModelProperty(value = "会议室名称")
    @ExcelProperty(value = "会议室", index = 2)
    private String roomName;     // 会议室名称
    @ApiModelProperty(value = "是否无效（0-无效，1-有效）")
    @ExcelIgnore
    private Integer inValidFlag; // 是否无效（0-无效，1-有效）
    @ExcelProperty(value = "是否为无效会议", index = 3)
    private String inValidFlagStr; // 是否无效（0-无效，1-有效）
    @ApiModelProperty(value = "会议时长（小时）")
    @ExcelProperty(value = "会议时长（小时）", index = 4)
    private BigDecimal duration;       // 会议时长（分钟）
    @ExcelIgnore
    private Long durations;       // 会议时长（分钟）
    @ApiModelProperty(value = "签到人数")
    @ExcelProperty(value = "签到人数", index = 5)
    private Integer signInCount; // 签到人数
    @ExcelIgnore
    private Long id;
}
