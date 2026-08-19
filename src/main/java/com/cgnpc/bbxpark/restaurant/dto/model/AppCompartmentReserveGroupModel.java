
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 包间预定分组业务数据模型
 * @author huangyongtao
 * @date 2024/7/31 16:03
 */
@Data
public class AppCompartmentReserveGroupModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "包间id.")
    private Long id;
    @ApiModelProperty(value = "包间名称.")
    private String name;
    @ApiModelProperty(value = "包间图片.")
    private String imageUrl;
    @ApiModelProperty(value = "容纳人数.")
    private Integer people;
    @ApiModelProperty(value = "满意度")
    private Double satisfaction;
    @ApiModelProperty(value = "包间介绍.")
    private String introduce;
    @ApiModelProperty(value = "面积.")
    private Double area;
    @ApiModelProperty(value = "位置")
    private String spaceFullPath;
    @ApiModelProperty(value = "营业状态.")
    private Boolean runState;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "营业时间.")
    private List<AppCompartmentTimeExModel> timeList;
    @ApiModelProperty(value = "预约时间集合")
    private List<ReserveTimeMode> reserveList;
    @ApiModelProperty(value = "设施集合")
    private List<AppCompartmentDeviceModel> deviceList;
}
