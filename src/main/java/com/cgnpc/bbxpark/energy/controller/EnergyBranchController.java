package com.cgnpc.bbxpark.energy.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchParam;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchQueryParam;
import com.cgnpc.bbxpark.energy.service.IEnergyBranchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 能源支路控制层
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/energy/energyBranch")
@Api(tags = "智慧能管-PC端-支路管理")
@Slf4j
public class EnergyBranchController {

    @Autowired
    private IEnergyBranchService energyBranchService;


    @ApiOperation(value = "新增支路")
    @PostMapping(value = "/insert")
    public CudResult<Boolean> insert(@RequestBody EnergyBranchParam param){
        return CudResult.success(energyBranchService.insert(param));
    }

    @ApiOperation(value = "编辑")
    @PostMapping(value = "/update")
    public CudResult<Boolean> update(@RequestBody EnergyBranchParam param){
        return CudResult.success(energyBranchService.updateById(param));
    }

    @ApiOperation(value = "删除")
    @GetMapping(value = "/delete")
    public CudResult<Boolean> delete(@RequestParam("id") Long id){
        return CudResult.success(energyBranchService.delete(id));
    }

    @ApiOperation(value = "查询分支树状数据")
    @PostMapping(value = "/findTreeList")
    public CudResult<List<EnergyBranchModel>> findTreeList(@RequestBody EnergyBranchQueryParam param){
        return CudResult.success(energyBranchService.findTreeList(param));
    }

    @ApiOperation(value = "启禁用支路")
    @PostMapping(value = "/updateStatus")
    public CudResult<Boolean> updateStatus(@RequestBody EnergyBranchParam param){
        return CudResult.success(energyBranchService.updateStatus(param));
    }

    @ApiOperation(value = "查询分支树状数据（启动的）")
    @PostMapping(value = "/findEnablerTree")
    public CudResult<List<EnergyBranchModel>> findEnablerTreeList(@RequestBody EnergyBranchQueryParam param){
        return CudResult.success(energyBranchService.findEnablerTree(param));
    }

}
