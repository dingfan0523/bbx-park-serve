
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
public class MealLineParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3654218208185154334L;

    @ApiModelProperty(value = "id.")
    @NotNull(groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    @NotNull(groups = {InsertGroup.class,UpdateGroup.class},message = "餐厅id不能为空")
    private Long restaurantId;

    @Length(max = 30,message = "餐线名称不能超过30字")
    @ApiModelProperty(value = "餐线名称.")
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class},message = "餐线名称不能为空")
    private String name;

    @Length(max = 32)
    @ApiModelProperty(value = "餐线类型(字典).")
    private String type;

    /*@ApiModelProperty(value = "摄像头id集合")
    private List<Long> deviceIdList;*/

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "所属楼层物模型编码.")
    private String sslcCode;

    @ApiModelProperty(value = "pos号集合.")
    private List<String> posList;

    @ApiModelProperty(value = "营业时间集合.")
    private List<MealLineTimeParam> mealLineTimeParams;
}
