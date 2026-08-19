
package com.cgnpc.bbxpark.settings.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.dto.model.SecurityDeviceRelationModel;
import com.cgnpc.bbxpark.settings.dto.model.SecurityManageModel;
import com.cgnpc.bbxpark.settings.dto.param.*;
import com.cgnpc.bbxpark.settings.service.ISecurityDeviceRelationService;
import com.cgnpc.bbxpark.settings.service.ISecurityManageService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 安全管理员服务控制类
 * @author huangyongtao
 * @date 2025/8/1 12:11
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/security")
@Api(value = "BBX-安全管理员")
public class SecurityManageController {

    /**
     * 安全管理员服务接口.
     */
    @Autowired
    private ISecurityManageService securityManageService;

    @Autowired
    private ISecurityDeviceRelationService securityDeviceRelationService;

    /**
     * 获取安全管理员列表(分页).
     */
    @ApiOperation(value = "获取安全管理员列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<SecurityManageModel>> page(@RequestBody SecurityManagePageParam param) {
        return CudResult.success(securityManageService.page(param));
    }

    /**
     * 获取安全管理员列表.
     */
    @ApiOperation(value = "获取安全管理员列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<SecurityManageModel>> list(@RequestBody SecurityManageListParam param) {
        return CudResult.success(securityManageService.list(param));
    }

    /**
     * 新增安全管理员.
     */
    @ApiOperation(value = "新增安全管理员")
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> add(@Validated({Default.class}) @RequestBody SecurityManageParam param) {
        return CudResult.success(securityManageService.add(param));
    }

    /**
     * 批量新增安全管理员.
     */
    @ApiOperation(value = "批量新增安全管理员")
    @PostMapping(value = "/add/batch",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> addBatch(@Validated @RequestBody SecurityManageParam param) {
        return CudResult.success(securityManageService.addBatch(param));
    }

    /**
     * 删除安全管理员.
     */
    @ApiOperation(value = "删除安全管理员")
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(securityManageService.remove(id));
    }

    /**
     * 批量关联安全管理员设备.
     */
    @ApiOperation(value = "批量关联安全管理员设备")
    @PostMapping(value = "/device/adds",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean>  addSecurityDevices(@RequestBody SecurityDeviceRelationParam param) {
        return CudResult.success(securityDeviceRelationService.add(param));
    }

    /**
     * 查询关联安全管理员设备.
     */
    @ApiOperation(value = "查询关联安全管理员设备")
    @PostMapping(value = "/device/list",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<SecurityDeviceRelationModel>>  findSecurityDevices(@RequestBody SecurityDeviceRelationListParam param) {
        return CudResult.success(securityDeviceRelationService.list(param));
    }
}
