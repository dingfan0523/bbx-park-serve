package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.domain.Combo;
import com.cgnpc.bbxpark.restaurant.dto.model.ComboModel;
import com.cgnpc.bbxpark.restaurant.dto.param.ComboListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.ComboParam;
import com.cgnpc.bbxpark.restaurant.service.IComboService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

import static com.cgnpc.bbxpark.common.constant.Constant.BASE_PATH;


/**
 * <p>
 * 套餐应用控制类
 * </p>
 *
 * @author gujun
 * @time 2024-07-22
 */
@Validated
@RestController
@RequestMapping(BASE_PATH + "/combo")
@Api(tags = "智慧餐厅-PC端-套餐管理")
public class ComboController {

    @Autowired
    private IComboService comboService;

    @ApiOperation(value = "列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<ComboModel>> page(@RequestBody  ComboListParam param) {
        return CudResult.success(comboService.pageResult(param));
    }


    /**
     * 获取套餐列表.
     */
    @ApiOperation(value = "获取套餐列表")
 
    @PostMapping(value = "/list")
    public CudResult<List<ComboModel>> list(@RequestBody ComboListParam param) {
        return CudResult.success(comboService.list(param));
    }


    @ApiOperation(value = "新增")
   
    @PostMapping(value = "/add")
    public CudResult<Long> add(@RequestBody @Validated({InsertGroup.class, Default.class}) ComboParam param) {
        return CudResult.success(comboService.saveOrUpdate(param));
    }

    @ApiOperation(value = "编辑")
   
    @PostMapping(value = "/edit")
    public CudResult<Long> edit(@RequestBody @Validated({InsertGroup.class, Default.class}) ComboParam param) {
        return CudResult.success(comboService.saveOrUpdate(param));
    }

    @ApiOperation(value = "查询套餐")
    
    @GetMapping(value = "/get/{id}")
    public CudResult<ComboModel> get(@PathVariable("id") @NotNull(message = "套餐标识不能为空") Long id) {
        Combo combo = comboService.getById(id);
        return CudResult.success(BeanUtils.convertTo(combo,ComboModel::new));
    }

    @ApiOperation(value = "启用套餐")
    
    @GetMapping(value = "/enable/{id}")
    public CudResult<Boolean> enable(@PathVariable("id") @NotNull(message = "套餐标识不能为空") Long id) {
        return CudResult.success(comboService.update(Wrappers.<Combo>lambdaUpdate().eq(Combo::getId, id).set(Combo::getStatus, Status.enabled.getKey())));
    }
    /**
     * 禁用套餐.
     */
    @ApiOperation(value = "禁用套餐")
    
    @GetMapping(value = "/disable/{id}")
    public CudResult<Boolean> disable(@PathVariable("id") @NotNull(message = "套餐标识不能为空") Long id) {
        return CudResult.success(comboService.update(Wrappers.<Combo>lambdaUpdate().eq(Combo::getId, id).set(Combo::getStatus, Status.disabled.getKey())));
    }
    /**
     * 删除套餐.
     */
    @ApiOperation(value = "删除套餐")
    
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable("id") @NotNull(message = "套餐标识不能为空") Long id) {
        return CudResult.success(comboService.remove(id));
    }

}
