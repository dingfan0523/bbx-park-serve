
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 会议预约座位分页参数模型
 * @author huangyongtao
 * @date 2024/12/24 14:10
 */
@Data
public class MeetingReserveSeatPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议预约id.")
    private Long reserveId;

    @ApiModelProperty(value = "座位类型;(1->回型桌；2->培训桌；3->讨论桌).")
    private Integer seatType;

    @ApiModelProperty(value = "座位名称.")
    private String seatName;

    @ApiModelProperty(value = "座位人员名称.")
    private String personName;

    @ApiModelProperty(value = "座位分组.")
    private String seatGroup;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

}
