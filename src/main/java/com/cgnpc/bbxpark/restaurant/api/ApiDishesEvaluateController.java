
package com.cgnpc.bbxpark.restaurant.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesEvaluateModel;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesEvaluatePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesEvaluateParam;
import com.cgnpc.bbxpark.restaurant.service.IDishesEvaluateService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/dishes/evaluate/app")
@Api(tags = "菜品评价")
public class ApiDishesEvaluateController {


    /**
     * 菜品评价服务接口.
     */
    @Autowired
    private IDishesEvaluateService dishesEvaluateService;

    @ApiOperation(value = "获取菜品评价列表(分页)")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<DishesEvaluateModel>> appPage(@RequestBody @Validated({Default.class}) DishesEvaluatePageParam param) {
        IPage<DishesEvaluateModel> pageList = dishesEvaluateService.page(param);
        anonymityHandle(WebFrameworkUtils.getHeaderUserId(),pageList.getRecords());
        return CudResult.success(pageList);
    }

    private void anonymityHandle(String userId, List<DishesEvaluateModel> list){
        list.forEach(model->{
            if(Objects.equals(model.getAnonymityStatus(), Status.enabled.getKey())){
                model.setAppraiserName(model.getAppraiserId().equals(userId) ? "匿名(我)":"匿名");
            }
            //其他字段置空
            model.setAppraiserId(null);
            model.setAnonymityStatus(null);
        });
    }

    /**
     * 新增菜品评价.
     */
    @ApiOperation(value = "新增菜品评价")
    @PostMapping(value = "/add")
    @RequiredToken
    public CudResult<Boolean> add(@RequestBody @Validated({InsertGroup.class, Default.class}) DishesEvaluateParam param) {
        return CudResult.success(dishesEvaluateService.add(param));
    }
}
