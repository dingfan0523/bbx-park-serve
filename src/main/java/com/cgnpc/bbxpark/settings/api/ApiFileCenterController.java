package com.cgnpc.bbxpark.settings.api;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.config.minio.configure.service.FileCenterService;
import com.cgnpc.bbxpark.config.minio.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FileLogParam;
import com.cgnpc.bbxpark.settings.dto.param.OssParam;
import com.cgnpc.bbxpark.settings.service.IFileLogService;
import com.cgnpc.bbxpark.settings.service.IOssService;
import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;


@Validated
@Api(tags = "BBX-文件中心")
@RestController
@RequestMapping("/api/file")
@Slf4j
public class ApiFileCenterController extends BaseController {

    @Autowired
    private FileCenterService fileCenterService;
    @Autowired
    private IOssService ossService;
    @Resource
    private IFileLogService logService;

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "upload")
    @RequiredToken
    public CudResult<FileModel> upload(@RequestPart("file") MultipartFile file) {
        log.info("文件上传请求到达，文件名: {}", file.getOriginalFilename());
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        FileModel fileModel = fileCenterService.upload(file, null,tenantId);
        // 保存附件文件
        CudResult<FileModel> result = new CudResult<>();
        if(!this.initOss(fileModel)){
             return CudResult.success(fileModel, "sys_oss表新增失败");
        }
        return CudResult.success(fileModel);
    }

    @ApiOperation(value = "PC端-新增异常文件日志")
    @PostMapping(value = "/log/add")
    @RequiredToken
    public CudResult<Boolean> add(@RequestBody FileLogParam param) {
        return CudResult.success(logService.add(param));
    }

    /**
     * 附件保存到sys_oss表
     * @param fileModel
     * @return
     */
    private boolean initOss(FileModel fileModel) {
        Date date = new Date();
        OssParam ossParam = OssParam.builder()
                .ossId(IdWorker.getId())
                .originalName(fileModel.getFileName())
                .url(fileModel.getUrl())
                .status(Status.enabled.getKey())
                .build();
        int fileNameIndex = fileModel.getUrl().lastIndexOf("/");
        if (fileNameIndex > 0) {
            ossParam.setFileName(fileModel.getUrl().substring(fileNameIndex + 1));
        }
        int fileSuffixIndex = fileModel.getFileName().lastIndexOf(".");
        if (fileSuffixIndex > 0) {
            ossParam.setFileSuffix(fileModel.getFileName().substring(fileSuffixIndex));
        }
        if(!ossService.add(ossParam)){
            return false;
        }
        fileModel.setOssId(ossParam.getOssId().toString());
        return true;
    }
}
