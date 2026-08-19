package com.cgnpc.bbxpark.problemReport.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @create zhaoshuo
 * @time 2025/3/24
 * @desc 报事报修查询条件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemReportQueryParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4281780296969979158L;

    @ApiModelProperty(value = "报事报修id")
    private Long id;
    @ApiModelProperty(value = "问题描述")
    private String problemDesc;
    @ApiModelProperty(value = "问题类型：1报事报修")
    private Integer problemType;
    @ApiModelProperty(value = "空间位置id")
    private Long spaceId;
    @ApiModelProperty(value = "状态：1待指派；2处理中；3已关闭；4已完成")
    private Integer status;
    @ApiModelProperty(value = "确认结果1.转工单，2.无需处理")
    private Integer problemResultType;
    @ApiModelProperty(value = "上报开始时间")
    private Date reportStartTime;
    @ApiModelProperty(value = "上报结束时间")
    private Date reportEndTime;
}
