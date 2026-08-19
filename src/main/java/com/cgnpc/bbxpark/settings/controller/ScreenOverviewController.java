
package com.cgnpc.bbxpark.settings.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.settings.dto.model.ScreenOverviewModel;
import com.cgnpc.bbxpark.settings.dto.param.ScreenOverviewParam;
import com.cgnpc.bbxpark.settings.service.IScreenOverviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.groups.Default;
import java.util.List;

@RestController
@RequestMapping(Constant.BASE_PATH + "/screen/overview")
@Api(tags = "大屏总览")
public class ScreenOverviewController {

    /**
     * 模块服务接口.
     */
    @Autowired
    private IScreenOverviewService screenOverviewService;

    /**
     * 获取模块信息.
     */
    @ApiOperation(value = "获取模块信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<ScreenOverviewModel> detail(@PathVariable Long id) {
            return CudResult.success(screenOverviewService.detail(id));
    }

    /**
     * 获取模块列表.
     */
    @ApiOperation(value = "获取模块列表")
    @PostMapping(value = "/list")
    public CudResult<List<ScreenOverviewModel>> list() {
        return CudResult.success(screenOverviewService.listBy());
    }

    /**
     * 新增模块.
     */
    @ApiOperation(value = "新增模块数据")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated({Default.class, InsertGroup.class}) @RequestBody ScreenOverviewParam param) {
            return CudResult.success(screenOverviewService.add(param));
    }

    /**
     * 编辑模块.
     */
    @ApiOperation(value = "编辑模块数据")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@Validated({Default.class, UpdateGroup.class}) @RequestBody ScreenOverviewParam param) {
        return CudResult.success(screenOverviewService.edit(param));
    }
}
