
package com.cgnpc.bbxpark.meeting.dto.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会议服务评价业务数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:27
 */
@Data
public class MeetingAttendantEvaluateDetailModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    @ExcelIgnore
    private Long id;

    @ApiModelProperty(value = "分数.")
    @ExcelProperty(value = "评价分数", index = 1)
    @ColumnWidth(45)
    private Integer score;

    @ApiModelProperty(value = "内容.")
    @ExcelProperty(value = "评价内容", index = 4)
    @ColumnWidth(45)
    private String content;

    @ApiModelProperty(value = "操作人id.")
    @ExcelIgnore
    private String operateUid;

    @ApiModelProperty(value = "操作人名称.")
    @ExcelIgnore
    private String operateUname;

    @ApiModelProperty(value = "操作人工号.")
    @ExcelIgnore
    private String operateStaffid;

    @ApiModelProperty(value = "评价人展示名称")
    @ExcelProperty(value = "评价人", index = 5)
    @ColumnWidth(45)
    private String operateShowName;

    @ApiModelProperty(value = "会议id.")
    @ExcelIgnore
    private Long reserveId;

    @ApiModelProperty(value = "会议名称.")
    @ExcelProperty(value = "会议名称", index = 3)
    @ColumnWidth(45)
    private String reserveName;

    @ApiModelProperty(value = "会议室id.")
    @ExcelIgnore
    private Long roomId;

    @ApiModelProperty(value = "会议室名称.")
    @ExcelProperty(value = "会议室", index = 2)
    @ColumnWidth(45)
    private String roomName;

    @ApiModelProperty(value = "创建时间.")
    @ExcelIgnore
    private Date createTime;

    @ApiModelProperty(value = "创建时间.")
    @ExcelProperty(value = "评价时间", index = 6)
    @ColumnWidth(45)
    private String createTimeStr;

    @ApiModelProperty(value = "处理人id.")
    @ExcelIgnore
    private String handleUid;

    @ApiModelProperty(value = "处理人名称.")
    @ExcelIgnore
    private String handleUname;

    @ApiModelProperty(value = "处理人工号.")
    @ExcelIgnore
    private String handleStaffid;

    @ApiModelProperty(value = "会服展示名称")
    @ExcelProperty(value = "会服人员名称", index = 0)
    @ColumnWidth(45)
    private String handleShowName;


}
