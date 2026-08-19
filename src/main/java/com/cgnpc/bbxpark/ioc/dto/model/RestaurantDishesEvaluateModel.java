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
import java.util.Date;

/***
 * @value 大屏菜品评价信息
 * @author huangyongtao
 * @date 2026/2/28 15:43
 */
@Data
public class RestaurantDishesEvaluateModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "满意度（1-5星）.")
    private Integer satisfaction;

    @ApiModelProperty(value = "满意度状态.")
    private Boolean status;

    @ApiModelProperty(value = "味道(字典)")
    private String taste;

    @ApiModelProperty(value = "评价人id.")
    private String appraiserId;

    @ApiModelProperty(value = "评价人工号.")
    private String appraiserStaffid;

    @ApiModelProperty(value = "评价人名称.")
    private String appraiserName;

    @ApiModelProperty(value = "匿名状态(0->未匿名;1->匿名).")
    private Integer anonymityStatus;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;


}
