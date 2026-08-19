package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:17
 */
@Data
@Builder
@ApiModel(value = "部门活跃度")
public class DepartmentActivity {
    @ApiModelProperty(value = "活跃线阈值")
    private Integer activityThreshold;
    @ApiModelProperty(value = "部门会议数量列表")
    private List<DepartmentMeeting> departmentMeetings;

    @Data
    @ApiModel(value = "部门会议统计")
    public static class DepartmentMeeting {
        @ApiModelProperty(value = "部门ID")
        private String deptId;
        @ApiModelProperty(value = "部门名称")
        private String deptName;
        @ApiModelProperty(value = "会议数量")
        private Integer count;
    }
}
