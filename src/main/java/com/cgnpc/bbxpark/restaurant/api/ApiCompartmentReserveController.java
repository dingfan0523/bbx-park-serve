
package com.cgnpc.bbxpark.restaurant.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.enums.CompartmentReserveTypeEnum;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.*;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentReserveService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;

/***
 * @Description 包间预定服务控制类
 * @author huangyongtao
 * @date 2024/7/31 17:30
 */
@RestController
@RequestMapping("/api/compartment/reserve/app")
@Api(tags = "包间预定")
public class ApiCompartmentReserveController {


    /**
     * 包间预定服务接口.
     */
    @Autowired
    private ICompartmentReserveService compartmentReserveService;

    /**
     * 移动端-查询包间相关信息(可预定时间及套餐).
     */
    @ApiOperation(value = "移动端-查询包间相关信息(可预定时间及套餐)")
    @PostMapping(value = "/findCompartmentEx")
    @RequiredToken
    public CudResult<AppCompartmentExModel> getCompartmentEx(@RequestBody AppCompartmentReserveTimeListParam param) {
        return CudResult.success(compartmentReserveService.findCompartmentEx(param));
    }

    /**
     * 移动端获取包间预定信息.
     */
    @ApiOperation(value = "移动端-获取包间预定信息")
 
    @GetMapping(value = "/detail/{id}")
    public CudResult<AppCompartmentReserveDetailModel> detailApp(@PathVariable @NotNull(message = "预定标识不能为空") Long id) {
        return CudResult.success(compartmentReserveService.detailApp(id));
    }

    /**
     * 移动端-获取包间预定列表(分页).
     */
    @ApiOperation(value = "移动端-获取包间预定列表(分页)")
    @PostMapping(value = "/my")
    @RequiredToken
    public CudResult<IPage<AppCompartmentReserveModel>> pageApp(@RequestBody AppCompartmentReservePageParam param) {
        return CudResult.success(compartmentReserveService.pageApp(param));
    }

    /**
     * 获取包间预定列表.
     */
    @ApiOperation(value = "移动端-获取包间预定列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<AppCompartmentReserveGroupModel>> listApp(@RequestBody AppCompartmentReserveListParam param) {
        return CudResult.success(compartmentReserveService.listApp(param));
    }

    /**
     * 移动端-包间预定
     */
    @ApiOperation(value = "移动端-包间预定")
    @PostMapping(value = "/add")
    @RequiredToken
    public CudResult<Long> add(@RequestBody @Validated({InsertGroup.class}) AppCompartmentReserveParam param) {
        if (new Date().after(param.getReserveEndTime())){
            throw GenericException.fail("预定时间已过，请重新选择");
        }
        CompartmentReserveParam param1 = BeanUtils.convertTo(param,CompartmentReserveParam::new);
        param1.setSubscriberId(WebFrameworkUtils.getHeaderUserId());
        param1.setReserveSource(CompartmentReserveTypeEnum.APP.getCode());
        return CudResult.success(compartmentReserveService.add(param1));
    }

    /**
     * 移动端-取消包间预定.
     */
    @ApiOperation(value = "移动端-取消包间预定")
    @PostMapping(value = "/cancel")
    @RequiredToken
    public CudResult<Boolean> cancelApp(@RequestBody @Validated({Default.class, UpdateGroup.class}) CompartmentReserveParam param) {
        return CudResult.success(compartmentReserveService.cancelApp(param));
    }

    /***
     * @Description 查询包间可预订的时间
     * @author huangyongtao
     * @date 2024/8/2 10:08
     * @param param
     */
    @ApiOperation(value = "查询包间可预订的时间")
    @PostMapping(value = "/time/list")
    @RequiredToken
    public CudResult<List<AppCompartmentTimeExModel>> findTimeApp(@RequestBody AppCompartmentReserveTimeListParam param) {
        return CudResult.success(compartmentReserveService.findTimeApp(param));
    }

    @ApiOperation(value = "移动端-获取最近一条预约信息")
    @GetMapping(value = "/getNearest")
    @RequiredToken
    public CudResult<AppSimpleReserveModel> getNearest() {
        return CudResult.success(compartmentReserveService.getNearest());
    }
}
