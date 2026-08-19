package com.cgnpc.bbxpark.log.controller;

import com.cgnpc.bbxpark.settings.dto.param.SystemRunningParam;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.log.service.ISystemRunningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author EDZ
 */
@RestController
@RequestMapping(value = Constant.BASE_PATH+"/journal")
@Api(tags = "BBX-系统运行日志")
public class SystemRunningController {
    @Autowired
    private ISystemRunningService systemRunningService;

    @ApiOperation(value = "系统运行日志信息")
    @PostMapping(value = "/selectSystemRunningLog")
    public CudResult<Map<String,Object>> selectSystemRunningLog(@RequestBody SystemRunningParam dto) {
        return CudResult.success(systemRunningService.selectSystemRunningLog(dto));

    }
}
