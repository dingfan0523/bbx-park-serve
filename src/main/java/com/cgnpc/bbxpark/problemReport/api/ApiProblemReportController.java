package com.cgnpc.bbxpark.problemReport.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.problemReport.model.ProblemReportModel;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportParam;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportQueryParam;
import com.cgnpc.bbxpark.problemReport.param.ProblemValidationParam;
import com.cgnpc.bbxpark.problemReport.service.IProblemReportService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @create zhaoshuo
 * @time 2025/3/28
 * @desc app报事报修控制器
 */
@RestController
@RequestMapping("/api/AppProblemReport")
@Api(tags = "智慧物业-app端-报事报修控制器")
public class ApiProblemReportController {
    @Autowired
    private IProblemReportService problemReportService;

    @ApiOperation(value = "上报")
    @PostMapping(value = "/save")
    @RequiredToken
    public CudResult<ProblemReportModel> save(@RequestBody ProblemReportParam param){
        return CudResult.success(problemReportService.save(param));
    }

    @ApiOperation(value = "上报历史")
    @PostMapping(value = "/reportHistory")
    @RequiredToken
    public CudResult<IPage<ProblemReportModel>> reportHistory(@RequestBody ProblemReportQueryParam param){
        return  CudResult.success(problemReportService.reportHistory(param));
    }

    @ApiOperation(value = "报事报修评价")
    @PostMapping(value = "/review")
    @RequiredToken
    public CudResult<Boolean> review(@RequestBody ProblemValidationParam param){
        return  CudResult.success(problemReportService.review(param));
    }

    @ApiOperation(value = "app查询报事报修详情")
    @PostMapping(value = "/appDetail")
    @RequiredToken
    public CudResult<ProblemReportModel> appDetail(@RequestBody ProblemReportQueryParam param){
        return  CudResult.success(problemReportService.appDetail(param));
    }
}
