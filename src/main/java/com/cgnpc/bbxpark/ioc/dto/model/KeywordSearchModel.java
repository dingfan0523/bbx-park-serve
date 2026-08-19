package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "关键词搜索模型")
public class KeywordSearchModel {
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间编码")
    private String sslcCode;
    @ApiModelProperty(value = "内容")
    private String content;

    public KeywordSearchModel(Long spaceId,String content){
        this.spaceId = spaceId;
        this.content = content;
    }

    public KeywordSearchModel(Long spaceId,String sslcCode,String content){
        this.spaceId = spaceId;
        this.content = content;
        this.sslcCode = sslcCode;
    }
}