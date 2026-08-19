
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/***
 * @Description 会服入参数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:29
 */
@Data
public class MeetingServiceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "会服名称")
    @NotNull(message = "会服名称不能为空",groups = {InsertGroup.class, UpdateGroup.class})
    private String name;
    @ApiModelProperty(value = "标准")
    @Length(max = 200,groups = {InsertGroup.class, UpdateGroup.class})
    private String standard;
    @ApiModelProperty(value = "提醒")
    @Length(max = 200,groups = {InsertGroup.class, UpdateGroup.class})
    private String warn;
    @ApiModelProperty(value = "说明")
    @Length(max = 200,groups = {InsertGroup.class, UpdateGroup.class})
    private String instructions;
    @ApiModelProperty(value = "单价")
    @Min(value = 0)
    @Max(value = 10000)
    private BigDecimal price;
}
