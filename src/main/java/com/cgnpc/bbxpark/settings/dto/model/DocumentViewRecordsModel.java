
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class DocumentViewRecordsModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4619000618457075036L;

    @ApiModelProperty(value = "主键标识.")
    private Long id;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "文档类型 userPrivacyAgree:用户隐私协议.")
    private String documentType;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "更新者.")
    private String updatorId;


}
