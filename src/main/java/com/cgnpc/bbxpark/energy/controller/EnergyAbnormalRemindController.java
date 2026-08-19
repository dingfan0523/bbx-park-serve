package com.cgnpc.bbxpark.energy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.energy.dto.model.EnergyAbnormalRemindModel;
import com.cgnpc.bbxpark.energy.dto.param.EnergyAbnormalRemindParam;
import com.cgnpc.bbxpark.energy.service.IEnergyAbnormalRemindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @create zhaoshuo
 * @time 2025/4/22
 * @desc 能源异常提醒控制器
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/energy/energyAbnormalRemind")
@Api(tags = "智慧能管-PC端-能源异常提醒控制器")
public class EnergyAbnormalRemindController {

    @Autowired
    private IEnergyAbnormalRemindService energyAbnormalRemindService;

    @ApiOperation(value = "快速报单")
    @GetMapping(value = "/submitOrder/{id}")
    public CudResult<Boolean> submitOrder(@PathVariable(value = "id") Long id) {
        return CudResult.success(energyAbnormalRemindService.submitProblem(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/queryPage")
    public CudResult<IPage<EnergyAbnormalRemindModel>> queryPage(@RequestBody EnergyAbnormalRemindParam param) {
        return CudResult.success(energyAbnormalRemindService.queryPage(param));
    }

    @ApiOperation(value = "搜索同比能耗异常偏差")
    @GetMapping(value = "/searchEnergyAbnormal")
    public void searchEnergyAbnormal() {
        energyAbnormalRemindService.searchEnergyAbnormal();
    }

    @ApiOperation(value = "搜索休息时段能耗异常偏差")
    @GetMapping(value = "/searchSleepEnergy")
    public void searchSleepEnergy() {
        energyAbnormalRemindService.searchSleepEnergy();
    }
}
