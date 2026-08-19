
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 会议预约文件分页参数模型
 * @author huangyongtao
 * @date 2024/12/23 15:48
 */
@Data
public class MeetingReserveFilePageParam extends CudPageDto implements Serializable {
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

    @ApiModelProperty(value = "是否需要打印;(0->是;1->否).")
    private Integer printing;

    @ApiModelProperty(value = "份数.")
    private Integer copies;

    @ApiModelProperty(value = "来源;（1->人工上传； 2->会议室音频文件）.")
    private Integer source;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

}
