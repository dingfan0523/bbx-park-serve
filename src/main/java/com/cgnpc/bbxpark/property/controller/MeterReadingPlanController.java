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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.property.dto.model.MeterReadingPlanModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.service.IMeterReadingPlanService;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 抄表计划管理服务控制类
 * @author huangyongtao
 * @date 2025/3/26 14:09
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/meterReading/plan")
@Api(tags = "智慧物业-PC端-抄表计划管理")
public class MeterReadingPlanController {

    /**
     * 抄表计划管理服务接口.
     */
    @Autowired
    private IMeterReadingPlanService meterReadingPlanService;

    /**
     * 获取抄表计划管理信息.
     */
    @ApiOperation(value = "获取抄表计划管理信息")
    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<MeterReadingPlanModel> detail(@PathVariable Long id) {
        return CudResult.success(meterReadingPlanService.detail(id));
    }

    /**
     * 获取抄表计划管理列表(分页).
     */
    @ApiOperation(value = "获取抄表计划管理列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<MeterReadingPlanModel>> page(@RequestBody MeterReadingPlanPageParam param) {
        return CudResult.success(meterReadingPlanService.page(param));
    }

    /**
     * 获取抄表计划管理列表.
     */
    @ApiOperation(value = "获取抄表计划管理列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<MeterReadingPlanModel>> list(@RequestBody MeterReadingPlanListParam param) {
        return CudResult.success(meterReadingPlanService.list(param));
    }

    /**
     * 新增抄表计划管理.
     */
    @ApiOperation(value = "新增抄表计划管理")
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> add(@Validated({Default.class}) @RequestBody MeterReadingPlanParam param) {
        return CudResult.success(meterReadingPlanService.add(param));
    }

    /**
     * 删除抄表计划管理.
     */
    @ApiOperation(value = "删除抄表计划管理")
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(meterReadingPlanService.remove(id));
    }

    /**
     * 编辑抄表计划管理.
     */
    @ApiOperation(value = "编辑抄表计划管理")
    @PostMapping(value = "/edit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> edit(@Validated({Default.class}) @RequestBody MeterReadingPlanParam param) {
        return CudResult.success(meterReadingPlanService.edit(param));
    }

    /**
     * 编辑启用状态抄表计划管理.
     */
    @ApiOperation(value = "编辑启用状态抄表计划管理")
    @PostMapping(value = "/status/edit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> statusEdit(@RequestBody MeterReadingPlanStatusParam param) {
        return CudResult.success(meterReadingPlanService.statusEdit(param));
    }
    /**
     * 查询抄表设备列表.
     */
    @ApiOperation(value = "查询抄表设备列表")
    @PostMapping(value = "/findDeviceList", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<IocDeviceModel>> findDeviceList(@RequestBody MeterReadingPlanDeviceParam param) {
        return CudResult.success(meterReadingPlanService.findDeviceList(param));
    }

    /**
     * 获取抄表计划工单列表(分页).
     */
    @ApiOperation(value = "获取抄表计划工单列表(分页)")
    @PostMapping(value = "/pageWorkOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<WorkOrderModel>> pageWorkOrder(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(meterReadingPlanService.pageWorkOrder(param));
    }

    /**
     * 执行抄表计划
     */
    @ApiOperation(value = "执行抄表计划")
    @PostMapping(value = "/executePlan")
    public CudResult<Boolean> executePlan() {
        return CudResult.success(meterReadingPlanService.executePlan(null));
    }
}
