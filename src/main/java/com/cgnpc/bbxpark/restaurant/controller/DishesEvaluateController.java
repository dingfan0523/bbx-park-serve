
package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesEvaluateModel;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesEvaluatePageParam;
import com.cgnpc.bbxpark.restaurant.service.IDishesEvaluateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;



@RestController
@RequestMapping(Constant.BASE_PATH + "/dishes/evaluate")
@Api(tags = "菜品评价")
public class DishesEvaluateController {


    /**
     * 菜品评价服务接口.
     */
    @Autowired
    private IDishesEvaluateService dishesEvaluateService;

    /**
     * 获取菜品排班列表.
     */
    @ApiOperation(value = "获取菜品评价列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<DishesEvaluateModel>> page(@RequestBody @Validated({Default.class}) DishesEvaluatePageParam param) {
        return CudResult.success(dishesEvaluateService.page(param));
    }
}
