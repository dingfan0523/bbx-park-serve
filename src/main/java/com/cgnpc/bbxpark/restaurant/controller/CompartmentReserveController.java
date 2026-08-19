
package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.enums.CompartmentReserveTypeEnum;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentReserveGroupModel;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentReserveModel;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentReserveListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentReservePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentReserveParam;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentReserveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;

/***
 * @Description 包间预定服务控制类
 * @author huangyongtao
 * @date 2024/7/31 17:30
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/compartment/reserve")
@Api(tags = "包间预定")
public class CompartmentReserveController {


    /**
     * 包间预定服务接口.
     */
    @Autowired
    private ICompartmentReserveService compartmentReserveService;

    /**
     * 获取包间预定信息.
     */
    @ApiOperation(value = "获取包间预定信息")
 
    @PostMapping(value = "/detail")
    public CudResult<CompartmentReserveModel> detail(@RequestBody CompartmentReserveParam param) {
            return CudResult.success(compartmentReserveService.detail(param.getId()));
    }

    /**
     * 获取包间预定列表(分页).
     */
    @ApiOperation(value = "获取包间预定列表(分页)")
 
    @PostMapping(value = "/page")
    public CudResult<IPage<CompartmentReserveModel>> page(@RequestBody CompartmentReservePageParam param) {
        return CudResult.success(compartmentReserveService.page(param));
    }

    /**
     * 获取包间预定列表.
     */
    @ApiOperation(value = "获取包间预定列表")
 
    @PostMapping(value = "/list")
    public CudResult<List<CompartmentReserveGroupModel>> list(@RequestBody CompartmentReserveListParam param) {
        return CudResult.success(compartmentReserveService.list(param));
    }

    /**
     * 新增包间预定.
     */
    @ApiOperation(value = "新增包间预定")
    @PostMapping(value = "/add")
    public CudResult<Long> add(@Validated({Default.class, InsertGroup.class}) @RequestBody CompartmentReserveParam param) {
        if (new Date().after(param.getReserveEndTime())){
            throw GenericException.fail("预定时间已过，请重新选择");
        }
        param.setReserveSource(CompartmentReserveTypeEnum.WEB.getCode());
        return CudResult.success(compartmentReserveService.add(param));
    }

    /**
     * 编辑包间预定.
     */
    @ApiOperation(value = "编辑包间预定")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) CompartmentReserveParam param) {
        return CudResult.success(compartmentReserveService.edit(param));
    }

    /**
     * 取消包间预定.
     */
    @ApiOperation(value = "取消包间预定")
    @PostMapping(value = "/cancel")
    public CudResult<Boolean> cancel(@RequestBody @Validated({Default.class, UpdateGroup.class}) CompartmentReserveParam param) {
        return CudResult.success(compartmentReserveService.cancel(param));
    }

    /**
     * 到店
     */
    @ApiOperation(value = "编辑包间预定")
    
    @PostMapping(value = "/arrive")
    public CudResult<Boolean> arrive(@RequestBody @Validated({Default.class, UpdateGroup.class}) CompartmentReserveParam param) {
        return CudResult.success(compartmentReserveService.arrive(param));
    }

    /***
     *  查询包间可预订的时间
     */
    @ApiOperation(value = "查询包间可预订的时间")
    
    @PostMapping(value = "/findTime")
    public CudResult<List<CompartmentTimeModel>> findTime(@RequestBody CompartmentReserveParam param) {
        return CudResult.success(compartmentReserveService.findTime(param));
    }

    /**
     * 更新过期状态
     */
    @ApiOperation(value = "更新过期状态")
    @PostMapping(value = "/updateStatus")
    public CudResult<Boolean> updateStatus() {
        return CudResult.success(compartmentReserveService.updateStatus());
    }

    /***
     * @Description 查询包间预约信息
     * @author huangyongtao
     * @date 2024/8/7 17:34
     * @param param
     */
    @ApiOperation(value = "查询包间预约信息")
    @PostMapping(value = "/findCompartmentReserve")
    public CudResult<List<CompartmentReserveModel>> findCompartmentReserve(@RequestBody CompartmentReserveParam param) {
        return CudResult.success(compartmentReserveService.findCompartmentReserve(param));
    }

    /***
     * @Description 查询包间营业时间范围
     * @author huangyongtao
     * @date 2024/8/8 15:34
     * @param param
     */
    @ApiOperation(value = "查询包间营业时间范围")
 
    @PostMapping(value = "/findTimeRange")
    public CudResult<CompartmentTimeModel> findTimeRange(@RequestBody CompartmentReserveParam param) {
        return CudResult.success(compartmentReserveService.findTimeRange(param));
    }
}
