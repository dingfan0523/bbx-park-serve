
package com.cgnpc.bbxpark.space.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.SpaceManagerModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerPageParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerParam;
import com.cgnpc.bbxpark.space.service.ISpaceManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(Constant.BASE_PATH + "/space/manager")
@Api(tags = "智慧空间-空间责任人")
@Slf4j
public class SpaceManagerController {

    /**
     * 空间责任人服务接口.
     */
    @Autowired
    private ISpaceManagerService spaceManagerService;

    /**
     * 获取空间责任人信息.
     */
    @ApiOperation(value = "获取空间责任人信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<SpaceManagerModel> detail(@PathVariable Long id) {
        return CudResult.success(spaceManagerService.detail(id));
    }

    /**
     * 获取空间责任人列表(分页).
     */
    @ApiOperation(value = "获取空间责任人列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<SpaceManagerModel>> page(@RequestBody SpaceManagerPageParam param) {
        return CudResult.success(spaceManagerService.page(param));
    }

    /**
     * 获取空间责任人列表.
     */
    @ApiOperation(value = "获取空间责任人列表")
    @PostMapping(value = "/list")
    public CudResult<List<SpaceManagerModel>> list(@RequestBody SpaceManagerListParam param) {
        return CudResult.success(spaceManagerService.list(param));
    }

    /**
     * 新增空间责任人.
     */
    @ApiOperation(value = "新增空间责任人")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated @RequestBody SpaceManagerParam param) {
        return CudResult.success(spaceManagerService.add(param));
    }

    /**
     * 编辑空间责任人.
     */
    @ApiOperation(value = "编辑空间责任人")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated SpaceManagerParam param) {
        return CudResult.success(spaceManagerService.edit(param));
    }

    /**
     * 删除空间责任人.
     */
    @ApiOperation(value = "删除空间责任人")
    @GetMapping(value = "remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(spaceManagerService.remove(id));
    }
}
