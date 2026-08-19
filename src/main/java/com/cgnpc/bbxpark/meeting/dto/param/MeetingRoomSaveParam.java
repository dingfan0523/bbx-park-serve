package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/9/26 15:53
 */
@Data
public class MeetingRoomSaveParam implements Serializable {
    @ApiModelProperty(value = "会议室名称.")
    @NotEmpty(message = "会议室名称不能为空",groups = InsertGroup.class)
    @Length(max = 50,message = "会议室名称不能超过50个字符",groups = InsertGroup.class)
    private String roomName;
    @ApiModelProperty(value = "位置id")
    private Long spaceId;
    @ApiModelProperty(value = "容量")
    private Integer roomVolume;
    @ApiModelProperty(value = "来源(add->本系统内新增;sync->集团会议系统同步)")
    private String source;
}
