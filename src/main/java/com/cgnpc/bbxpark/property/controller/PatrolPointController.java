package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.PatrolPointModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPointListParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPointPageParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPointParam;
import com.cgnpc.bbxpark.property.service.IPatrolPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 巡更点服务控制类
 */
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/patrol/point")
@Api(tags = "BBX-物业管理-PC端-巡更点")
public class PatrolPointController {
    /**
     * 巡更点服务接口.
     */
    @Autowired
    private IPatrolPointService patrolPointService;

    /**
     * 获取巡更点信息.
     */
    @ApiOperation(value = "获取巡更点详细信息")
    @GetMapping(value = "/{id}")
    public CudResult<PatrolPointModel> detail(@PathVariable Long id) {
        return CudResult.success(patrolPointService.detail(id));
    }

    /**
     * 获取巡更点列表(分页).
     */
    @ApiOperation(value = "获取巡更点列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<PatrolPointModel>> page(@RequestBody PatrolPointPageParam param) {
        return CudResult.success(patrolPointService.page(param));
    }

    /**
     * 获取巡更点列表.
     */
    @ApiOperation(value = "获取巡更点列表")
    @PostMapping(value = "/list")
    public CudResult<List<PatrolPointModel>> list(@RequestBody PatrolPointListParam param) {
        return CudResult.success(patrolPointService.list(param));
    }

    /**
     * 新增巡更点.
     */
    @ApiOperation(value = "新增巡更点")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//({Default.class, InsertGroup.class})
                                      @RequestBody PatrolPointParam param) {
        return CudResult.success(patrolPointService.add(param));
    }

    /**
     * 编辑巡更点.
     */
    @ApiOperation(value = "编辑巡更点")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                       PatrolPointParam param) {
        return CudResult.success(patrolPointService.edit(param));
    }

    /**
     * 删除巡更点.
     */
    @ApiOperation(value = "删除巡更点")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(patrolPointService.remove(id));
    }

    /**
     * 编辑启用状态
     */
    @ApiOperation(value = "编辑启用状态")
    @PostMapping(value = "/status/edit")
    public CudResult<Boolean> statusEdit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                         InspectionPointStatusParam param) {
        return CudResult.success(patrolPointService.statusEdit(param));
    }
}
