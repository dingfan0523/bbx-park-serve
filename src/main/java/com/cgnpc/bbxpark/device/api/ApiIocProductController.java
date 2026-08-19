package com.cgnpc.bbxpark.device.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.IocProductModel;
import com.cgnpc.bbxpark.device.dto.param.IocProductPageParam;
import com.cgnpc.bbxpark.device.service.IIocProductService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/24
 * @desc ioc产品服务接口
 */
@Slf4j
@RestController
@RequestMapping("/api/iocProduct")
@Api(tags = "BBX-ioc产品服务接口")
public class ApiIocProductController {

    @Autowired
    private IIocProductService iIocProductService;

    @ApiOperation(value = "条件查询所有产品信息")
    @PostMapping(value = "/queryList")
    @RequiredToken
    public CudResult<List<IocProductModel>> queryList(@RequestBody IocProductPageParam param) {
        return CudResult.success(iIocProductService.queryList(param));
    }
}
