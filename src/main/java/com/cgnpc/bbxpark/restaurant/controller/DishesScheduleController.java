
package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesScheduleImortReturnModel;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesScheduleModel;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesScheduleListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesSchedulePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesScheduleParam;
import com.cgnpc.bbxpark.restaurant.service.DishesScheduleExportService;
import com.cgnpc.bbxpark.restaurant.service.DishesScheduleServiceApp;
import com.cgnpc.bbxpark.restaurant.service.IDishesScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;



@RestController
@RequestMapping(Constant.BASE_PATH + "/dishes/schedule")
@Api(tags = "菜品排班")
public class DishesScheduleController {


    /**
     * 菜品排班服务接口.
     */
    @Autowired
    private IDishesScheduleService dishesScheduleService;

    @Autowired
    private DishesScheduleServiceApp dishesScheduleServiceApp;
    @Autowired
    private DishesScheduleExportService dishesScheduleExportService;


    /**
     * 获取菜品排班列表(分页).
     */
    @ApiOperation(value = "获取菜品排班列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<DishesScheduleModel>> page(@RequestBody @Validated DishesSchedulePageParam param) {
        return CudResult.success(dishesScheduleService.page(param));
    }

    /**
     * 获取菜品排班列表.
     */
    @ApiOperation(value = "获取菜品排班列表")
    @PostMapping(value = "/list")
    public CudResult<List<DishesScheduleModel>> list(@RequestBody DishesScheduleListParam param) {
        return CudResult.success(dishesScheduleService.list(param));
    }

    /**
     * 获取菜品排班详情.
     */
    @ApiOperation(value = "获取菜品排班详情")
    @GetMapping(value = "detail/{id}")
    public CudResult<DishesScheduleModel> detail(@PathVariable @NotNull(message = "菜品排班标识不能为空") Long id) {
        return CudResult.success(dishesScheduleService.detail(id));
    }

    /**
     * 新增菜品排班.
     */
    @ApiOperation(value = "新增菜品排班")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@RequestBody @Validated({InsertGroup.class, Default.class}) DishesScheduleParam param) {
        return CudResult.success(dishesScheduleService.add(param));
    }


    /**
     * 批量新增菜品排班.
     */
    @ApiOperation(value = "批量新增菜品排班")
    @PostMapping(value = "/adds")
    public CudResult<Boolean> adds(@RequestBody List<DishesScheduleParam> dishesScheduleParams) {
        return CudResult.success(dishesScheduleService.adds(dishesScheduleParams));
    }


    /**
     * 编辑菜品排班.
     */
    @ApiOperation(value = "编辑菜品排班")
    @PostMapping(value = "edit")
    public CudResult<Boolean> edit(@RequestBody @Validated({UpdateGroup.class, Default.class}) DishesScheduleParam param) {
        return CudResult.success(dishesScheduleService.edit(param));
    }

    /**
     * 上架菜品排班.
     */
    @ApiOperation(value = "上架菜品排班")
    @GetMapping(value = "enable/{id}")
    public CudResult<Boolean> enable(@PathVariable("id") @NotNull(message = "菜品排班标识不能为空") Long id) {
        return CudResult.success(dishesScheduleService.enable(id));
    }

    /**
     * 下架菜品排班.
     */
    @ApiOperation(value = "下架菜品排班")
    @GetMapping(value = "disable/{id}")
    public CudResult<Boolean> disable(@PathVariable("id") @NotNull(message = "菜品排班标识不能为空") Long id) {
        return CudResult.success(dishesScheduleService.disable(id));
    }

    /**
     * 删除菜品排班.
     */
    @ApiOperation(value = "删除菜品排班")
    @GetMapping(value = "remove/{id}")
    public CudResult<Boolean> remove(@PathVariable @NotNull(message = "菜品排班标识不能为空") Long id) {
        return CudResult.success(dishesScheduleService.remove(id));
    }

    /**
     * 菜品周排导入
     *
     */
    @ApiOperation(value = "菜品周排导入")
    @PostMapping(value = "/importSchedule")
    public CudResult<List<DishesScheduleImortReturnModel>> importSchedule(@RequestParam(value = "file") MultipartFile file,
                                                                    @RequestParam(value = "params") String params) {
        return CudResult.success(dishesScheduleServiceApp.importSchedule(file,params));
    }

    /***
     * @Description 菜品周排模板生成
     * @author huangyongtao
     * @date 2024/7/23 15:13
     * @param response
     * @param request
     */
    @ApiOperation(value = "菜品周排模板生成")
    @GetMapping(value = "/template/generate/{restaurantId}")
    public void templateGenerate(@PathVariable("restaurantId")Long restaurantId, HttpServletResponse response, HttpServletRequest request) {
        dishesScheduleExportService.templateGenerate(restaurantId,response, request);
    }
}
