package com.cgnpc.bbxpark.device.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.enums.IocProductFileTypeEnum;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.domain.IocProductFile;
import com.cgnpc.bbxpark.device.dto.model.IocProductFileModel;
import com.cgnpc.bbxpark.device.dto.param.IocProductFileUpdate;
import com.cgnpc.bbxpark.device.service.IIocProductFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/25
 * @desc ioc产品设备文件上传接口
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/iocProductFile")
@Api(tags = "BBX-ioc产品设备文件上传接口")
public class IocProductFileController {

    @Autowired
    private IIocProductFileService iIocProductFileService;

    @ApiOperation(value = "上传产品文件")
    @PostMapping(value = "/uploadProductFile")
    public CudResult<Boolean> uploadProductFile(@RequestBody IocProductFile iocProductFile) {
        iocProductFile.setType(IocProductFileTypeEnum.PRODUCT.getValue());
        return CudResult.success(iIocProductFileService.uploadProductFile(iocProductFile));
    }

    @ApiOperation(value = "上传设备文件")
    @PostMapping(value = "/uploadDeviceFile")
    public CudResult<Boolean> uploadDeviceFile(@RequestBody IocProductFile iocProductFile) {
        iocProductFile.setType(IocProductFileTypeEnum.DEVICE.getValue());
        return CudResult.success(iIocProductFileService.uploadProductFile(iocProductFile));
    }

    @ApiOperation(value = "修改设备文件")
    @PostMapping(value = "/updateFileList")
    public CudResult<Boolean> updateFileList(@RequestBody List<IocProductFileUpdate> iocProductFiles) {
        return CudResult.success(iIocProductFileService.updateFileList(iocProductFiles));
    }

    @ApiOperation(value = "删除设备文件")
    @GetMapping(value = "/deleteFile")
    public CudResult<Boolean> deleteFile(@RequestParam("id") Long id) {
        return CudResult.success(iIocProductFileService.deleteFile(id));
    }

    @ApiOperation(value = "根据产品id查询上传文件")
    @GetMapping(value = "/queryByProductId")
    public CudResult<List<IocProductFileModel>> queryByProductId(@RequestParam("id") Long id) {
        return CudResult.success(iIocProductFileService.queryByBusinessId(id,IocProductFileTypeEnum.PRODUCT.getValue()));
    }

    @ApiOperation(value = "根据设备id查询上传文件")
    @GetMapping(value = "/queryByDeviceId")
    public CudResult<List<IocProductFileModel>> queryByDeviceId(@RequestParam("id") Long id) {
        return CudResult.success(iIocProductFileService.queryByBusinessId(id,IocProductFileTypeEnum.DEVICE.getValue()));
    }
}
