package com.cgnpc.bbxpark.meeting.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@ApiModel(value = "数据导入返回信息")
@Data
@Builder
public class ImportReturnModel {
    @ApiModelProperty(value = "是否成功:true->成功;false->失败")
    private Boolean success;
    @ApiModelProperty(value = "提示信息")
    private List<String> messageList;
}
