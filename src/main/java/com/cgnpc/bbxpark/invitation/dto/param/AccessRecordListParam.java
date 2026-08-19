
package com.cgnpc.bbxpark.invitation.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AccessRecordListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4271807040006952759L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "人员标识.")
    private String personIdentity;

    @ApiModelProperty(value = "姓名.")
    private String name;

    @ApiModelProperty(value = "联系方式.")
    private String mobile;

    @ApiModelProperty(value = "通行方向.")
    private String accessDir;

    @ApiModelProperty(value = "通行方式.")
    private String accessWay;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "卡号.")
    private String card;

    @ApiModelProperty(value = "人脸图片.")
    private String faceImg;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    private Integer deleted;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "乐观锁.")
    private Integer revision;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "id集合.")
    private List<Long> ids;
}
