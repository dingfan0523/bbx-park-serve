
package com.cgnpc.bbxpark.settings.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.dto.model.RegionManageModel;
import com.cgnpc.bbxpark.settings.dto.model.RegionSpaceRelationModel;
import com.cgnpc.bbxpark.settings.dto.param.RegionManageListParam;
import com.cgnpc.bbxpark.settings.dto.param.RegionManagePageParam;
import com.cgnpc.bbxpark.settings.dto.param.RegionManageParam;
import com.cgnpc.bbxpark.settings.dto.param.RegionSpaceRelationParam;
import com.cgnpc.bbxpark.settings.service.IRegionManageService;
import com.cgnpc.bbxpark.settings.service.IRegionSpaceRelationService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 区域管理员管理服务控制类
 * @author huangyongtao
 * @date 2025/3/11 11:38
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/region")
@Api(value = "BBX-PC端-智慧物业-区域管理员管理")
public class RegionManageController {

    /**
     * 区域管理员管理服务接口.
     */
    @Autowired
    private IRegionManageService regionManageService;

    @Autowired
    private IRegionSpaceRelationService regionSpaceRelationService;

    /**
     * 获取区域管理员管理列表(分页).
     */
    @ApiOperation(value = "获取区域管理员管理列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<RegionManageModel>> page(@RequestBody RegionManagePageParam param) {
        return CudResult.success(regionManageService.page(param));
    }

    /**
     * 获取区域管理员管理列表.
     */
    @ApiOperation(value = "获取区域管理员管理列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<RegionManageModel>> list(@RequestBody RegionManageListParam param) {
        return CudResult.success(regionManageService.list(param));
    }

    /**
     * 新增区域管理员管理.
     */
    @ApiOperation(value = "新增区域管理员管理")
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> add(@Validated({Default.class}) @RequestBody RegionManageParam param) {
        return CudResult.success(regionManageService.add(param));
    }

    /**
     * 批量新增区域管理员管理.
     */
    @ApiOperation(value = "批量新增区域管理员管理")
    @PostMapping(value = "/add/batch",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> addBatch(@Validated @RequestBody RegionManageParam params) {
        return CudResult.success(regionManageService.addBatch(params));
    }

    /**
     * 删除区域管理员管理.
     */
    @ApiOperation(value = "删除区域管理员管理")
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(regionManageService.remove(id));
    }

    /**
     * 批量关联区域管理员空间.
     */
    @ApiOperation(value = "批量关联区域管理员空间")
    @PostMapping(value = "/space/adds",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> addRegionSpaces(@RequestBody RegionSpaceRelationParam param) {
        return CudResult.success(regionSpaceRelationService.add(param));
    }

    /**
     * 查询关联区域管理员空间.
     */
    @ApiOperation(value = "查询关联区域管理员空间")
    @PostMapping(value = "/space/list",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<RegionSpaceRelationModel> >  findRegionSpaces(@RequestBody RegionSpaceRelationParam param) {
        return CudResult.success(regionSpaceRelationService.list(param));
    }

}
