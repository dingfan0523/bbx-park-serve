
package com.cgnpc.bbxpark.supplier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierGroupPersonModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPersonListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPersonPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPersonParam;
import com.cgnpc.bbxpark.supplier.service.ISupplierGroupPersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 服务商分组人员服务控制类
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/supplier/group/person")
@Api(tags = "服务商分组人员")
public class SupplierGroupPersonController {

    /**
     * 服务商分组人员服务接口.
     */
    @Autowired
    private ISupplierGroupPersonService supplierGroupPersonService;

    /**
     * 获取服务商分组人员信息.
     */
    @ApiOperation(value = "获取服务商分组人员信息")
    
    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<SupplierGroupPersonModel> detail(@PathVariable Long id) {
            return CudResult.success(supplierGroupPersonService.detail(id));
    }

    /**
     * 获取服务商分组人员列表(分页).
     */
    @ApiOperation(value = "获取服务商分组人员列表(分页)")
    
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<SupplierGroupPersonModel>> page(@RequestBody SupplierGroupPersonPageParam param) {
            return CudResult.success(supplierGroupPersonService.page(param));
    }

    /**
     * 获取服务商分组人员列表.
     */
    @ApiOperation(value = "获取服务商分组人员列表")
    
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<SupplierGroupPersonModel>> list(@RequestBody SupplierGroupPersonListParam param) {
            return CudResult.success(supplierGroupPersonService.list(param));
    }

    /**
     * 新增服务商分组人员.
     */
    @ApiOperation(value = "新增服务商分组人员")
    
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult add(@Validated({Default.class, InsertGroup.class}) @RequestBody SupplierGroupPersonParam param) {
            return CudResult.success(supplierGroupPersonService.add(param));
    }

    /**
     * 删除服务商分组人员.
     */
    @ApiOperation(value = "删除服务商分组人员")
    
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult remove(@PathVariable Long id) {
            return CudResult.success(supplierGroupPersonService.remove(id));
    }

    /**
     * 编辑服务商分组人员.
     */
    @ApiOperation(value = "编辑服务商分组人员")
    
    @PostMapping(value = "/edit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) SupplierGroupPersonParam param) {
            return CudResult.success(supplierGroupPersonService.edit(param));
    }
}