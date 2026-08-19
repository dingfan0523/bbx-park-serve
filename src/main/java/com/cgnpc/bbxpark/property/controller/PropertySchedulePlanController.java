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
 * ANY DAMAGES SUFFERED BY LICENSEE AS A CudResult OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.cgnpc.bbxpark.property.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.PropertyDatePlanDetailModel;
import com.cgnpc.bbxpark.property.dto.model.PropertyDatePlanModel;
import com.cgnpc.bbxpark.property.dto.model.PropertySchedulePlanModel;
import com.cgnpc.bbxpark.property.dto.param.PropertyDatePlanParam;
import com.cgnpc.bbxpark.property.dto.param.PropertySchedulePlanListParam;
import com.cgnpc.bbxpark.property.dto.param.PropertySchedulePlanParam;
import com.cgnpc.bbxpark.property.service.IPropertySchedulePlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Description 物业分组排班计划服务控制类
 * @author huangyongtao
 * @date 2025/9/28 13:44
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/property/schedule/plan")
@Api(tags = "物业分组排班计划")
public class PropertySchedulePlanController {

    /**
     * 物业分组排班计划服务接口.
     */
    @Autowired
    private IPropertySchedulePlanService propertySchedulePlanService;

    /**
     * 获取物业分组排班计划信息.
     */
    @ApiOperation(value = "获取物业分组排班计划信息")
    @GetMapping(value = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<PropertySchedulePlanModel> detail(@PathVariable Long scheduleId) {
        return CudResult.success(propertySchedulePlanService.detail(scheduleId));
    }

    /**
     * 获取物业分组排班计划列表.
     */
    @ApiOperation(value = "获取物业分组排班计划列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<PropertySchedulePlanModel>> list(@RequestBody PropertySchedulePlanListParam param) {
        return CudResult.success(propertySchedulePlanService.list(param));
    }

    /**
     * 新增物业分组排班计划.
     */
    @ApiOperation(value = "新增物业分组排班计划")
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> add(@RequestBody PropertySchedulePlanParam param) {
        return CudResult.success(propertySchedulePlanService.add(param));
    }

    /**
     * 获取物业分组排班日历.
     */
    @ApiOperation(value = "获取物业分组排班日历")
    @PostMapping(value = "/getDatePlan", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<PropertyDatePlanModel>> getDatePlan(@RequestBody PropertyDatePlanParam param) {
        return CudResult.success(propertySchedulePlanService.getDatePlan(param));
    }

    /**
     * 获取物业分组排班日历详情.
     */
    @ApiOperation(value = "获取物业分组排班日历详情")
    @PostMapping(value = "/getDatePlanDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<PropertyDatePlanDetailModel>> getDatePlanDetail(@RequestBody PropertyDatePlanParam param) {
        return CudResult.success(propertySchedulePlanService.getDatePlanDetail(param));
    }
}
