package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.enums.DeviceMeterMethodEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordModel;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchQueryParam;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordPageParam;
import com.cgnpc.bbxpark.energy.service.IEnergyBranchService;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordService;
import com.cgnpc.bbxpark.ioc.dto.model.KeywordSearchModel;
import com.cgnpc.bbxpark.ioc.service.IScreenDeviceService;
import com.cgnpc.bbxpark.property.dto.model.MeterReadingPlanModel;
import com.cgnpc.bbxpark.property.dto.param.MeterReadingPlanDeviceParam;
import com.cgnpc.bbxpark.property.dto.param.MeterReadingPlanPageParam;
import com.cgnpc.bbxpark.property.service.IMeterReadingPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 大屏工单统计
 */
@RestController
@RequestMapping("/api/dtwin")
@Api(tags= "大屏-能耗公共模块接口")
@Slf4j
public class ApiEnergyCommonScreenController {

    @Autowired
    private IMeterReadingPlanService meterReadingPlanService;
    @Autowired
    private IMeterAutoRecordService meterAutoRecordService;
    @Autowired
    private IMeterPersonRecordService meterPersonRecordService;
    @Autowired
    private IEnergyBranchService energyBranchService;
    @Autowired
    private IScreenDeviceService screenDeviceService;


    /**
     * 获取抄表计划管理列表(分页).
     */
    @ApiOperation(value = "获取抄表计划管理列表(分页)")
    @PostMapping(value = "/readingPlan/page")
    public CudResult<IPage<MeterReadingPlanModel>> page(@RequestBody MeterReadingPlanPageParam param) {
        return CudResult.success(meterReadingPlanService.page(param));
    }
/**
     * 查询抄表设备列表.
     */
    @ApiOperation(value = "查询抄表设备列表")
    @PostMapping(value = "/readingPlan/findDeviceList")
    public CudResult<List<IocDeviceModel>> findDeviceList(@RequestBody MeterReadingPlanDeviceParam param) {
            return CudResult.success(meterReadingPlanService.findDeviceList(param));
    }

    @ApiOperation(value = "抄表记录列表(分页)")
    @PostMapping(value = "/readingPlan/record/page")
    public CudResult<IPage<MeterRecordModel>> autoPage(@RequestBody MeterRecordPageParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            return CudResult.success(meterAutoRecordService.page(param));
        }else{
            return CudResult.success(meterPersonRecordService.page(param));
        }
    }

    @ApiOperation(value = "支路树状数据")
    @PostMapping(value = "/branch/tree")
    public CudResult<List<EnergyBranchModel>> tree(@ApiParam(value = "支路类型:electricity->电;water->水")@RequestParam String branchType){
        EnergyBranchQueryParam param = new EnergyBranchQueryParam();
        param.setBranchType(branchType);
        //param.setStatus(Status.enabled.getKey());
        return CudResult.success(energyBranchService.findTreeList(param));
    }

    @GetMapping({"/device/search"})
    @ApiOperation("抄表设备检索接口")
    public CudResult<List<KeywordSearchModel>> keywordSearch(@ApiParam("关键词") @RequestParam(required = false) String keyword) {
        return CudResult.success(screenDeviceService.search(keyword));
    }
}
