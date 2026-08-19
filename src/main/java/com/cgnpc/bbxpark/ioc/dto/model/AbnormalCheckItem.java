package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 15:15
 */
@Data
@ApiModel(value = "异常检查项")
public class AbnormalCheckItem {
    @ApiModelProperty(value = "检查项名称")
    private String itemName;
    @ApiModelProperty(value = "会议室名称")
    private String roomName;
    @ApiModelProperty(value = "异常时间(格式: YYYY-MM-DD HH:mm:ss)")
    private Date abnormalTime;
    @ApiModelProperty(value = "状态")
    private String status;

    public AbnormalCheckItem(String itemName,String roomName,Date abnormalTime,String status){
        this.itemName = itemName;
        this.roomName = roomName;
        this.abnormalTime = abnormalTime;
        this.status = status;
    }
}

