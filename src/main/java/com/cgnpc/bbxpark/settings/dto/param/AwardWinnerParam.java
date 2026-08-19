
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/***
 * @Description 获奖人信息入参数据模型
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
public class AwardWinnerParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class, message = "主键id不能为空！")
    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "评优评奖表id.")
    private Long awardId;

    @NotBlank(groups = InsertGroup.class, message = "获奖人姓名不能为空！")
    @ApiModelProperty(value = "获奖人姓名.")
    private String winnerName;

    @ApiModelProperty(value = "获奖人姓名拼音.")
    private String winnerNamePinyin;

    @ApiModelProperty(value = "获奖说明.")
    private String remark;

    @ApiModelProperty(value = "获奖人照片.")
    private String photoUrl;

    @ApiModelProperty(value = "顺序.")
    private Integer sortNumber;
}