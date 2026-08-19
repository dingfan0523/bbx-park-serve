package com.cgnpc.bbxpark.energy.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.energy.dto.param.BranchEnergyFlowQueryParam;
import com.cgnpc.bbxpark.energy.service.IBranchEnergyFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/4/24
 * @desc 支路能耗流向图
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/energy/branchEnergyFlow")
@Api(tags = "智慧能管-PC端-支路能耗流向图")
public class BranchEnergyFlowController {

    @Autowired
    private IBranchEnergyFlowService branchEnergyFlowService;

    @ApiOperation(value = "查询数据")
    @PostMapping(value = "/queryData")
    public CudResult<List<EnergyBranchModel>> queryData(@RequestBody BranchEnergyFlowQueryParam param){
        return CudResult.success(branchEnergyFlowService.queryData(param));
    }

    /***
     *生成自动抄表集抄数据
     */
    @ApiOperation(value = "生成支路异常提醒")
    @PostMapping(value = "/executeAutoReadingRemind")
    public CudResult<Boolean> executeAutoReadingRemind() {
        branchEnergyFlowService.executeAutoReadingRemind(DeviceReadingTypeEnum.ELECTRICITY.getCode());
        branchEnergyFlowService.executeAutoReadingRemind(DeviceReadingTypeEnum.WATER.getCode());
        return CudResult.success(true);
    }


}
