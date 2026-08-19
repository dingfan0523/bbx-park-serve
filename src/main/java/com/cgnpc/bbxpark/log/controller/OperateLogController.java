package com.cgnpc.bbxpark.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.log.service.IOperateLogService;
import com.cgnpc.bbxpark.log.vo.OperateLogModel;
import com.cgnpc.bbxpark.log.vo.OperateLogPageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constant.BASE_PATH + "/log/operate")
@Api(tags = "操作日志")
public class OperateLogController {

    @Autowired
    private IOperateLogService operateLogService;

    @ApiOperation(value = "获取操作日志信息")
    @GetMapping(value = "/{id}")
    public CudResult<OperateLogModel> detail(@PathVariable Long id) {
        return  CudResult.success(operateLogService.detail(id));
    }
    @ApiOperation(value = "获取操作日志列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<OperateLogModel>> page(@RequestBody OperateLogPageParam param) {
        return CudResult.success(operateLogService.page(param));
    }
}
