
package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleListModel;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleModel;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleUserModel;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleListParam;
import com.cgnpc.bbxpark.property.dto.param.PropertySchedulePageParam;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleParam;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 物业排班服务控制类
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/property/schedule")
@Api(tags = "物业管理-PC端-物业分组排班")
@Slf4j
public class PropertyScheduleController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(PropertyScheduleController.class);

    /**
     * 物业排班服务接口.
     */
    @Autowired
    private IPropertyScheduleService propertyScheduleService;

    /**
     * 获取物业排班信息.
     */
    @ApiOperation(value = "获取物业排班信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<PropertyScheduleModel> detail(@PathVariable Long id) {
        return CudResult.success(propertyScheduleService.detail(id));
    }

    /**
     * 获取物业排班列表(分页).
     */
    @ApiOperation(value = "获取物业排班列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<PropertyScheduleListModel>> page(@RequestBody PropertySchedulePageParam param) {
        return CudResult.success(propertyScheduleService.page(param));
    }

    /**
     * 获取物业排班列表
     */
    @ApiOperation(value = "获取物业排班列表")
    @PostMapping(value = "/list")
    public CudResult<List<PropertyScheduleListModel>> list(@RequestBody PropertyScheduleListParam param) {
        return CudResult.success(propertyScheduleService.list(param));
    }

    /**
     * 新增物业排班.
     */
    @ApiOperation(value = "新增物业排班")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated({Default.class}) @RequestBody PropertyScheduleParam param) {
        return CudResult.success(propertyScheduleService.add(param));
    }

    /**
     * 编辑物业排班.
     */
    @ApiOperation(value = "编辑物业排班")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated({Default.class}) PropertyScheduleParam param) {
        return CudResult.success(propertyScheduleService.edit(param));
    }

    /**
     * 删除物业排班.
     */
    @ApiOperation(value = "删除物业排班")
    @GetMapping(value = "remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(propertyScheduleService.remove(id));
    }

    /**
     * 根据id获取物业排班分组下的人员列表.
     */
    @ApiOperation(value = "根据id获取物业排班分组下的人员列表")
    @GetMapping(value = "/findUserList")
    public CudResult<List<PropertyScheduleUserModel>> findUserList(@RequestParam Long id) {
        return CudResult.success(propertyScheduleService.findUserList(id));
    }
}
