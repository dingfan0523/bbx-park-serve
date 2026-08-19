package com.cgnpc.bbxpark.energy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.energy.dto.model.BranchDeviceModel;
import com.cgnpc.bbxpark.energy.dto.param.BranchDeviceInsertParam;
import com.cgnpc.bbxpark.energy.service.IBranchDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/energy/branchDevice")
@Api(tags = "智慧能管-PC端-支路设备控制器")
public class BranchDeviceController {
    @Autowired
    private IBranchDeviceService branchDeviceService;


    @ApiOperation(value = "新增支路设备")
    @PostMapping(value = "/insert")
    public CudResult<Boolean> insert(@RequestBody BranchDeviceInsertParam param) {
        return CudResult.success(branchDeviceService.insert(param));
    }

    @ApiOperation(value = "获取支路设备")
    @GetMapping(value = "/getBranchDevice/{id}")
    public CudResult<List<BranchDeviceModel>> getBranchDevice(@PathVariable(value = "id") Long id) {
        return CudResult.success(branchDeviceService.getBranchDevice(id));
    }

    @ApiOperation(value = "修改支路设备")
    @PostMapping(value = "/updateBranchDevice")
    public CudResult<Boolean> updateBranchDevice(@RequestBody BranchDeviceInsertParam param) {
        return CudResult.success(branchDeviceService.updateBranchDevice(param));
    }

    /**
     * 设备列表(分页).
     */
    @ApiOperation(value = "设备列表(分页).")
    @PostMapping(value = "/getDevices")
    public CudResult<IPage<IocDeviceModel>> page(@RequestBody IocDevicePageParam param) {
        return CudResult.success(branchDeviceService.getDevices(param));
    }
}
