
package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.domain.Compartment;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentComboModel;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentModel;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentPageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentParam;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;


@RestController
@RequestMapping(Constant.BASE_PATH + "/compartment")
@Api(tags = "包间")
public class CompartmentController {



    /**
     * 包间服务接口.
     */
    @Autowired
    private ICompartmentService compartmentService;
    /**
     * 获取包间信息.
     */
    @ApiOperation(value = "获取包间信息")

    @GetMapping(value = "/get/{id}")
    public CudResult<CompartmentModel> get(@PathVariable("id") @NotNull(message = "包间标识不能为空") Long id) {
        return CudResult.success(compartmentService.get(id));
    }

    /**
     * 获取包间列表(分页).
     */
    @ApiOperation(value = "获取包间列表(分页)")
 
    @PostMapping(value = "/page")
    public CudResult<IPage<CompartmentModel>> page(@RequestBody CompartmentPageParam param) {
        return CudResult.success(compartmentService.pageResult(param));
    }

    /**
     * 获取包间列表.
     */
    @ApiOperation(value = "获取包间列表")
 
    @PostMapping(value = "/list")
    public CudResult<List<CompartmentModel>> list(@RequestBody CompartmentListParam param) {
        return CudResult.success(compartmentService.list(param));
    }

    /**
     * 新增包间.
     */
    @ApiOperation(value = "新增包间")
 
    @PostMapping(value = "/add")
    public CudResult<Long> add(@Validated({Default.class, InsertGroup.class}) @RequestBody CompartmentParam param) {
        return CudResult.success(compartmentService.save(param));
    }


    /**
     * 删除包间.
     */
    @ApiOperation(value = "删除包间")
    
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable("id") @NotNull(message = "包间标识不能为空") Long id) {
        return CudResult.success(compartmentService.deleteById(id));
    }



    /**
     * 编辑包间.
     */
    @ApiOperation(value = "编辑包间")
   
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) CompartmentParam param) {
        return CudResult.success(compartmentService.edit(param));
    }


    /**
     * 启用包间.
     */
    @ApiOperation(value = "启用包间")
    
    @GetMapping(value = "/enable/{id}")
    public CudResult<Boolean> enable(@PathVariable("id") @NotNull(message = "包间标识不能为空") Long id) {
        return CudResult.success(compartmentService.update(Wrappers.<Compartment>lambdaUpdate().eq(Compartment::getId, id).set(Compartment::getStatus, Status.enabled.getKey())));
    }



    /**
     * 禁用包间.
     */
    @ApiOperation(value = "禁用包间")
    
    @GetMapping(value = "/disable/{id}")
    public CudResult<Boolean> disable(@PathVariable("id") @NotNull(message = "包间标识不能为空") Long id) {
        return CudResult.success(compartmentService.disable(id));
    }

    /**
     * 获取包间列表.
     */
    @ApiOperation(value = "获取包间列表")
    @PostMapping(value = "/getCombolistByIds")
    public CudResult<List<CompartmentComboModel>> getCombolistByIds(@RequestBody List<Long> compartmentIds) {
        return CudResult.success(compartmentService.getCombolistByIds(compartmentIds));
    }
}
