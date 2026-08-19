
package com.cgnpc.bbxpark.supplier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierPersonModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPersonListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPersonPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPersonParam;
import com.cgnpc.bbxpark.supplier.service.ISupplierPersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 服务商人员服务控制类
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/supplier/person")
@Api(tags = "服务商人员")
public class SupplierPersonController {


    @Autowired
    private ISupplierPersonService supplierPersonService;


    @ApiOperation(value = "获取服务商人员信息")
    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<SupplierPersonModel> detail(@PathVariable Long id) {
            return CudResult.success(supplierPersonService.detail(id));
    }

    @ApiOperation(value = "获取服务商人员列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<SupplierPersonModel>> page(@RequestBody SupplierPersonPageParam param) {
            return CudResult.success(supplierPersonService.page(param));
    }

    @ApiOperation(value = "获取服务商人员列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<SupplierPersonModel>> list(@RequestBody SupplierPersonListParam param) {
            return CudResult.success(supplierPersonService.list(param));
    }

    @ApiOperation(value = "新增服务商人员")
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult add(@Validated({Default.class, InsertGroup.class}) @RequestBody SupplierPersonParam param) {
            return CudResult.success(supplierPersonService.add(param));
    }

    @ApiOperation(value = "删除服务商人员")
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult remove(@PathVariable Long id) {
            return CudResult.success(supplierPersonService.remove(id));
    }

    @ApiOperation(value = "编辑服务商人员")
    @PostMapping(value = "/edit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) SupplierPersonParam param) {
            return CudResult.success(supplierPersonService.edit(param));
    }
}