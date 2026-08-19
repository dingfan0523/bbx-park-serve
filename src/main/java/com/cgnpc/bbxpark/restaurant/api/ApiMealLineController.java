
package com.cgnpc.bbxpark.restaurant.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.AppMealLineModel;
import com.cgnpc.bbxpark.restaurant.dto.param.AppMealLineListParam;
import com.cgnpc.bbxpark.restaurant.service.IMealLineService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 餐线服务控制类
 * @author dingfan
 * @date 2024/7/18
 */
@RestController
@RequestMapping("/api/mealLine/app")
@Api(tags = "餐线")
public class ApiMealLineController {


    /**
     * 餐线服务接口.
     */
    @Autowired
    private IMealLineService mealLineService;

    /**
     * 移动端-餐线列表
     */
    @ApiOperation(value = "移动端-获取餐线列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<AppMealLineModel>> listApp(@RequestBody @Validated AppMealLineListParam param) {
        return CudResult.success(mealLineService.listApp(param));
    }
}
