
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会服统计入参
 * @author huangyongtao
 * @date 2025/2/10 10:33
 */
@Data
public class MeetingAttendantTaskCountDetailPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /*** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /*** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "用户id集合.")
    private String userId;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @ApiModelProperty(value = "会议名称.")
    private String reserveName;

    @ApiModelProperty(value = "用户id集合.")
    private List<String> userIdList;

    @ApiModelProperty(value = "排序方式（asc：正序； desc:倒序）")
    private String sortOrder;

}
