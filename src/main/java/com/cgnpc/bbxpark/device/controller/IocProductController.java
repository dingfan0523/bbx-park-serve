package com.cgnpc.bbxpark.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.IocProductModel;
import com.cgnpc.bbxpark.device.dto.param.IocProductAddParam;
import com.cgnpc.bbxpark.device.dto.param.IocProductPageParam;
import com.cgnpc.bbxpark.device.service.IIocProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/24
 * @desc ioc产品服务接口
 */
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/iocProduct")
@Api(tags = "BBX-ioc产品服务接口")
public class IocProductController {

    @Autowired
    private IIocProductService iIocProductService;

    @ApiOperation(value = "ioc产品新增或修改")
    @PostMapping(value = "/addOrUpdate")
    public CudResult<Boolean> addOrUpdate(@RequestBody IocProductAddParam param) {
        boolean success = param.getId() != null ? iIocProductService.updateProduct(param) : iIocProductService.add(param);
        return CudResult.success(success);
    }

    @ApiOperation(value = "ioc产品分页查询")
    @PostMapping(value = "/queryPage")
    public CudResult<IPage<IocProductModel>> queryPage(@RequestBody IocProductPageParam param) {
        return CudResult.success(iIocProductService.queryPage(param));
    }

    @ApiOperation(value = "条件查询所有产品信息")
    @PostMapping(value = "/queryList")
    public CudResult<List<IocProductModel>> queryList(@RequestBody IocProductPageParam param) {
        return CudResult.success(iIocProductService.queryList(param));
    }

    @ApiOperation(value = "根据id删除产品")
    @GetMapping(value = "/deleteById")
    public CudResult<Boolean> deleteById(@RequestParam Long id) {
        return CudResult.success(iIocProductService.deleteById(id));
    }

    @ApiOperation(value = "根据id获取产品详情")
    @GetMapping(value = "/getDetail")
    public CudResult<IocProductModel> getDetail(@RequestParam Long id) {
        return CudResult.success(iIocProductService.getDetail(id));
    }
}
