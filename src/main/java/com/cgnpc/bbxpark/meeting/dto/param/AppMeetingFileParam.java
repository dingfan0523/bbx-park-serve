package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 移动端-会议文件入参数据模型
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 16:41
 */
@Data
public class AppMeetingFileParam implements Serializable {
    @ApiModelProperty(value = "会议id.")
    @NotNull(groups = InsertGroup.class,message = "会议id不能为空")
    private Long reserveId;
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "来源:1->人工上传;2->会议室音频文件")
    private Integer source;
    @ApiModelProperty(value = "文件地址")
    @NotNull(groups = InsertGroup.class,message = "文件地址不能为空")
    private String url;
}
