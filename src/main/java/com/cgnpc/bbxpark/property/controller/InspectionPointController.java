package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.InspectionPointModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointListParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointPageParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.service.IInspectionPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 巡检点服务接口.
 */
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/inspection/point")
@Api(tags = "BBX-物业管理-PC端-巡检点")
public class InspectionPointController {

    /**
     * 巡检点服务接口.
     */
    @Autowired
    private IInspectionPointService inspectionPointService;

    /**
     * 获取巡检点信息.
     */
    @ApiOperation(value = "获取巡检点详细信息")
    @GetMapping(value = "/{id}")
    public CudResult<InspectionPointModel> detail(@PathVariable Long id) {
        return CudResult.success(inspectionPointService.detail(id));
    }

    /**
     * 获取巡检点列表(分页).
     */
    @ApiOperation(value = "获取巡检点列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<InspectionPointModel>> page(@RequestBody InspectionPointPageParam param) {
        return CudResult.success(inspectionPointService.page(param));
    }

    /**
     * 获取巡检点列表.
     */
    @ApiOperation(value = "获取巡检点列表")
    @PostMapping(value = "/list")
    public CudResult<List<InspectionPointModel>> list(@RequestBody InspectionPointListParam param) {
        return CudResult.success(inspectionPointService.list(param));
    }

    /**
     * 新增巡检点.
     */
    @ApiOperation(value = "新增巡检点")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//({Default.class, InsertGroup.class})
                                      @RequestBody InspectionPointParam param) {
        return CudResult.success(inspectionPointService.add(param));
    }

    /**
     * 编辑巡检点.
     */
    @ApiOperation(value = "编辑巡检点")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                       InspectionPointParam param) {
        return CudResult.success(inspectionPointService.edit(param));
    }

    /**
     * 删除巡检点.
     */
    @ApiOperation(value = "删除巡检点")
    @GetMapping(value = "remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(inspectionPointService.remove(id));
    }

    /**
     * 编辑启用状态
     */
    @ApiOperation(value = "编辑启用状态")
    @PostMapping(value = "/status/edit")
    public CudResult<Boolean> statusEdit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                         InspectionPointStatusParam param) {
        return CudResult.success(inspectionPointService.statusEdit(param));
    }
}
