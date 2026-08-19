
package com.cgnpc.bbxpark.supplier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierParam;
import com.cgnpc.bbxpark.supplier.service.ISupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 服务商服务控制类
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/supplier")
@Api(tags = "服务商")
public class SupplierController {

    /**
     * 服务商服务接口.
     */
    @Autowired
    private ISupplierService supplierService;

    /**
     * 获取服务商信息.
     */
    @ApiOperation(value = "获取服务商信息")
    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<SupplierModel> detail(@PathVariable Long id) {
            return CudResult.success(supplierService.detail(id));
    }

    /**
     * 获取服务商列表(分页).
     */
    @ApiOperation(value = "获取服务商列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<SupplierModel>> page(@RequestBody SupplierPageParam param) {
            return CudResult.success(supplierService.page(param));
    }

    /**
     * 获取服务商列表.
     */
    @ApiOperation(value = "获取服务商列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<SupplierModel>> list(@RequestBody SupplierListParam param) {
            return CudResult.success(supplierService.list(param));
    }

    /**
     * 获取服务商列表(包含服务商分组列表).
     */
    @ApiOperation(value = "获取服务商列表(包含服务商分组列表)")
    @PostMapping(value = "/findList", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<SupplierModel>> findList(@RequestBody SupplierListParam param) {
        return CudResult.success(supplierService.findList(param));
    }

    /**
     * 新增服务商.
     */
    @ApiOperation(value = "新增服务商")
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult add(@Validated({Default.class, InsertGroup.class}) @RequestBody SupplierParam param) {
            return CudResult.success(supplierService.add(param));
    }

    /**
     * 删除服务商.
     */
    @ApiOperation(value = "删除服务商")
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult remove(@PathVariable Long id) {
            return CudResult.success(supplierService.remove(id));
    }

    /**
     * 编辑服务商.
     */
    @ApiOperation(value = "编辑服务商")
    @PostMapping(value = "/edit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) SupplierParam param) {
            return CudResult.success(supplierService.edit(param));
    }
}