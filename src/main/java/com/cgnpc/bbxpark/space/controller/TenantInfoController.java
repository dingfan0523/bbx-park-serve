
package com.cgnpc.bbxpark.space.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.domain.TenantDomain;
import com.cgnpc.bbxpark.space.dto.model.TenantInfoModel;
import com.cgnpc.bbxpark.space.dto.model.TenantInfoModelExt;
import com.cgnpc.bbxpark.space.dto.param.TenantInfoListParam;
import com.cgnpc.bbxpark.space.dto.param.TenantInfoParam;
import com.cgnpc.bbxpark.space.dto.param.TenantPageParam;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import com.cgnpc.cud.annotation.OperatorType;
import com.cgnpc.cud.annotation.UBA;
import com.cgnpc.cud.core.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/******************************
 * 用途说明:租户信息
 * 作者姓名: P309150
 * 创建时间: 2025/11/17 13:43
 ******************************/
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/tenant")
@Api(tags = "BBX-租户信息API")
public class TenantInfoController extends BaseController {

    @Autowired
    private ITenantInfoService tenantInfoService;

    @ApiOperation(value = "获取租户信息")
    @GetMapping(value = "/{id}")
    public CudResult<TenantDomain> detail(@PathVariable Long id) {
        return CudResult.success(tenantInfoService.detail(id));
    }
    @ApiOperation(value = "获取租户信息列表(分页)")
    @PostMapping(value = "/pageFullInfo")
    @UBA(module = "园区管理", action = "查询园区列表", channel = OperatorType.Button)
    public CudResult<IPage<TenantInfoModelExt>> pageFullInfo(@Validated @RequestBody TenantPageParam param) {
        return CudResult.success(tenantInfoService.pageFullInfo(param));

    }
    @ApiOperation(value = "获取租户信息列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<TenantInfoModel>> page(@Validated @RequestBody TenantPageParam param) {
        return CudResult.success(tenantInfoService.page(param));
    }

    @ApiOperation(value = "获取租户信息列表")
    @PostMapping(value = "/list")
    public CudResult<List<TenantInfoModel>> list(@RequestBody TenantInfoListParam param) {
        return CudResult.success(tenantInfoService.list(param));
    }

    @ApiOperation(value = "新增租户信息")
    @PostMapping(value = "/add")
    public CudResult<TenantInfoModel> add(@Validated//
                                                           @RequestBody TenantInfoParam param) {
        return CudResult.success(tenantInfoService.add(param));
    }

    @ApiOperation(value = "删除租户信息")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(tenantInfoService.remove(id));
    }

    @ApiOperation(value = "编辑租户信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<Boolean> edit(@PathVariable Long id, @Validated//
                                           @RequestBody TenantInfoParam param) {
        return CudResult.success(tenantInfoService.edit(id,param));
    }

    @ApiOperation(value = "启用租户信息")
    @PostMapping(value = "/{id}/enable")
    public CudResult<Boolean> enable(@PathVariable Long id) {
        return CudResult.success(tenantInfoService.enable(id));
    }

    @ApiOperation(value = "禁用租户信息")
    @PostMapping(value = "/{id}/disable")
    public CudResult<Boolean> disable(@PathVariable Long id) {
        return CudResult.success(tenantInfoService.disable(id));
    }

//    @ApiOperation(value = "租户分配组织部门")
//    @PostMapping(value = "/org-dept/assign")
//    public CudResult<Boolean> assignOrgDept(@RequestBody TenantAssignOrgDeptParam param) {
//        CudResult<Boolean> result = new CudResult<>();
//        result.data(tenantInfoService.assignOrgDept(param));
//        return result;
//    }
//
//
//    @ApiOperation(value = "租户分配权限")
//    @PostMapping(value = "/perm/assign")
//    public ResponseEntity<Result> assignPerm(@RequestBody TenantAssignPermParam param) {
//        return ResponseEntity.ok( CudResult.success(tenantInfoService.assignPerm(param)));
//    }
//
//    @ApiOperation(value = "租户分配菜单")
//    @PostMapping(value = "/menu/assign")
//    public ResponseEntity<Result> assignMenu(@RequestBody TenantAssignMenuParam param) {
//        return ResponseEntity.ok( CudResult.success(tenantInfoService.assignMenu(param)));
//    }

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
