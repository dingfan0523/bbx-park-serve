package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.PatrolPointListModel;
import com.cgnpc.bbxpark.property.dto.model.PatrolRouteModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.service.IPatrolRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 巡更路线服务控制类
 */
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/patrol/route")
@Api(tags = "BBX-物业管理-PC端-巡更路线")
public class PatrolRouteController {

    /**
     * 巡更路线服务接口.
     */
    @Autowired
    private IPatrolRouteService patrolRouteService;

    /**
     * 获取巡更路线列表(分页).
     */
    @ApiOperation(value = "获取巡更路线列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<PatrolRouteModel>> page(@RequestBody PatrolRoutePageParam param) {
        return CudResult.success(patrolRouteService.page(param));
    }

    /**
     * 获取巡更路线列表.
     */
    @ApiOperation(value = "获取巡更路线列表")
    @PostMapping(value = "/list")
    public CudResult<List<PatrolRouteModel>> list(@RequestBody PatrolRouteListParam param) {
        return CudResult.success(patrolRouteService.list(param));
    }

    /**
     * 获取巡更路线信息.
     */
    @ApiOperation(value = "获取巡更路线详细信息")
    @GetMapping(value = "/{id}")
    public CudResult<PatrolRouteModel> detail(@PathVariable Long id) {
        return CudResult.success(patrolRouteService.detail(id));
    }

    /**
     * 新增巡更路线.
     */
    @ApiOperation(value = "新增巡更路线")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//({Default.class, InsertGroup.class})
                                      @RequestBody PatrolRouteParam param) {
        return CudResult.success(patrolRouteService.add(param));
    }

    /**
     * 编辑巡更路线.
     */
    @ApiOperation(value = "编辑巡更路线")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                       PatrolRouteParam param) {
        return CudResult.success(patrolRouteService.edit(param));
    }

    /**
     * 删除巡更路线.
     */
    @ApiOperation(value = "删除巡更路线")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(patrolRouteService.remove(id));
    }


    /**
     * 编辑启用状态
     */
    @ApiOperation(value = "编辑启用状态")
    @PostMapping(value = "/status/edit")
    public CudResult<Boolean> statusEdit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                         InspectionPointStatusParam param) {
        return CudResult.success(patrolRouteService.statusEdit(param));
    }

    /**
     * 获取巡更路线下的巡更点列表.
     */
    @ApiOperation(value = "根据id获取巡更路线下的巡更点列表")
    @GetMapping(value = "/findPointList")
    public CudResult<List<PatrolPointListModel>> findPointList(@RequestParam Long id) {
        return CudResult.success(patrolRouteService.findPointList(id));
    }

    /**
     * 保存巡更路线下的巡更点.
     */
    @ApiOperation(value = "保存巡更路线下的巡更点")
    @PostMapping(value = "/savePoint")
    public CudResult<Boolean> savePoint(@Validated//({Default.class, InsertGroup.class})
                                            @RequestBody PatrolRoutePointParam param) {
        return CudResult.success(patrolRouteService.savePoint(param));
    }

    /**
     * 保存巡更路线下的巡更点.
     */
    @ApiOperation(value = "删除巡更路线下的巡更点")
    @GetMapping(value = "/removePoint")
    public CudResult<Boolean> removePoint(@RequestParam("id") @ApiParam(value = "巡更路线id") Long id,
                                     @RequestParam("pointId") @ApiParam(value = "巡更点id") Long pointId) {
        return CudResult.success(patrolRouteService.removePoint(id,pointId));
    }
}