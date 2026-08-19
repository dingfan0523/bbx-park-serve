package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.acl.haikang.model.PlayBackURLsModel;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupTreeModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupListParam;
import com.cgnpc.bbxpark.device.service.IDeviceGroupService;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.AlarmAnalysisParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePageParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePointParam;
import com.cgnpc.bbxpark.ioc.service.IScreenDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dtwin/device")
@Api(tags = "大屏-设备统计接口")
public class ApiScreenDeviceController {

    @Autowired
    private IScreenDeviceService screenDeviceService;
    @Autowired
    private IDeviceGroupService deviceGroupService;

    @GetMapping("/getDeviceOverview")
    @ApiOperation("设备资产总览")
    public CudResult<DeviceOverviewModel> getDeviceOverview(
            @ApiParam(value = "空间模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(screenDeviceService.getDeviceOverview(sslcCode));
    }

    @GetMapping("/getDeptDeviceDistribution")
    @ApiOperation("部门设备分布")
    public CudResult<List<DeviceDeptDistributionModel>> getDeptDeviceDistribution(
            @ApiParam(value = "空间模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(screenDeviceService.getDeptDeviceDistribution(sslcCode));
    }

    @GetMapping("/getLifeDistribution")
    @ApiOperation("设备寿命分布")
    public CudResult<DeviceLifeDistributionModel> getLifeDistribution(
            @ApiParam(value = "空间模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(screenDeviceService.getLifeDistribution(sslcCode));
    }

    @GetMapping("/getDepreciationAnalysis")
    @ApiOperation("设备折旧分析")
    public CudResult<DeviceDepreciationAnalysisModel> getDepreciationAnalysis(
            @ApiParam(value = "空间模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(screenDeviceService.getDepreciationAnalysis(sslcCode));
    }

    @GetMapping("/getIntelligentOverview")
    @ApiOperation("智能化设备总览")
    public CudResult<IntelligentDeviceOverview> getIntelligentOverview(
            @ApiParam(value = "空间模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(screenDeviceService.getIntelligentOverview(sslcCode));
    }

    @GetMapping("/getIntelligentDistribution")
    @ApiOperation("智能化设备分布")
    public CudResult<List<DeviceSpaceDistributionModel>> getIntelligentDistribution(
            @ApiParam(value = "空间模型编码") @RequestParam(required = false) String sslcCode,
            @ApiParam(value = "空间id") @RequestParam(required = false) Long spaceId) {
        return CudResult.success(screenDeviceService.getIntelligentDistribution(sslcCode,spaceId));
    }

    @GetMapping("/getHealthTrend")
    @ApiOperation("设备健康趋势")
    public CudResult<List<DeviceHealthTrendModel>> getHealthTrend(
            @ApiParam(value = "空间模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(screenDeviceService.getHealthTrend(sslcCode));
    }

    @PostMapping("/getAlarmAnalysis")
    @ApiOperation("设备告警分析")
    public CudResult<List<AlarmAnalysisModel>> getAlarmAnalysis(@RequestBody AlarmAnalysisParam param) {
        return CudResult.success(screenDeviceService.getAlarmAnalysis(param));
    }

    /**
     *  查询设备分组树
     */
    @ApiOperation(value = "查询设备分组树")
    @PostMapping(value = "/groupTree")
    public CudResult<List<DeviceGroupTreeModel>> groupTree() {
        return CudResult.success(deviceGroupService.findTree(new DeviceGroupListParam()));
    }

    @PostMapping("/pointList")
    @ApiOperation("设备点位列表")
    public CudResult<List<DevicePointModel>> pointList(@RequestBody DevicePointParam param) {
        return CudResult.success(screenDeviceService.pointList(param));
    }

    @PostMapping("/list")
    @ApiOperation("设备列表")
    public CudResult<IPage<DeviceModel>> list(@RequestBody DevicePageParam param) {
        return CudResult.success(screenDeviceService.list(param));
    }

    @GetMapping("/detail")
    @ApiOperation("设备详情信息")
    public CudResult<DeviceDetailModel> detail(@RequestParam("id") @ApiParam(value = "设备id") Long id) {
        return CudResult.success(screenDeviceService.detail(id));
    }

    @GetMapping("/summary/detail")
    @ApiOperation("设备概览信息")
    public CudResult<DeviceSummaryModel> summary(@RequestParam("id") @ApiParam(value = "设备id") Long id) {
        return CudResult.success(screenDeviceService.summaryDetail(id));
    }

    @GetMapping({"/product/list"})
    @ApiOperation("产品列表")
    public CudResult<List<ProductListModel>> getProductList() {
        return CudResult.success(screenDeviceService.productList());
    }

    /**
     * 获取摄像头实时预览视频流地址-海康
     * @param id 设备id
     * @return PlayBackURLsDTO 视频流地址信息
     */
    @GetMapping({"/camera/preview/hk"})
    @ApiOperation("获取摄像头实时预览视频流地址")
    public CudResult<PlayBackURLsModel> getHKCameraPreviewUrl(
            @RequestParam("id") @ApiParam("设备id") Long id,
            @RequestParam(value = "streamType", defaultValue = "1") @ApiParam("码流类型 0:主码流 1:子码流") Integer streamType) {
        return CudResult.success(screenDeviceService.getHKCameraPreviewUrl(id, streamType));
    }
}
