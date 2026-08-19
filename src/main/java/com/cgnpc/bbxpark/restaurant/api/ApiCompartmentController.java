
package com.cgnpc.bbxpark.restaurant.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.AppComboModel;
import com.cgnpc.bbxpark.restaurant.dto.model.AppCompartmentModel;
import com.cgnpc.bbxpark.restaurant.dto.param.AppComboListParam;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/api/compartment/app")
@Api(tags = "包间")
public class ApiCompartmentController {



    /**
     * 包间服务接口.
     */
    @Autowired
    private ICompartmentService compartmentService;

    /**
     * 移动端-包间详情
     */
    @ApiOperation(value = "移动端-获取包间详情")
    @GetMapping(value = "/detail/{id}")
    @RequiredToken
    public CudResult<AppCompartmentModel> detail(@PathVariable @NotNull(message = "包间标识不能为空") Long id) {
        return CudResult.success(compartmentService.detailApp(id));
    }

    /**
     * 获取包间列表.
     */
    @ApiOperation(value = "获取包间列表")
    @PostMapping(value = "/combo/list")
    @RequiredToken
    public CudResult<List<AppComboModel>> findComboListById(@RequestBody AppComboListParam param) {
        return CudResult.success(compartmentService.findComboListById(param));
    }
}
