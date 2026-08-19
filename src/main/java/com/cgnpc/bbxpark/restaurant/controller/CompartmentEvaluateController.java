
package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentEvaluateModel;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentEvaluatePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentEvaluateParam;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentEvaluateService;
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
import java.util.Optional;


@RestController
@RequestMapping(Constant.BASE_PATH + "/compartment/evaluate")
@Api(tags = "包间评价")
public class CompartmentEvaluateController {


    /**
     * 包间评价服务接口.
     */
    @Autowired
    private ICompartmentEvaluateService compartmentEvaluateService;

    /**
     * 获取包间评价列表(分页).
     */
    @ApiOperation(value = "获取包间评价列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<CompartmentEvaluateModel>> page(@Validated({Default.class}) @RequestBody CompartmentEvaluatePageParam param) {
            return CudResult.success(compartmentEvaluateService.page(param));
    }
}
