
package com.cgnpc.bbxpark.invitation.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class AccessRecordModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4652375233937361513L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "人员类型.")
    private String personType;

    @ApiModelProperty(value = "人员标识.")
    private String personIdentity;

    @ApiModelProperty(value = "姓名.")
    private String name;

    @ApiModelProperty(value = "工号.")
    private String staffid;

    @ApiModelProperty(value = "联系方式.")
    private String mobile;

    @ApiModelProperty(value = "通行方向.")
    private String accessDir;

    @ApiModelProperty(value = "通行方式.")
    private String accessWay;

    @ApiModelProperty(value = "通行结果.")
    private String accessResult;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "设备位置")
    private String deviceLocation;

    @ApiModelProperty(value = "卡号.")
    private String card;

    @ApiModelProperty(value = "人脸图片.")
    private String faceImg;

    @ApiModelProperty(value = "通行时间")
    private Date createTime;
}
