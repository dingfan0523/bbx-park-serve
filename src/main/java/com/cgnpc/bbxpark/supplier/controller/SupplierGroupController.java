
package com.cgnpc.bbxpark.supplier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierGroupModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupParam;
import com.cgnpc.bbxpark.supplier.service.ISupplierGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 服务商分组服务控制类
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/supplier/group")
@Api(tags = "服务商分组")
public class SupplierGroupController {

    /**
     * 服务商分组服务接口.
     */
    @Autowired
    private ISupplierGroupService supplierGroupService;

    /**
     * 获取服务商分组信息.
     */
    @ApiOperation(value = "获取服务商分组信息")

    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<SupplierGroupModel> detail(@PathVariable Long id) {
        return CudResult.success(supplierGroupService.detail(id));
    }

    /**
     * 获取服务商分组列表(分页).
     */
    @ApiOperation(value = "获取服务商分组列表(分页)")
    
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<SupplierGroupModel>> page(@RequestBody SupplierGroupPageParam param) {
        return CudResult.success(supplierGroupService.page(param));
    }

    /**
     * 获取服务商分组列表.
     */
    @ApiOperation(value = "获取服务商分组列表")
    
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<SupplierGroupModel>> list(@RequestBody SupplierGroupListParam param) {
        return CudResult.success(supplierGroupService.list(param));
    }

    /**
     * 新增服务商分组.
     */
    @ApiOperation(value = "新增服务商分组")
    
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult add(@Validated({Default.class, InsertGroup.class}) @RequestBody SupplierGroupParam param) {
        return CudResult.success(supplierGroupService.add(param));
    }

    /**
     * 删除服务商分组.
     */
    @ApiOperation(value = "删除服务商分组")
    
    @GetMapping(value = "/remove/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult remove(@PathVariable Long id) {
        return CudResult.success(supplierGroupService.remove(id));
    }

    /**
     * 编辑服务商分组.
     */
    @ApiOperation(value = "编辑服务商分组")
    
    @PostMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) SupplierGroupParam param) {
        return CudResult.success(supplierGroupService.edit(param));
    }
}