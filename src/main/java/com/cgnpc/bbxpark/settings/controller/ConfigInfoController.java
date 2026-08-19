package com.cgnpc.bbxpark.settings.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.bbxpark.settings.dto.param.ConfigInfoParam;
import com.cgnpc.bbxpark.settings.dto.param.ConfigListParam;
import com.cgnpc.bbxpark.settings.dto.param.ConfigPageParam;
import com.cgnpc.bbxpark.settings.service.IConfigInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Collections;
import java.util.List;


@Validated
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/config")
@Api(tags = "系统配置", description = "系统配置API")
public class ConfigInfoController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigInfoController.class);

    @Autowired
    private IConfigInfoService configInfoService;

    @ApiOperation(value = "获取系统配置信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<ConfigInfoModel> detail(@PathVariable Long id) {
            return CudResult.success(configInfoService.detail(id));
    }

    @ApiOperation(value = "获取系统配置列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<ConfigInfoModel>> page(@RequestBody ConfigPageParam param) {
        return CudResult.success(configInfoService.page(param));
    }

    @ApiOperation(value = "获取系统配置列表")
    @PostMapping(value = "/list")
    public CudResult<List<ConfigInfoModel>> list(@RequestBody ConfigListParam param) {
        return CudResult.success(configInfoService.list(param)); 
    }

    @ApiOperation(value = "新增系统配置")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated({Default.class, InsertGroup.class}) @RequestBody ConfigInfoParam param) {
        return CudResult.success(configInfoService.addBatch(Collections.singletonList(param)));
    }

    @ApiOperation(value = "批量新增系统配置")
    @PostMapping(value = "/add/batch")
    public CudResult<Boolean> addBatch(@Validated({Default.class, InsertGroup.class}) @RequestBody List<ConfigInfoParam> params) {
        return CudResult.success(configInfoService.addBatch(params));
    }

    @ApiOperation(value = "删除系统配置")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(configInfoService.remove(id));
    }

    @ApiOperation(value = "批量删除系统配置")
    @PostMapping(value = "/remove/batch")
    public CudResult<Boolean> removeBatch(@NotNull @RequestBody List<Long> ids) {
        return CudResult.success(configInfoService.removeBatch(ids));
    }

    @ApiOperation(value = "编辑系统配置")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@Validated({Default.class, UpdateGroup.class}) @RequestBody ConfigInfoParam param) {
        return CudResult.success(configInfoService.edit(param));
    }

    @ApiOperation(value = "启用系统配置")
    @GetMapping(value = "/enable/{id}")
    public CudResult<Boolean> enable(@PathVariable Long id) {
        return CudResult.success(configInfoService.enableBatch(Collections.singletonList(id)));
    }

    @ApiOperation(value = "批量启用系统配置")
    @GetMapping(value = "/enable/batch")
    public CudResult<Boolean> enableBatch(@NotNull @RequestBody List<Long> ids) {
        return CudResult.success(configInfoService.enableBatch(ids));
    }

    @ApiOperation(value = "禁用系统配置")
    @GetMapping(value = "/disable/{id}")
    public CudResult<Boolean> disable(@PathVariable Long id) {
        return CudResult.success(configInfoService.disableBatch(Collections.singletonList(id)));
    }

    @ApiOperation(value = "批量禁用系统配置")
    @GetMapping(value = "/disable/batch")
    public CudResult<Boolean> disableBatch(@NotNull @RequestBody List<Long> ids) {
        return CudResult.success(configInfoService.disableBatch(ids));

    }

    @ApiOperation(value = "根据code获取系统配置信息")
    @GetMapping(value = "/getByCodeDetail/{code}")
    public CudResult<ConfigInfoModel> getByCodeDetail(@PathVariable String code) {
        return CudResult.success(configInfoService.getByCodeDetail(code));
    }
}
