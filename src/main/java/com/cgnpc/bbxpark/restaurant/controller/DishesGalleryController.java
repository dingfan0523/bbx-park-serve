
package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesGalleryModel;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesGalleryListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesGalleryPageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesGalleryParam;
import com.cgnpc.bbxpark.restaurant.service.DishesGalleryServiceApp;
import com.cgnpc.bbxpark.restaurant.service.IDishesGalleryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;


@RestController
@RequestMapping(Constant.BASE_PATH + "/dishes/gallery")
@Api(tags = "菜品库")
public class DishesGalleryController {

    /**
     * Logger.
     */
    private final Logger LOGGER = LoggerFactory.getLogger(DishesGalleryController.class);

    /**
     * 菜品库服务接口.
     */
    @Autowired
    private IDishesGalleryService bbxDishesGalleryService;

    @Autowired
    private DishesGalleryServiceApp dishesGalleryServiceApp;

    /**
     * 新增菜品库.
     */
    @ApiOperation(value = "新增菜品库")
    
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@RequestBody @Validated({InsertGroup.class, Default.class}) DishesGalleryParam param) {
        return CudResult.success(bbxDishesGalleryService.add(param));
    }

    @ApiOperation(value = "菜品库列表(分页)")
    
    @PostMapping(value = "/page")
    public CudResult<IPage<DishesGalleryModel>> page(@RequestBody DishesGalleryPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(bbxDishesGalleryService.pageDishesGalleryModel(param));
    }

    /**
     * 获取菜品库列表.
     */
    @ApiOperation(value = "获取菜品库列表")
    
    @PostMapping(value = "/list")
    public CudResult<List<DishesGalleryModel>> list(@RequestBody DishesGalleryListParam param) {
        return CudResult.success(bbxDishesGalleryService.dishesGalleryModel(param));
    }

    /**
     * 删除菜品库.
     */
    @ApiOperation(value = "删除菜品")
    
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable @NotNull(message = "餐线标识不能为空") Long id) {
        return CudResult.success(bbxDishesGalleryService.removeDishesGallery(id));
    }

    @ApiOperation(value = "菜品库导入")
    @PostMapping(value = "/importDishes")
    public CudResult<Boolean> importSpace(@RequestParam(value = "file") MultipartFile file) {
        return dishesGalleryServiceApp.importDishes(file);
    }
}
