
package com.cgnpc.bbxpark.invitation.dto.model;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 邀约导出模型
 * @author huangyongtao
 * @date 2025/8/5 17:29
 */
@Data
public class InvitationExportModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "访客.")
    @ExcelProperty(value = "访客", index = 1)
    private String visitorNames;

    @ApiModelProperty(value = "到访开始时间.")
    @ExcelProperty(value = "到访开始时间", index = 2)
    private String startTimeStr;

    @ApiModelProperty(value = "到访结束时间.")
    @ExcelProperty(value = "到访结束时间", index = 3)
    private String endTimeStr;

    @ApiModelProperty(value = "到访区域")
    @ExcelProperty(value = "到访区域", index = 4)
    private String spaceNames;

    @ApiModelProperty(value = "到访事由;1：参观调研；2：参加会议；3：业务培训.")
    @ExcelProperty(value = "到访事由", index = 5)
    private String visitReasonTypeStr;

    @ApiModelProperty(value = "邀约人.")
    @ExcelProperty(value = "邀约人", index = 6)
    private String inviteUname;

    @ApiModelProperty(value = "接待人.")
    @ExcelProperty(value = "接待人", index = 7)
    private String receiveUname;

    @ApiModelProperty(value = "邀约状态.")
    @ExcelProperty(value = "邀约状态", index = 8)
    private String inviteStatusStr;

    @ApiModelProperty(value = "邀约时间.")
    @ExcelProperty(value = "邀约时间", index = 9)
    private String createTimeStr;

    @ApiModelProperty(value = "邀约说明.")
    @ExcelProperty(value = "邀约说明", index = 10)
    private String remark;


}
