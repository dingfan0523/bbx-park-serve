
package com.cgnpc.bbxpark.invitation.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 邀约访客列表参数模型
 * @author huangyongtao
 * @date 2025/8/1 14:10
 */
@Data
public class InvitationVisitorListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邀约id.")
    private Long inviteId;

    @ApiModelProperty(value = "邀约id集合.")
    private List<Long> inviteIdList;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "电话.")
    private String phone;

    @ApiModelProperty(value = "公司.")
    private String company;

    @ApiModelProperty(value = "人脸照片.")
    private String faceImg;

    @ApiModelProperty(value = "第三方访客id.")
    private String thirdVisitId;

    @ApiModelProperty(value = "是否到访;0：到访；1：未到访.")
    private Integer visitStatus;

    @ApiModelProperty(value = "到访时间.")
    private Date visitTime;
}
