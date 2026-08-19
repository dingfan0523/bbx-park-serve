package com.cgnpc.bbxpark.restaurant.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantSpaceModel;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantPageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantSpaceParam;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantService;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@Slf4j
@Validated
@RestController
@RequestMapping("/api/app/restaurant")
@Api(tags = "智慧餐厅-移动端-餐厅管理")
public class ApiRestaurantController {

    /**
     * 餐厅服务.
     */
    @Autowired
    private IRestaurantService restaurantService;
    /**
     * 获取餐厅列表(分页).
     */
    @ApiOperation(value = "获取餐厅列表(分页)")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<RestaurantModel>> page(@RequestBody @Validated RestaurantPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(restaurantService.pageAppRestaurantModel(param));
    }

    /**
     * 获取餐厅列表.
     */
    @ApiOperation(value = "获取餐厅列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<RestaurantModel>> list(@RequestBody RestaurantListParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(restaurantService.listAppRestaurantModel(param));
    }

    /**
     * 获取餐厅详情.
     */
    @ApiOperation(value = "获取餐厅详情")
    @GetMapping(value = "/detail/{id}")
    @RequiredToken
    public CudResult<RestaurantModel> detail(@PathVariable @NotNull(message = "餐厅标识不能为空") Long id) {
            return CudResult.success(restaurantService.detail(id));
    }

    /**
     * 空间图片查询接口.
     */
    @ApiOperation(value = "空间图片查询接口")
    @PostMapping(value = "/image/list")
    @RequiredToken
    public CudResult<List<RestaurantSpaceModel>> imgList(@RequestBody RestaurantSpaceParam param) {
        return CudResult.success(restaurantService.imgList(param));
    }

    /**
     * 餐厅列表上通知查询入口.
     */
    @ApiOperation(value = "餐厅列表顶部通知查询")
    @PostMapping(value = "/config/notic")
    @RequiredToken
    public CudResult<ConfigInfoModel> configNotic(@RequestBody ConfigInfoModel param) {
        return CudResult.success(restaurantService.configNotic(param));
    }

    /**
     * 餐厅列表上通知查询入口.
     */
    @ApiOperation(value = "餐厅列表顶部通知查询")
    @GetMapping(value = "/config/notic/{tenantId}")
    @RequiredToken
    public CudResult<ConfigInfoModel> configNotice(@PathVariable @NotNull(message = "园区id不能为空") Long tenantId) {
        return CudResult.success(restaurantService.configNotice(tenantId));
    }
}
