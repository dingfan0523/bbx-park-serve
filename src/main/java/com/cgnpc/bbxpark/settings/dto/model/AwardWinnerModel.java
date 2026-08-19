
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 获奖人信息业务数据模型
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
public class AwardWinnerModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "评优评奖表id.")
    private Long awardId;

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
