
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会服统计入参
 * @author huangyongtao
 * @date 2025/2/10 10:33
 */
@Data
public class MeetingAttendantTaskCountParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /***时间类型（day：按天统计；week：按周统计； month：按月统计；quarter：按季度统计；year：按年度统计） */
    @ApiModelProperty(value = "时间类型（day：按天统计；week：按周统计； month：按月统计；quarter：按季度统计；year：按年度统计）")
    private String timeType;

    /*** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /*** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    private Date  endTime;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "用户id集合.")
    private List<String> userIdList;
}
