
package com.cgnpc.bbxpark.meeting.dto.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会服人员工作量统计
 * @author huangyongtao
 * @date 2025/2/11 10:55
 */
@Data
public class MeetingAttendantTaskPersonCountModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    @ExcelIgnore
    private Long id;

    @ApiModelProperty(value = "用户id")
    @ExcelIgnore
    private String userId;

    @ApiModelProperty(value = "用户工号")
    @ExcelIgnore
    private String staffid;

    @ApiModelProperty(value = "用户名称")
    @ExcelIgnore
    private String userName;

    @ApiModelProperty(value = "用户展示名称")
    @ExcelProperty(value = "会服人员名称", index = 0)
    @ColumnWidth(45)
    private String userShowName;

    @ApiModelProperty(value = "展示会议室名称")
    @ExcelProperty(value = "服务会议室", index = 1)
    @ColumnWidth(45)
    private String roomShowName;

    @ApiModelProperty(value = "服务会议的人数")
    @ExcelProperty(value = "服务会议人数", index = 3)
    @ColumnWidth(45)
    private Long personNum = 0L;

    @ApiModelProperty(value = "服务的会议场次")
    @ExcelIgnore
    private Long meetingNum = 0L;

    @ApiModelProperty(value = "提供的会服次数")
    @ExcelProperty(value = "服务次数", index = 2)
    @ColumnWidth(45)
    private Long attendantTaskNum = 0L;

    @ApiModelProperty(value = "过期的会服次数")
    @ExcelIgnore
    private Long expireAttendantTaskNum = 0L;

    @ApiModelProperty(value = "服务的平均分")
    @ExcelProperty(value = "平均分数", index = 4)
    @ColumnWidth(45)
    private String averageScore = "0.00";

    @ApiModelProperty(value = "服务的平均分double类型")
    @ExcelIgnore
    private Double averageScoreDouble = 0d;

    @ApiModelProperty(value = "会议室集合")
    @ExcelIgnore
    private List<String> roomNameList;

    @ApiModelProperty(value = "创建时间")
    @ExcelIgnore
    private Date createTime;

}
