
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会议预约入参数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:33
 */
@Data
public class MeetingReserveAppSaveParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @Length(max = 100)
    @ApiModelProperty(value = "会议主题.")
    private String reserveName;

    @Length(max = 100)
    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;

    @ApiModelProperty(value = "预约人id.")
    private String reserveUid;

    @ApiModelProperty(value = "预约人部门id.")
    private String reserveDepartmentId;

    @ApiModelProperty(value = "预约人部门.")
    private String reserveDepartment;

    @ApiModelProperty(value = "涉密会议(0->是;1->否)")
    private Integer confidentiality;

    @ApiModelProperty(value = "会议摘要")
    private String summary;

    @ApiModelProperty(value = "会服留言")
    private String serveRemark;

    @ApiModelProperty(value = "参会人数")
    private Integer participantNumber;

    @ApiModelProperty(value = "ordinary->普通会议;video->视频会议)")
    private String meetingType;

    @ApiModelProperty(value = "需要密码(0->是;1->否)")
    private Integer needPassword;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "保留音频(0->是;1->否)")
    private Integer retainedAudio;

    @ApiModelProperty(value = "参会方式(initiator->发起方(主会场);participator->参与方(分会场))")
    private String way;

    @ApiModelProperty(value = "文件列表")
    private List<MeetingReserveFileParam> fileParamList;

    @ApiModelProperty(value = "文件信息")
    private MeetingReserveFileParam fileParam;

    @ApiModelProperty(value = "座位列表")
    private List<MeetingReserveSeatParam> seatParamList;

    @ApiModelProperty(value = "参会人id集合")
    private List<String> userIdList;

    @ApiModelProperty(value = "会服信息列表")
    private List<MeetingServiceDetailParam> serviceParamList;

    @ApiModelProperty(value = "是否必须签到(0->必须签到;1->自愿签到)")
    @Min(value = 0)
    @Max(value = 1)
    @NotNull(groups = UpdateGroup.class,message = "签到要求不能为空")
    private Integer mustSignFlag;

    @ApiModelProperty(value = "是否允许代签(0->允许;1->不允许)")
    @Min(value = 0)
    @Max(value = 1)
    @NotNull(groups = UpdateGroup.class,message = "代签到不能为空")
    private Integer behalfSignFlag;

    @ApiModelProperty(value = "是否允许补签(0->允许;1->不允许)")
    @Min(value = 0)
    @Max(value = 1)
    @NotNull(groups = UpdateGroup.class,message = "补签不能为空")
    private Integer replenishSignFlag;


}
