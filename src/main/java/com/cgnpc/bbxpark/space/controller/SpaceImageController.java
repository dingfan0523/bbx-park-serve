
package com.cgnpc.bbxpark.space.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.SpaceImageModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceImageListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceImageParam;
import com.cgnpc.bbxpark.space.service.ISpaceImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(Constant.BASE_PATH + "/space/image")
@Api(tags = "智慧空间-空间图片")
public class SpaceImageController {


    /**
     * 空间图片服务接口.
     */
    @Autowired
    private ISpaceImageService spaceImageService;

    /**
     * 获取空间图片信息.
     */
    @ApiOperation(value = "获取空间图片信息")
    @GetMapping(value = "detail/{id}")
    public CudResult<SpaceImageModel> detail(@PathVariable Long id) {
        return CudResult.success(spaceImageService.detail(id));
    }

    /**
     * 获取空间图片列表.
     */
    @ApiOperation(value = "获取空间图片列表")
    @PostMapping(value = "/list")
    public CudResult<List<SpaceImageModel>> list(@RequestBody SpaceImageListParam param) {
        return CudResult.success(spaceImageService.list(param));
    }

    /**
     * 新增空间图片.
     */
    @ApiOperation(value = "新增空间图片")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated @RequestBody SpaceImageParam param) {
        return CudResult.success(spaceImageService.add(param));
    }

    /**
     * 编辑空间图片.
     */
    @ApiOperation(value = "编辑空间图片")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated SpaceImageParam param) {
        return CudResult.success(spaceImageService.edit(param));
    }

    /**
     * 删除空间图片.
     */
    @ApiOperation(value = "删除空间图片")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(spaceImageService.remove(id));
    }
}
