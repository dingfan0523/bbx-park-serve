
package com.cgnpc.bbxpark.restaurant.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentEvaluateModel;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentEvaluatePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentEvaluateParam;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentEvaluateService;
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
import java.util.Optional;


@RestController
@RequestMapping("/api/compartment/evaluate/app")
@Api(tags = "包间评价")
public class ApiCompartmentEvaluateController {


    /**
     * 包间评价服务接口.
     */
    @Autowired
    private ICompartmentEvaluateService compartmentEvaluateService;
    @Autowired
    private IUserApiService userApiService;



    @ApiOperation(value = "获取包间评价列表(分页)")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<CompartmentEvaluateModel>> list(@RequestBody @Validated({Default.class}) CompartmentEvaluatePageParam param) {
        IPage<CompartmentEvaluateModel> page = compartmentEvaluateService.page(param);
        Optional.ofNullable(page).ifPresent(r->anonymityHandle(userApiService.getCurrentStaffNo(), r.getRecords()));
        return CudResult.success(page);
    }

    private void anonymityHandle(String userId, List<CompartmentEvaluateModel> list){
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
     * 新增包间评价.
     */
    @ApiOperation(value = "新增包间评价")
    @PostMapping(value = "/add")
    @RequiredToken
    public CudResult<Boolean> add(@Validated({InsertGroup.class}) @RequestBody CompartmentEvaluateParam param) {
            return CudResult.success(compartmentEvaluateService.add(param));
    }
}
