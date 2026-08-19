package com.cgnpc.bbxpark.device.api;

import com.cgnpc.bbxpark.common.enums.IocProductFileTypeEnum;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.IocProductFileModel;
import com.cgnpc.bbxpark.device.service.IIocProductFileService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/25
 * @desc ioc产品设备文件上传接口
 */
@RestController
@RequestMapping("/api/iocProductFile")
@Api(tags = "BBX-ioc产品设备文件上传接口")
public class ApiIocProductFileController {

    @Autowired
    private IIocProductFileService iIocProductFileService;

    @ApiOperation(value = "根据产品id查询上传文件")
    @GetMapping(value = "/queryByProductId")
    @RequiredToken
    public CudResult<List<IocProductFileModel>> queryByProductId(@RequestParam("id") Long id) {
        return CudResult.success(iIocProductFileService.queryByBusinessId(id,IocProductFileTypeEnum.PRODUCT.getValue()));
    }

    @ApiOperation(value = "根据设备id查询上传文件")
    @GetMapping(value = "/queryByDeviceId")
    @RequiredToken
    public CudResult<List<IocProductFileModel>> queryByDeviceId(@RequestParam("id") Long id) {
        return CudResult.success(iIocProductFileService.queryByBusinessId(id,IocProductFileTypeEnum.DEVICE.getValue()));
    }
}
