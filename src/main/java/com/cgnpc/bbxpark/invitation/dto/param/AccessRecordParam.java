
package com.cgnpc.bbxpark.invitation.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AccessRecordParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3460831382002323686L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @Length(max = 100)
    @ApiModelProperty(value = "人员标识.")
    private String personIdentity;

    @Length(max = 32)
    @ApiModelProperty(value = "姓名.")
    private String name;

    @Length(max = 32)
    @ApiModelProperty(value = "联系方式.")
    private String mobile;

    @Length(max = 32)
    @ApiModelProperty(value = "通行方向.")
    private String accessDir;

    @Length(max = 32)
    @ApiModelProperty(value = "通行方式.")
    private String accessWay;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @Length(max = 64)
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @Length(max = 32)
    @ApiModelProperty(value = "卡号.")
    private String card;

    @Length(max = 64)
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
