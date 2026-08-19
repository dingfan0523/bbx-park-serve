package com.cgnpc.bbxpark.energy.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchQueryParam;
import com.cgnpc.bbxpark.energy.service.IEnergyBranchService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 能源支路控制层
 */
@RestController
@RequestMapping("/api/energy/energyBranch")
@Api(tags = "智慧能管-PC端-支路管理")
@Slf4j
public class ApiEnergyBranchController {

    @Autowired
    private IEnergyBranchService energyBranchService;

    @ApiOperation(value = "查询分支树状数据（启动的）")
    @PostMapping(value = "/findEnablerTree")
    @RequiredToken
    public CudResult<List<EnergyBranchModel>> findEnablerTreeList(@RequestBody EnergyBranchQueryParam param){
        return CudResult.success(energyBranchService.findEnablerTree(param));
    }

}
