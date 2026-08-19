
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 获奖人信息列表参数模型
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
public class AwardWinnerListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评优评奖表id.")
    private Long awardId;

    @ApiModelProperty(value = "评优评奖表id集合.")
    private List<Long> awardIds;

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

    @ApiModelProperty(value = "展示顺序（1：按排序；2：按拼音）.")
    private Integer sortRule;
}