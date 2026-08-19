
package com.cgnpc.bbxpark.invitation.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
/***
 * @Description 邀约访客入参数据模型
 * @author huangyongtao
 * @date 2025/8/1 14:11
 */
@Data
public class InvitationVisitorParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邀约id.")
    private Long inviteId;

    @Length(max = 50)
    @ApiModelProperty(value = "名称.")
    private String name;

    @Length(max = 50)
    @ApiModelProperty(value = "电话.")
    private String phone;

    @Length(max = 100)
    @ApiModelProperty(value = "公司.")
    private String company;

    @Length(max = 100)
    @ApiModelProperty(value = "人脸照片.")
    private String faceImg;

    @Length(max = 100)
    @ApiModelProperty(value = "第三方访客id.")
    private String thirdVisitId;

    @ApiModelProperty(value = "是否到访;0：到访；1：未到访.")
    private Integer visitStatus;

    @ApiModelProperty(value = "到访时间.")
    private Date visitTime;
}
