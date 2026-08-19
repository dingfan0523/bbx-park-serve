
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/***
 * @Description 会议预约签到入参数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:31
 */
@Data
public class MeetingSignParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会议id.")
    private Long id;
    @ApiModelProperty(value = "签到人id集合")
    @Size(max = 10, message = "单次最多代签10人")
    private List<String> userIdList;
    @ApiModelProperty(value = "钉钉用户id集合(代签时使用)")
    private List<String> thirdUserIdList;
}
