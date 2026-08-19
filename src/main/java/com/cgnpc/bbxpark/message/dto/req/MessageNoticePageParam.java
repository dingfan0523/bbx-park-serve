package com.cgnpc.bbxpark.message.dto.req;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MessageNoticePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3785205794009100185L;


    @ApiModelProperty(value = "通知公告类型，0通知1公告.")
    private Integer type;

    @ApiModelProperty(value = "消息类型")
    private Integer typeId;

    @ApiModelProperty(value = "优先级，0低1中2高.")
    private Integer priority;

    @ApiModelProperty(value = "发布级别，0系统级1组织级2部门级3租户级4应用级.")
    private Integer level;

    @ApiModelProperty(value = "有效状态，0有效1无效2已过期.")
    private Integer effectiveStatus;

    @ApiModelProperty(value = "标题.")
    private String title;

    @ApiModelProperty(value = "消息状态，0未发布1已发布2已撤销3已删除")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间.")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间.")
    private Date endTime;
}
