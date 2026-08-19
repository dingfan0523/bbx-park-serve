
package com.cgnpc.bbxpark.restaurant.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.AppDishesScheduleModel;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesTypeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.AppDishesScheduleListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesTypeListParam;
import com.cgnpc.bbxpark.restaurant.service.DishesScheduleServiceApp;
import com.cgnpc.bbxpark.restaurant.service.IDishesScheduleService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;


@RestController
@RequestMapping("/api/dishes/schedule/app")
@Api(tags = "菜品排班")
public class ApiDishesScheduleController {


    /**
     * 菜品排班服务接口.
     */
    @Autowired
    private IDishesScheduleService dishesScheduleService;

    @Autowired
    private DishesScheduleServiceApp dishesScheduleServiceApp;

    /**
     * 移动端-获取菜品排班列表.
     */
    @ApiOperation(value = "移动端-获取菜品排班列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<AppDishesScheduleModel>> listApp(@RequestBody AppDishesScheduleListParam param) {
        return CudResult.success(dishesScheduleService.listApp(param));
    }

    /**
     * 移动端-获取菜品排班列表.
     */
    @ApiOperation(value = "获取菜品类型列表")
    @PostMapping(value = "/type/list")
    @RequiredToken
    public CudResult<List<DishesTypeModel>> typeList(@RequestBody @Validated({Default.class}) DishesTypeListParam param) {
        return CudResult.success(dishesScheduleServiceApp.listType(param));
    }

    /**
     * 移动端-获取菜品排班详情.
     */
    @ApiOperation(value = "移动端-获取菜品排班详情")
    @GetMapping(value = "/detail/{id}")
    @RequiredToken
    public CudResult<AppDishesScheduleModel> detailApp(@PathVariable @NotNull(message = "菜品排班标识不能为空") Long id) {
        return CudResult.success(dishesScheduleService.detailApp(id));
    }

}
