
package com.cgnpc.bbxpark.message.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.message.dto.resp.AppLogisticsGuideDetailModel;
import com.cgnpc.bbxpark.message.dto.resp.AppLogisticsGuideModel;
import com.cgnpc.bbxpark.message.service.ILogisticsGuideService;
import com.cgnpc.mobile.annotation.RequiredToken;
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
@RequestMapping("/api/logistics/guide")
@Api(tags = "后勤指南")
public class ApiLogisticsGuideController {

    /**
     * 后勤指南服务接口.
     */
    @Resource
    private ILogisticsGuideService logisticsGuideService;

    /**
     * 移动端-后勤指南分页列表
     */
    @ApiOperation(value = "移动端-后勤指南分页列表")
    @PostMapping(value = "/app/page")
    @RequiredToken
    public CudResult<IPage<AppLogisticsGuideModel>> pageApp(@RequestBody CudPageDto param) {
        return CudResult.success(logisticsGuideService.pageApp(param));
    }

    /**
     * 移动端-后勤指南列表
     */
    @ApiOperation(value = "移动端-后勤指南列表")
    @PostMapping(value = "/app/list")
    @RequiredToken
    public CudResult<List<AppLogisticsGuideModel>> listApp() {
        return CudResult.success(logisticsGuideService.listApp());
    }

    /**
     * 移动端-后勤指南详情
     */
    @ApiOperation(value = "移动端-后勤指南详情")
    @GetMapping(value = "/app/detail/{id}")
    @RequiredToken
    public CudResult<AppLogisticsGuideDetailModel> detailApp(@PathVariable(value = "id")Long id) {
        return CudResult.success(logisticsGuideService.detailApp(id));
    }
}
