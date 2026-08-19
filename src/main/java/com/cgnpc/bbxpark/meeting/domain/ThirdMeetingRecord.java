package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bbx_third_meeting_record")
public class ThirdMeetingRecord implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long fileId;
    private String propertiesName;
    private String frameShortName;
    private String mediaType;
    private String conferId;
    private String conferName;
    private String accountName;
    private String departmentName;
    private String frameName;
    private String barTerm;
    private String barman;
    private Date endTime;
    private Date startTime;
    /**
     * 会议时长
     */
    private BigDecimal duration;
    private String roomName;
    private String pName;
    private String fName;

    /**
     * 记录时间
     */
    private Date recordTime;
    private Long tenantId;
    private Integer deleted;
}
