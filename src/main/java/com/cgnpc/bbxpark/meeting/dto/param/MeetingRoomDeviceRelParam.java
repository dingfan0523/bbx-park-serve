
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
/***
 * @Description 会议室设备关联入参数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:30
 */
@Data
public class MeetingRoomDeviceRelParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @Length(max = 255)
    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

}
