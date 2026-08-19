/**
 * Copyright © 2024-2025 AsiaInfo Technologies Limited.
 * All Rights Reserved.
 * -
 * This software is the confidential and proprietary information of
 * AsiaInfo Technologies Limited.
 * ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with asiainfo.
 * -
 * ASIAINFO MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT.ASIAINFO SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/***
 * @value 大屏餐线菜品信息
 * @author huangyongtao
 * @date 2026/2/28 15:30
 */
@Data
public class RestaurantDishesScheduleModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3311741091516297631L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐线id.")
    private Long mealLineId;

    @ApiModelProperty(value = "菜品名称.")
    private String name;

    @ApiModelProperty(value = "类别(字典).")
    private String type;

    @ApiModelProperty(value = "餐线名称")
    private String mealLineName;

    @ApiModelProperty(value = "用餐时间(字典).")
    private String mealTime;

    @ApiModelProperty(value = "出品日期(date).")
    private Date productionDate;

    @ApiModelProperty(value = "星期（1,2,3,4,5,6,7->对应周一到周日）.")
    private String week;

    @ApiModelProperty(value = "单价.")
    private String price;

    @ApiModelProperty(value = "克重.")
    private String weight;

    @ApiModelProperty(value = "辣度建议(0,1,2,3,4,5).")
    private Integer pungencyDegree;

    @ApiModelProperty(value = "原料信息.")
    private String information;

    @ApiModelProperty(value = "营养成分-碳水.")
    private String carbohydrate;

    @ApiModelProperty(value = "营养成分-脂肪.")
    private String fat;

    @ApiModelProperty(value = "营养成分-蛋白质.")
    private String protein;
}
