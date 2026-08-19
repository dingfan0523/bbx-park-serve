
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.validation.PhoneNumber;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
public class RestaurantParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3902163899450529982L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @Length(max = 30, groups = InsertGroup.class, message = "餐厅名称长度不能超过30.")
    @NotNull(groups = InsertGroup.class, message = "餐厅名称不能为空.")
    @ApiModelProperty(value = "餐厅名称.")
    private String name;

    @ApiModelProperty(value = "餐厅图片.")
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "餐厅图片不能为空.")
    private String imageUrl;

    @ApiModelProperty(value = "容纳人数.")
    @Max(value = 9999, groups = {InsertGroup.class, UpdateGroup.class}, message = "容纳人数不能超过4位数.")
    private Integer capacity;

    @Length(max = 12, groups = {InsertGroup.class, UpdateGroup.class}, message = "餐厅电话长度不能超过12位.")
    @PhoneNumber(groups = {InsertGroup.class, UpdateGroup.class},message = "餐厅电话格式不正确")
    @ApiModelProperty(value = "餐厅电话.")
    private String telphone;

    @Length(max = 200, groups = {InsertGroup.class, UpdateGroup.class}, message = "餐厅介绍长度不能超过200.")
    @ApiModelProperty(value = "介绍.")
    private String introduce;

    @Length(max = 200, groups = {InsertGroup.class, UpdateGroup.class}, message = "餐厅通知长度不能超过200.")
    @ApiModelProperty(value = "通知.")
    private String notification;

    @ApiModelProperty(value = "状态(1->启用;0->禁用).")
    private Integer status;

    @ApiModelProperty(value = "餐厅标签(jsonArray格式).")
    private List<String> tags;

    @ApiModelProperty(value = "营业时间.")
    @Size(min = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "营业时间不能为空.")
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "餐厅位置不能为空.")
    private List<RestaurantTimeParam> timeList;

    @ApiModelProperty(value = "餐厅位置.")
    @Size(min = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "餐厅位置不能为空.")
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "餐厅位置不能为空.")
    private List<RestaurantSpaceParam> spaceList;
}
