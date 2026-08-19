
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/***
 * @Description 会服数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:13
 */
@Data
public class MeetingServiceModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "通用会服(1->是;0->否)")
    private Integer common;
    @ApiModelProperty(value = "会服名称")
    private String name;
    @ApiModelProperty(value = "标准")
    private String standard;
    @ApiModelProperty(value = "提醒")
    private String warn;
    @ApiModelProperty(value = "说明")
    private String instructions;
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    @ApiModelProperty(value = "是否选中(1->是;0->否)")
    private Integer selected;
    @ApiModelProperty(value = "是否可用(1->是;0->否)")
    private Integer valid = 1;
}
