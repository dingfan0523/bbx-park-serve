
package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantSpaceModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantService;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantSpaceService;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/restaurant")
@Api(tags = "智慧餐厅-PC端-餐厅管理")
public class RestaurantController {
    /**
     * 餐厅服务接口.
     */
    @Autowired
    private IRestaurantService restaurantService;

    @Autowired
    private IRestaurantTimeService restaurantTimeService;
    @Autowired
    private IRestaurantSpaceService restaurantSpaceService;


    /**
     * 新增餐厅.
     */
    @ApiOperation(value = "新增餐厅")
    
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated({InsertGroup.class}) @RequestBody RestaurantParam param) {
        return CudResult.success(restaurantService.add(param));
    }
    /**
     * 编辑餐厅.
     */
    @ApiOperation(value = "编辑餐厅")
    
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated({UpdateGroup.class})  RestaurantParam param) {
        return CudResult.success(restaurantService.edit(param));
    }
    /**
     * 获取餐厅列表(分页).
     */
    @ApiOperation(value = "获取餐厅列表(分页)")
    
    @PostMapping(value = "/page")
    public CudResult<IPage<RestaurantModel>> page(@RequestBody RestaurantPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(restaurantService.pageRestaurantModel(param));
    }

    /**
     * 获取餐厅列表.
     */
    @ApiOperation(value = "获取餐厅列表")
    
    @PostMapping(value = "/list")
    public CudResult<List<RestaurantModel>> list(@RequestBody RestaurantListParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(restaurantService.listRestaurantModel(param));
    }

    /**
     * 获取餐厅列表.
     */
    @ApiOperation(value = "获取餐厅列表")
    
    @PostMapping(value = "/listSimple")
    public CudResult<List<RestaurantModel>> listSimple(@RequestBody RestaurantListParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(restaurantService.listRestaurantModel(param));
    }

    /**
      * 获取餐厅详情.
    */
    @ApiOperation(value = "获取餐厅详情")
    
    @GetMapping(value = "/detail/{id}")
    public CudResult<RestaurantModel> detail(@PathVariable @NotNull(message = "餐厅标识不能为空") Long id) {
        return CudResult.success(restaurantService.detail(id));
    }

    /**
      * 餐厅通知保存接口.
    */
    @ApiOperation(value = "通知保存接口")
    @PostMapping(value = "/notification/save")
    public CudResult<Boolean> notification(@RequestBody @Validated({UpdateGroup.class, Default.class}) RestaurantNotificationParam param) {
        return CudResult.success(restaurantService.notificationSave(param));
    }


    /**
     * 启用餐厅.
     */
    @ApiOperation(value = "启用餐厅")
    
    @PostMapping(value = "/enable/{id}")
    public CudResult<Boolean> enable(@PathVariable("id") @NotNull(message = "餐厅标识不能为空") Long id) {
        return CudResult.success(restaurantService.enable(id));
    }

    /**
     * 禁用餐厅.
     */
    @ApiOperation(value = "禁用餐厅")
    
    @PostMapping(value = "/disable/{id}")
    public CudResult<Boolean> disable(@PathVariable("id") @NotNull(message = "餐厅标识不能为空") Long id) {
        return CudResult.success(restaurantService.disable(id));
    }

    /**
     * 删除餐厅.
     */
    @ApiOperation(value = "删除餐厅")
    
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable @NotNull(message = "餐厅标识不能为空") Long id) {
        return CudResult.success(restaurantService.removeRestaurantById(id));
    }
    /**
     * 营业时间查询.
     */
    @ApiOperation(value = "营业时间查询")
    
    @PostMapping(value = "/time/list")
    public CudResult<List<RestaurantTimeModel>> timeList(@RequestBody RestaurantTimeListParam param) {
        return CudResult.success(restaurantTimeService.timeList(param));
    }

    /**
     *  空间位置图片保存接口
     */
    @ApiOperation(value = "空间位置图片保存接口")
    
    @PostMapping(value = "/image/save")
    public CudResult<Boolean>  imageSave(@RequestBody @Validated({UpdateGroup.class})  List<RestaurantSpaceListParam> spaceList) {
        return CudResult.success(restaurantSpaceService.saveImg(spaceList));
    }

    /**
     * 空间图片查询接口.
     */
    @ApiOperation(value = "空间图片查询接口")
    
    @PostMapping(value = "/image/list")
    public CudResult<List<RestaurantSpaceModel>> imgList(@RequestBody RestaurantSpaceParam param) {
        return CudResult.success(restaurantService.imgList(param));
    }
}
