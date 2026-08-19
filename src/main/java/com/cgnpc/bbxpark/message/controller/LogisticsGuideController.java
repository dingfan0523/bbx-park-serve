
package com.cgnpc.bbxpark.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.message.dto.req.LogisticsGuideListParam;
import com.cgnpc.bbxpark.message.dto.req.LogisticsGuidePageParam;
import com.cgnpc.bbxpark.message.dto.req.LogisticsGuideParam;
import com.cgnpc.bbxpark.message.dto.resp.LogisticsGuideDetailModel;
import com.cgnpc.bbxpark.message.dto.resp.LogisticsGuideListModel;
import com.cgnpc.bbxpark.message.service.ILogisticsGuideService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后勤指南服务控制类
 * @author dingfan
 * @date 2024/10/12 13:56
 */
@Validated
@RestController
@RequestMapping(Constant.BASE_PATH + "/logistics/guide")
@Api(tags = "后勤指南")
public class LogisticsGuideController {

    /**
     * 后勤指南服务接口.
     */
    @Resource
    private ILogisticsGuideService logisticsGuideService;

    /**
     * PC端-后勤指南分页列表
     */
    @ApiOperation(value = "PC端-后勤指南分页列表")
    @PostMapping(value = "/page")
    public CudResult<IPage<LogisticsGuideListModel>> page(@RequestBody LogisticsGuidePageParam param) {
        return CudResult.success(logisticsGuideService.page(param));
    }

    /**
     * PC端-后勤指南列表
     */
    @ApiOperation(value = "PC端-后勤指南列表")
    @PostMapping(value = "/list")
    public CudResult<List<LogisticsGuideListModel>> list(@RequestBody LogisticsGuideListParam param) {
        return CudResult.success(logisticsGuideService.list(param));
    }

    /**
     * PC端-后勤指南详情
     */
    @ApiOperation(value = "PC端-后勤指南详情")
    @GetMapping(value = "/detail/{id}")
    public CudResult<LogisticsGuideDetailModel> detail(@PathVariable(value = "id")Long id) {
        return CudResult.success(logisticsGuideService.detail(id));
    }

    /**
     * 新增后勤指南.
     */
    @ApiOperation(value = "PC端-新增后勤指南")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated @RequestBody LogisticsGuideParam param) {
        return CudResult.success(logisticsGuideService.add(param));
    }

    /**
     * 编辑后勤指南.
     */
    @ApiOperation(value = "PC端-编辑后勤指南")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated LogisticsGuideParam param) {
        return CudResult.success(logisticsGuideService.edit(param));
    }

    /**
     * 删除后勤指南.
     */
    @ApiOperation(value = "PC端-删除后勤指南")
    @PostMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable(value = "id")Long id) {
        return CudResult.success(logisticsGuideService.remove(id));
    }
}
