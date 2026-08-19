package com.cgnpc.bbxpark.problemReport.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.problemReport.model.ProblemReportModel;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportParam;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportQueryParam;
import com.cgnpc.bbxpark.problemReport.param.ProblemValidationParam;
import com.cgnpc.bbxpark.problemReport.service.IProblemReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc 报事报修控制器
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/problemReport")
@Api(tags = "智慧物业-PC端-报事报修控制器")
public class ProblemReportController {

    @Autowired
    private IProblemReportService problemReportService;

    @ApiOperation(value = "上报")
    @PostMapping(value = "/save")
    public CudResult<ProblemReportModel> save(@RequestBody ProblemReportParam param){
        return CudResult.success(problemReportService.save(param));
    }


    @ApiOperation(value = "问题确认")
    @PostMapping(value = "/validationProblem")
    public CudResult<Boolean> validationProblem(@RequestBody ProblemValidationParam param){
        return  CudResult.success(problemReportService.validationProblem(param));
    }

    @ApiOperation(value = "分页查询报事报修记录")
    @PostMapping(value = "/page")
    public CudResult<IPage<ProblemReportModel>> page(@RequestBody ProblemReportQueryParam param){
        return  CudResult.success(problemReportService.page(param));
    }

    @ApiOperation(value = "查询报事报修详情")
    @PostMapping(value = "/detail")
    public CudResult<ProblemReportModel> detail(@RequestBody ProblemReportQueryParam param){
        return  CudResult.success(problemReportService.detail(param));
    }

    /**
     * 新增文件.
     */
    @ApiOperation(value = "导入文件")
    @PostMapping(value = "/import")
    public CudResult<ImportReturnModel> importFile(@RequestParam(value = "file") MultipartFile file) {
        return CudResult.success(problemReportService.importFile(file));
    }

}
