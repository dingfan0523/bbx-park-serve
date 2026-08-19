package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.domain.ReportWidget;
import com.cgnpc.report.chart.common.dto.report.QueryReportPageDto;
import com.cgnpc.report.chart.common.dto.report.ReportQueryDto;
import com.cgnpc.report.chart.common.dto.report.ReportWidgetDeleteDto;
import com.cgnpc.report.chart.common.dto.report.ReportWidgetTreeDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 报表组件 前端控制器
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-10-25
 */
@CrossOrigin("*")
@Api(value = "/report-widget", tags = "报表组件控制器")
@ApiResponses(@ApiResponse(code = 404, message = "report not found"))
@RestController
@RequestMapping("/report-widget")
@Slf4j
public class CudReportWidgetController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    /**
     * 分页查询
     *
     * @param queryReportPageDto
     * @return
     */
    @OperateLog(operateName = "分页查询",interfacePath = "/report-widget/pageInfo")
    @ApiOperation(value = "分页查询")
    @PostMapping("/pageInfo")
    public WfResult<PageInfo<ReportWidget>> pageInfo(@Valid @RequestBody QueryReportPageDto queryReportPageDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/report-widget/pageInfo";
            HttpEntity httpEntity = new HttpEntity(queryReportPageDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("查询已发布的图表组件树异常");
                throw new NullPointerException("查询已发布的图表组件树异常");
            }
        }catch (Exception e){
            log.error("查询已发布的图表组件树失败：{}",e.getMessage(),e);
            return new WfResult<>("查询已发布的图表组件树失败！",null);
        }
    }

    /**
     * @title: 删除报表
     * @author: p636016 XIAOJINHUI
     * @date: 2023/10/7 15:12
     * @description:
     * @param:
     * @return
     */
    @OperateLog(operateName = "删除报表组件",interfacePath = "/report-widget/deleteWidgets")
    @ApiOperation(value = "删除报表组件")
    @PostMapping(value = "/deleteWidgets", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> deleteWidgets(@Valid @RequestBody ReportWidgetDeleteDto reportWidgetDeleteDto,
                                               HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/report-widget/deleteWidgets";
            HttpEntity httpEntity = new HttpEntity(reportWidgetDeleteDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("查询已发布的图表组件树异常");
                throw new NullPointerException("查询已发布的图表组件树异常");
            }
        }catch (Exception e){
            log.error("删除报表组件失败：{}",e.getMessage(),e);
            return new WfResult<>("500","删除报表组件失败！",null);
        }
    }


    /**
     * 查询已发布的图表组件树
     *
     * @param reportQueryDto
     * @return
     */
    @OperateLog(operateName = "查询已发布的图表组件树",interfacePath = "/report/findPublishCharts")
    @ApiOperation(value = "查询已发布的图表组件树")
    @PostMapping("/findPublishCharts")
    public WfResult<List<ReportWidgetTreeDto>> findPublishCharts(@Valid @RequestBody ReportQueryDto reportQueryDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/report-widget/findPublishCharts";
            HttpEntity httpEntity = new HttpEntity(reportQueryDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("查询已发布的图表组件树异常");
                throw new NullPointerException("查询已发布的图表组件树异常");
            }
        }catch (Exception e){
            log.error("查询已发布的图表组件树失败：{}",e.getMessage(),e);
            return new WfResult<>("查询已发布的图表组件树失败！",null);
        }
    }

}

