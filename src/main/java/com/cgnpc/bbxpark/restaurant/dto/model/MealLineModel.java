
package com.cgnpc.bbxpark.restaurant.dto.model;

import com.cgnpc.bbxpark.restaurant.domain.MealLineTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class MealLineModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4880174263764988443L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    private Long restaurantId;

    @ApiModelProperty(value = "餐厅名称.")
    private String restaurantName;

    @ApiModelProperty(value = "餐线名称.")
    private String name;

    @ApiModelProperty(value = "餐线类型(字典).")
    private String type;

    @ApiModelProperty(value = "状态(1->启用;0->禁用).")
    private Integer status;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    /*@ApiModelProperty(value = "摄像头id集合")
    private List<Long> deviceIdList;*/

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "所属楼层物模型编码.")
    private String sslcCode;

    @ApiModelProperty(value = "pos号集合.")
    private List<String> posList;

    @ApiModelProperty(value = "营业时间集合.")
    private List<MealLineTimeModel> timeModels;
}
