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
 * @value 餐厅投诉建议信息
 * @author huangyongtao
 * @date 2026/2/28 15:48
 */
@Data
public class RestaurantComplaintSuggestionModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "完成时间.")
    private Date completeTime;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "状态;10：待回复；20：已分配（待处理）；30：待审核；40：审核驳回；50：已完成；60：已评价")
    private Integer status;

    @ApiModelProperty(value = "提交人工号.")
    private String submitStaffid;

    @ApiModelProperty(value = "提交时间.")
    private Date submitTime;

    @ApiModelProperty(value = "提交人id.")
    private Long submitUid;

    @ApiModelProperty(value = "提交人名称.")
    private String submitUname;

    @ApiModelProperty(value = "标题.")
    private String title;

    @ApiModelProperty(value = "类型;complaint：投诉；suggestion：建议.")
    private String type;

}
