
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/***
 * @Description 会议预约文件入参数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:49
 */
@Data
public class MeetingReserveFileParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键id.")
    private Long id;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "会议预约id.")
    private Long reserveId;

    @Length(max = 255)
    @ApiModelProperty(value = "文件名称.")
    private String name;

    @Length(max = 255)
    @ApiModelProperty(value = "文件地址.")
    private String url;

    @ApiModelProperty(value = "是否需要打印;(0->是;1->否).")
    private Integer printing = 1;

    @ApiModelProperty(value = "份数.")
    private Integer copies;

    @ApiModelProperty(value = "来源;（1->人工上传； 2->会议室音频文件）.")
    private Integer source;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
