
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/***
 * @Description 会议预约座位入参数据模型
 * @author huangyongtao
 * @date 2024/12/24 14:10
 */
@Data
public class MeetingReserveSeatParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键id.")
    private Long id;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "会议预约id.")
    private Long reserveId;

    @ApiModelProperty(value = "座位类型;(1->回型桌；2->培训桌；3->讨论桌).")
    private Integer seatType;

    @Length(max = 100)
    @ApiModelProperty(value = "座位名称.")
    private String seatName;

    @Length(max = 100)
    @ApiModelProperty(value = "座位人员名称.")
    private String personName;

    @ApiModelProperty(value = "座位分组.")
    private String seatGroup;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
