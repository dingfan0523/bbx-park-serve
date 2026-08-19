
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.settings.dto.param.FileParam;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会服人员任务入参数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:47
 */
@Data
public class MeetingAttendantTaskParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键id.")
    private Long id;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "会议id.")
    private Long reserveId;

    @Length(max = 100)
    @ApiModelProperty(value = "会议名称.")
    private String reserveName;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @Length(max = 100)
    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会服类型;(1->会前布置；2->会中呼叫；3->会后清洁).")
    private Integer serviceType;

    @ApiModelProperty(value = "会服状态;(1->未处理；2->已确认； 3->已完成).")
    private Integer serviceStatus;

    @ApiModelProperty(value = "会服是否有效;(0->有效；1->无效).")
    private Integer serviceValid;

    @Length(max = 255)
    @ApiModelProperty(value = "会服备注.")
    private String serviceRemark;

    @ApiModelProperty(value = "处理人id.")
    private String handleUid;

    @Length(max = 100)
    @ApiModelProperty(value = "处理人名称.")
    private String handleUname;

    @Length(max = 100)
    @ApiModelProperty(value = "处理人工号.")
    private String handleStaffid;

    @ApiModelProperty(value = "处理时间.")
    private Date handleTime;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "实际参会人数")
    private Integer realParticipantNumber;

    @ApiModelProperty(value = "文件列表")
    private List<FileParam> fileParamList;
}
