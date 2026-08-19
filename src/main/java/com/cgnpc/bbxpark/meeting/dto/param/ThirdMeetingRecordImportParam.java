package com.cgnpc.bbxpark.meeting.dto.param;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ThirdMeetingRecordImportParam {
    @ApiModelProperty(value = "propertiesName")
    @ExcelProperty(value = {"propertiesName"}, index = 0)
    private String propertiesName;

    @Length(max = 64)
    @ApiModelProperty(value = "frameshortname")
    @ExcelProperty(value = {"frameshortname"}, index = 1)
    private String frameShortName;

    @Length(max = 64)
    @ApiModelProperty(value = "mediatype.")
    @ExcelProperty(value = {"mediatype"}, index = 2)
    private String mediaType;

    @Length(max = 32)
    @ApiModelProperty(value = "ConferID.")
    @ExcelProperty(value = {"ConferID"}, index = 3)
    private String conferId;

    @Length(max = 255)
    @ApiModelProperty(value = "ConferName.")
    @ExcelProperty(value = {"ConferName"}, index = 4)
    private String conferName;

    @ApiModelProperty(value = "AccountName.")
    @ExcelProperty(value = {"AccountName"}, index = 5)
    private String accountName;

    @ApiModelProperty(value = "DepartmentName.")
    @ExcelProperty(value = {"DepartmentName"}, index = 6)
    private String departmentName;

    @ApiModelProperty(value = "framename.")
    @ExcelProperty(value = {"framename"}, index = 7)
    private String frameName;

    @ApiModelProperty(value = "barterm.")
    @ExcelProperty(value = {"barterm"}, index = 8)
    private String barTerm;

    @ApiModelProperty(value = "barman.")
    @ExcelProperty(value = {"barman"}, index = 9)
    private String barman;

    @ApiModelProperty(value = "endtime.")
    @ExcelProperty(value = {"endtime"}, index = 10)
    private String endTime;

    @ApiModelProperty(value = "starttime.")
    @ExcelProperty(value = {"starttime"}, index = 11)
    private String startTime;

    @ApiModelProperty(value = "RoomName.")
    @ExcelProperty(value = {"RoomName"}, index = 12)
    private String roomName;

    @ApiModelProperty(value = "pName.")
    @ExcelProperty(value = {"pName"}, index = 13)
    private String pName;

    @ApiModelProperty(value = "fname.")
    @ExcelProperty(value = {"fname"}, index = 14)
    private String fName;

    @ExcelIgnore
    @ApiModelProperty(value = "行数.")
    private String number;

    @ExcelIgnore
    @ApiModelProperty(value = "错误信息.")
    private String errMessage;
}
