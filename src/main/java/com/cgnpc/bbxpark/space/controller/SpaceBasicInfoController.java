
package com.cgnpc.bbxpark.space.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.SimpleSpaceBasicInfoModel;
import com.cgnpc.bbxpark.space.dto.model.SpaceBasicInfoModel;
import com.cgnpc.bbxpark.space.dto.model.SpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceBasicInfoListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceBasicInfoParam;
import com.cgnpc.bbxpark.space.service.ISpaceBasicInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(Constant.BASE_PATH + "/space")
@Api(tags = "智慧空间-空间基础信息")
@Slf4j
public class SpaceBasicInfoController {

    /**
     * 空间基础信息服务接口.
     */
    @Autowired
    private ISpaceBasicInfoService spaceBasicInfoService;

    /**
     * 获取空间树形结构列表
     */
    @ApiOperation(value = "获取空间树形结构列表")
    @PostMapping(value = "/tree")
    public CudResult<List<SpaceTreeModel>> tree(@RequestBody SpaceBasicInfoListParam param) {
        return CudResult.success(spaceBasicInfoService.tree(param));
    }

    @ApiOperation(value = "获取空间列表")
    @PostMapping(value = "/list")
    public CudResult<List<SimpleSpaceBasicInfoModel>> list(@RequestBody SpaceBasicInfoListParam param) {
        return CudResult.success(spaceBasicInfoService.list(param));
    }

    /**
     * 获取空间基础信息信息.
     */
    @ApiOperation(value = "获取空间基础信息信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<SpaceBasicInfoModel> detail(@PathVariable Long id) {
        return CudResult.success(spaceBasicInfoService.detail(id));
    }

    /**
     * 编辑空间基础信息.
     */
    @ApiOperation(value = "编辑空间基础信息")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated SpaceBasicInfoParam param) {
        return CudResult.success(spaceBasicInfoService.edit(param));
    }
}
