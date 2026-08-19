
package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.AppMealLineModel;
import com.cgnpc.bbxpark.restaurant.dto.model.MealLineModel;
import com.cgnpc.bbxpark.restaurant.dto.param.AppMealLineListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.MealLineListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.MealLinePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.MealLineParam;
import com.cgnpc.bbxpark.restaurant.service.IMealLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;


/**
 * 餐线服务控制类
 * @author dingfan
 * @date 2024/7/18
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/mealLine")
@Api(tags = "餐线")
public class MealLineController {


    /**
     * 餐线服务接口.
     */
    @Autowired
    private IMealLineService mealLineService;

    /**
     * 餐线详情
     */
    @ApiOperation(value = "餐线详情")
    @GetMapping(value = "/detail/{id}")
    public CudResult<MealLineModel> detail(@PathVariable @NotNull(message = "餐线标识不能为空") Long id) {
        return CudResult.success(mealLineService.detail(id));
    }

    /**
     * 获取餐线列表(分页).
     */
    @ApiOperation(value = "获取餐线列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MealLineModel>> page(@RequestBody @Validated MealLinePageParam param) {
        return CudResult.success(mealLineService.page(param));
    }

    /**
     * 餐线列表
     */
    @ApiOperation(value = "获取餐线列表(不分页)")
    @PostMapping(value = "/list")
    public CudResult<List<MealLineModel>> list(@RequestBody @Validated MealLineListParam param) {
        return CudResult.success(mealLineService.list(param));
    }

    /**
     * 新增餐线
     */
    @ApiOperation(value = "新增餐线")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@RequestBody @Validated({InsertGroup.class, Default.class}) MealLineParam param) {
        return CudResult.success(mealLineService.add(param));
    }

    /**
     * 编辑餐线.
     */
    @ApiOperation(value = "编辑餐线")
    @PostMapping(value = "edit")
    public CudResult<Boolean> edit(@RequestBody @Validated({UpdateGroup.class, Default.class}) MealLineParam param) {
        return CudResult.success(mealLineService.edit(param));
    }

    /**
     * 启用餐线.
     */
    @ApiOperation(value = "启用餐线")
    @GetMapping(value = "enable/{id}")
    public CudResult<Boolean> enable(@PathVariable("id") @NotNull(message = "餐线标识不能为空") Long id) {
        return CudResult.success(mealLineService.enable(id));
    }

    /**
     * 禁用餐线.
     */
    @ApiOperation(value = "禁用餐线")
    @GetMapping(value = "disable/{id}")
    public CudResult<Boolean> disable(@PathVariable("id") @NotNull(message = "餐线标识不能为空") Long id) {
        return CudResult.success(mealLineService.disable(id));
    }

    /**
     * 删除餐线.
     */
    @ApiOperation(value = "删除餐线")
    @GetMapping(value = "remove/{id}")
    public CudResult<Boolean> remove(@PathVariable @NotNull(message = "餐线标识不能为空") Long id) {
        return CudResult.success(mealLineService.remove(id));
    }
}
