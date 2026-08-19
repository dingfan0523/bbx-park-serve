
package com.cgnpc.bbxpark.space.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberAddParam;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberListParam;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberParam;
import com.cgnpc.bbxpark.space.service.ITenantMemberService;
import com.cgnpc.cud.core.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 租户成员
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/tenant/member")
@Api(tags = "BBX-租户成员API")
public class TenantMemberController extends BaseController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(TenantMemberController.class);

    @Autowired
    private ITenantMemberService tenantMemberService;

    @ApiOperation(value = "获取租户成员信息")
    @GetMapping(value = "/{id}")
    public CudResult<TenantMemberDomain> detail(@PathVariable Long id) {
        return CudResult.success(tenantMemberService.detail(id));

    }

//    @ApiOperation(value = "获取租户成员列表(分页)")
//    @PostMapping(value = "/page")
//    public WfGridResult<TenantMemberDomain> page(@RequestBody @Validated TenantMemberPageParam param) {
//        WfGridResult<TenantMemberDomain> result = new WfGridResult<>();
//        result.records(tenantMemberService.page(param));
//        return result;
//    }


    @ApiOperation(value = "获取租户成员列表")
    @PostMapping(value = "/listPage")
    public CudResult<IPage<TenantMemberDomain>> listPage(@RequestBody TenantMemberListParam param) {
        return CudResult.success(tenantMemberService.listPage(param));

    }

    @ApiOperation(value = "获取租户成员列表")
    @PostMapping(value = "/list")
    public CudResult<List<TenantMemberDomain>> list(@RequestBody TenantMemberListParam param) {
        return CudResult.success(tenantMemberService.list(param));
    }

    @ApiOperation(value = "新增租户成员")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(//@Validated
                                     @RequestBody TenantMemberParam param) {
        return CudResult.success(tenantMemberService.add(param));
    }

    @ApiOperation(value = "批量新增租户成员")
    @PostMapping(value = "/add/batch")
    public CudResult<Boolean> addBatch(@Validated @RequestBody TenantMemberAddParam param) {
        return CudResult.success(tenantMemberService.addBatch(param));
    }


    @ApiOperation(value = "分配租户管理员")
    @ResponseBody
    @PostMapping(value = "/admin/assign")
    public CudResult<Boolean> assignAdmin(@RequestBody //@Validated
                                                           TenantMemberParam param) {
        return CudResult.success(tenantMemberService.assignAdmin(param));
    }


    @ApiOperation(value = "删除租户成员")
    @DeleteMapping(value = "/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(tenantMemberService.remove(id));
    }

    @ApiOperation(value = "批量删除租户成员")
    @PostMapping(value = "/remove/batch")
    public CudResult<Boolean> removeBatch(@RequestBody List<Long> ids) {
        return CudResult.success(tenantMemberService.removeBatch(ids));
    }

    @ApiOperation(value = "编辑租户成员")
    @PutMapping(value = "/{id}")
    public CudResult<Boolean> edit(@PathVariable Long id,
                                       //@Validated
                                       @RequestBody TenantMemberParam param) {
        return CudResult.success(tenantMemberService.edit(id, param));
    }

    @ApiOperation(value = "启用租户成员")
    @PutMapping(value = "/{id}/enable")
    public CudResult<Boolean> enable(@PathVariable Long id) {
        return CudResult.success(tenantMemberService.enableBatch(Collections.singletonList(id)));
    }

    @ApiOperation(value = "批量启用租户成员")
    @PutMapping(value = "/enable/batch")
    public CudResult<Boolean> enableBatch(@RequestBody List<Long> ids) {
        return CudResult.success(tenantMemberService.enableBatch(ids));
    }

    @ApiOperation(value = "禁用租户成员")
    @PutMapping(value = "/{id}/disable")
    public CudResult<Boolean> disable(@PathVariable Long id) {
        return CudResult.success(tenantMemberService.disableBatch(Collections.singletonList(id)));
    }

    @ApiOperation(value = "批量禁用租户成员")
    @PutMapping(value = "/disable/batch")
    public CudResult<Boolean> disableBatch(@RequestBody List<Long> ids) {
        return CudResult.success(tenantMemberService.disableBatch(ids));
    }

    /**********************************
     * 用途说明:
     * 参数说明: @param binder
     * 返回值说明: @return void
     * 异常说明: @throws
     * 作者姓名: @author
     * 创建时间: @date 2021/11/26 14:17
     ***********************************/
    @Override
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{""});
    }
}
