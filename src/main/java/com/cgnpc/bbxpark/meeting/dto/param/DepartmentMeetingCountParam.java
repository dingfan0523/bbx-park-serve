package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/13
 * @desc 部门会议统计查询条件
 */
@Data
public class DepartmentMeetingCountParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;
//    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
//    private String endTime;
    @ApiModelProperty(value = "排序字段")
    private String sortBy;
    @ApiModelProperty(value = "排序方式")
    private String sortOrder;
    @ApiModelProperty(value = "部门id")
    private String departmentId;
    @ApiModelProperty(value = "部门名称模糊查询")
    private String departmentName;
    private List<String> departmentIds;

    @ApiModelProperty(value = "会议名字模糊查询")
    private String reserveNameLike;
    @ApiModelProperty(value = "是否为无效会议")
    private Integer inValidFlag;

    private Long tenantId;
}
