package com.cgnpc.bbxpark.message.dto.req;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MessageInfoPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4367486894784428827L;

    @ApiModelProperty(value = "消息标识.")
    private Long id;

    @ApiModelProperty(value = "类型标识.")
    private Long typeId;

    @ApiModelProperty(value = "消息标题.")
    private String title;

    @ApiModelProperty(value = "消息摘要.")
    private String summary;

    @ApiModelProperty(value = "消息内容.")
    private String content;

    @ApiModelProperty(value = "模版标识.")
    private Long templateId;


    @ApiModelProperty(value = "消息状态，0未发布1已发布2已撤销3已删除.")
    private Integer status;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间.")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间.")
    private Date endTime;
}
