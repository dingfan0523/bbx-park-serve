package com.cgnpc.bbxpark.message.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MessageNoticeListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3639247759098421870L;

    @ApiModelProperty(value = "通知公告标识.")
    private Long id;

    @ApiModelProperty(value = "类型标识.")
    private Long typeId;

    @ApiModelProperty(value = "消息标识.")
    private Long msgId;

    @ApiModelProperty(value = "通知公告类型，0通知1公告.")
    private Integer type;

    @ApiModelProperty(value = "优先级，0低1中2高.")
    private Integer priority;

    @ApiModelProperty(value = "发布级别，0系统级1组织级2部门级3租户级4应用级.")
    private Integer level;

    @ApiModelProperty(value = "有效状态，0有效1无效2已过期.")
    private Integer effectiveStatus;

    @ApiModelProperty(value = "有效日期.")
    private Date effectiveDate;

    @ApiModelProperty(value = "通知公告标识集合.")
    private List<Long> ids;


    @ApiModelProperty(value = "标题.")
    private String title;


    @ApiModelProperty(value = "消息状态，0未发布1已发布2已撤销")
    private Integer status;
}
