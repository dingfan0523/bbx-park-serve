package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.ConstructionPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePageParam;
import com.cgnpc.bbxpark.ioc.dto.param.StationPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.StoragePageParam;
import com.cgnpc.bbxpark.ioc.service.IScreenSpaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping({"/api/dtwin/space"})
@Api(tags = {"大屏-空间统计接口"})
public class ApiScreenSpaceController {
    @Autowired
    private IScreenSpaceService spaceService;

    @GetMapping({"/getSpaceTypeDistribution"})
    @ApiOperation(value = "空间类型分布")
    public CudResult<List<SpaceTypeDistributionModel>> getSpaceTypeDistribution() {
        return CudResult.success(spaceService.getSpaceTypeDistribution());
    }

    @GetMapping({"/getOfficeSpaceOverview"})
    @ApiOperation("办公空间分析概览")
    public CudResult<OfficeSpaceOverviewModel> getOfficeSpaceOverview() {
        return CudResult.success(spaceService.getOfficeSpaceOverview());
    }

    @GetMapping({"/getDeptOfficeSpaceArea"})
    @ApiOperation("部门办公空间分析-面积维度")
    public CudResult<List<DeptOfficeSpaceAreaModel>> getDeptOfficeSpaceArea() {
        return CudResult.success(spaceService.getDeptOfficeSpaceArea());
    }

    @GetMapping({"/getDeptOfficeSpaceStation"})
    @ApiOperation("部门办公空间分析-工位维度")
    public CudResult<List<DeptOfficeSpaceStationModel>> getDeptOfficeSpaceStation() {
        return CudResult.success(spaceService.getDeptOfficeSpaceStation());
    }

    @GetMapping({"/getDeviceDistribution"})
    @ApiOperation("设备资产分布对比")
    public CudResult<List<DeviceSpaceDistributionModel>> getIntelligentDistribution(@ApiParam("空间模型编码 优先拿code查询，code拿不到用id查询") @RequestParam(required = false) String sslcCode,
                                                                                    @ApiParam("空间ID") @RequestParam(required = false) Long spaceId) {
        return CudResult.success(spaceService.getIntelligentDistribution(sslcCode,spaceId));
    }

    @GetMapping({"/getMeetingRoomUseOverview"})
    @ApiOperation("会议室利用率概览")
    public CudResult<MeetingRoomUtilizationOverviewModel> getMeetingRoomUtilizationOverview() {
        return CudResult.success(spaceService.getMeetingRoomUtilizationOverview());
    }

    @GetMapping({"/getMeetingRoomUseAnalysis"})
    @ApiOperation("会议室利用率分析(30天)")
    public CudResult<List<MeetingRoomUtilizationAnalysisModel>> getMeetingRoomUtilizationAnalysis() {
        return CudResult.success(spaceService.getMeetingRoomUtilizationAnalysis());
    }

    @GetMapping({"/getSecurityManagementOverview"})
    @ApiOperation("安全管理概览")
    public CudResult<SecurityManagementOverviewModel> getSecurityManagementOverview() {
        return CudResult.success(spaceService.getSecurityManagementOverview());
    }

    @GetMapping({"/construction/list"})
    @ApiOperation("施工信息列表")
    public CudResult<List<ConstructionModel>> getConstructionList() {
        return CudResult.success(spaceService.getConstructionList());
    }

    @PostMapping({"/construction/page"})
    @ApiOperation("施工信息列表(分页)")
    public CudResult<IPage<ConstructionModel>> getConstructionPage(@RequestBody ConstructionPageParam param) {
        return CudResult.success(spaceService.getConstructionPage(param));
    }

    @GetMapping({"/storage/list"})
    @ApiOperation("物资存放列表")
    public CudResult<List<StorageModel>> getStorageList() {
        return CudResult.success(spaceService.getStorageList());
    }

    @PostMapping({"/storage/page"})
    @ApiOperation("物资存放列表(分页)")
    public CudResult<IPage<StorageModel>> getStoragePage(@RequestBody StoragePageParam param) {
        return CudResult.success(spaceService.getStoragePage(param));
    }

    @GetMapping({"/construction/detail"})
    @ApiOperation("施工详情信息接口")
    public CudResult<ConstructionDetailModel> getConstructionDetail(@ApiParam("施工id") @RequestParam(required = true) Long id) {
        return CudResult.success(spaceService.getConstructionDetail(id));
    }

    @GetMapping({"/storage/detail"})
    @ApiOperation("物资存放详情接口")
    public CudResult<StorageDetailModel> getStorageDetail(@ApiParam("物资id") @RequestParam(required = true) Long id) {
        return CudResult.success(spaceService.getStorageDetail(id));
    }

    @GetMapping(value = "/getSpaceView")
    @ApiOperation(value = "空间楼层高亮展示列表")
    public CudResult<List<SpaceViewModel>> getWorkOrderSpaceView(@ApiParam(value = "所属楼层物模型编码") @RequestParam(required = false) String sslcCode,
                                                                 @ApiParam(value = "空间类型") @RequestParam(required = false) Integer type) {
        return CudResult.success(spaceService.getSpaceView(sslcCode,type));
    }

    @GetMapping({"/list"})
    @ApiOperation("空间信息列表")
    public CudResult<List<SpaceListModel>> getSpaceList(@ApiParam("上级空间编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(spaceService.getSpaceList(sslcCode));
    }

    @GetMapping({"/manager/list"})
    @ApiOperation("空间负责人列表")
    public CudResult<List<ManagerModel>> getManagerList(@RequestParam @ApiParam("空间模型编码") String sslcCode) {
        return CudResult.success(spaceService.getManagerList(sslcCode));
    }

    @GetMapping({"/station/list"})
    @ApiOperation("办公人员列表")
    public CudResult<List<StationModel>> getStationList(@RequestParam(required = false) @ApiParam("空间模型编码,不存在模型编码时传id") String sslcCode,
        @RequestParam(required = false) @ApiParam("空间id") Long id) {
        if (StringUtils.isBlank(sslcCode) && id == null) {
            return CudResult.errorMessage("模型编码或id不能同时为空");
        }
        return CudResult.success(spaceService.getStationList(sslcCode,id));
    }

    @PostMapping({"/station/page"})
    @ApiOperation("办公人员列表(分页)")
    public CudResult<IPage<StationModel>> getStationPage(@RequestBody StationPageParam param) {
        if (StringUtils.isBlank(param.getSslcCode()) && param.getSpaceId() == null) {
            return CudResult.errorMessage("模型编码或id不能同时为空");
        }
        return CudResult.success(spaceService.getStationPage(param));
    }

    @GetMapping({"/image/list"})
    @ApiOperation("空间图片列表")
    public CudResult<List<SpaceImageModel>> getImageList(@RequestParam(required = false) @ApiParam("空间模型编码,不存在模型编码时传id") String sslcCode,
        @RequestParam(required = false) @ApiParam("空间id") Long id) {
        if (StringUtils.isBlank(sslcCode) && id == null) {
            return CudResult.errorMessage("模型编码或id不能同时为空");
        }
        return CudResult.success(spaceService.getImageList(sslcCode,id));
    }

    @PostMapping({"/device/page"})
    @ApiOperation("空间设备分页列表")
    public CudResult<IPage<DeviceListModel>> getDevicePage(@RequestBody DevicePageParam param) {
        if (StringUtils.isBlank(param.getSslcCode()) && param.getSpaceId() == null) {
            return CudResult.errorMessage("模型编码或id不能同时为空");
        }
        return CudResult.success(spaceService.getDevicePage(param));
    }

    @ApiOperation(value = "空间信息详情")
    @GetMapping(value = "/detail")
    public CudResult<SpaceDetailModel> getSpaceDetail(@RequestParam(required = false) @ApiParam("空间模型编码,不存在模型编码时传id") String sslcCode, @RequestParam(required = false) @ApiParam("空间id") Long id) {
        if (StringUtils.isBlank(sslcCode) && id == null) {
            return CudResult.errorMessage("模型编码或id不能同时为空");
        }
        return CudResult.success(spaceService.detail(sslcCode,id));
    }

//    @PostMapping({"/pointList"})
//    @ApiOperation("设备点位列表")
//    public CudResult<List<DevicePointModel>> pointList(@RequestBody DevicePageParam param) {
//        //        （10）进入楼层
////        模型交互进入楼层后，默认展示该楼层所有智能化设备的点位，和该楼层标点的设备图例，同一个末级分组的设备使用相同图标。
////        图例区支持显示和隐藏某分组的图标。
////        用颜色区分设备状态：在线、离线、告警。优先级：告警＞离线＞在线。
//        try {
////            List<DevicePointModel> data = (List)this.readJsonAndConvertList("pointList.json", new TypeReference<List<DevicePointModel>>() {
////            });
//            List<DevicePointModel> data = generateDevicePointData(param);
//            return CudResult.success(data);
//        } catch (Exception e) {
//            log.error("读取数据失败: " + e.getMessage(),  e);
//            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
//        }
//    }

    @GetMapping({"/keywordSearch"})
    @ApiOperation("关键词搜索接口")
    public CudResult<List<KeywordSearchModel>> keywordSearch(@ApiParam("关键词") @RequestParam(required = false) String keyword) {
        return CudResult.success(spaceService.keywordSearch(keyword));
    }
}