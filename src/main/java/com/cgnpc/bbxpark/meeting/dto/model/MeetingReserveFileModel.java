
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会议预约文件业务数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:25 
 */
@Data
public class MeetingReserveFileModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议预约id.")
    private Long reserveId;

    @ApiModelProperty(value = "文件名称.")
    private String name;

    @ApiModelProperty(value = "文件地址.")
    private String url;

    @ApiModelProperty(value = "是否需要打印;(1->是;0->否).")
    private Integer printing;

    @ApiModelProperty(value = "份数.")
    private Integer copies;

    @ApiModelProperty(value = "来源;（1->人工上传； 2->会议室音频文件）.")
    private Integer source = 1;

    @ApiModelProperty(value = "上传时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称")
    private String userName;

    @ApiModelProperty(value = "工号")
    private String staffid;

    @ApiModelProperty(value = "本人上传:true->是;false->否.")
    private Boolean oneself = Boolean.FALSE;
}
