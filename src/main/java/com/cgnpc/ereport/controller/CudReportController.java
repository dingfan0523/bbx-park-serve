package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.alibaba.fastjson.JSON;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.core.page.ReportPageInfo;
import com.cgnpc.report.chart.common.domain.Report;
import com.cgnpc.report.chart.common.dto.report.*;
import com.cgnpc.report.chart.common.exception.BusinessException;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 报表设计 前端控制器
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-10-07
 */
@CrossOrigin("*")
@Api(value = "/mapper/report", tags = "报表设计控制器")
@ApiResponses(@ApiResponse(code = 404, message = "report not found"))
@RestController
@RequestMapping("/report")
@Slf4j
public class CudReportController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;
    /**
     * @title: 创建报表
     * @author: p636016 XIAOJINHUI
     * @date: 2023/10/7 15:12
     * @description:
     * @param:
     * @return
     */
    @OperateLog(operateName = "创建报表",interfacePath = "/report/createReport")
    @ApiOperation(value = "创建报表")
    @PostMapping(value = "/createReport", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> createReport(@Valid @RequestBody ReportCreateInfoDto reportCreateInfoDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/report/createReport";
            HttpEntity httpEntity = new HttpEntity(reportCreateInfoDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("创建报表异常");
                throw new NullPointerException("创建报表异常");
            }
        }catch (Exception e){
            log.error("创建报表失败：{}",e.getMessage(),e);
            String msg = "创建报表失败！";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            }
            return new WfResult<>("500", msg, null);
        }

    }

    /**
     * @title: 更新报表
     * @author: p636016 XIAOJINHUI
     * @date: 2023/10/7 15:12
     * @description:
     * @param:
     * @return
     */
    @OperateLog(operateName = "更新报表",interfacePath = "/report/updateReport")
    @ApiOperation(value = "更新报表")
    @PostMapping(value = "/updateReport", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> updateReport(@Valid @RequestBody ReportUpdateInfoDto reportUpdateDto,
                                             HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/report/updateReport";
            HttpEntity httpEntity = new HttpEntity(reportUpdateDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("更新报表异常");
                throw new NullPointerException("更新报表异常");
            }
        }catch (Exception e){
            log.error("更新报表失败：{}",e.getMessage(),e);
            String msg = "更新报表失败！";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            }
            return new WfResult<>("500", msg, null);
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
    @OperateLog(operateName = "删除报表",interfacePath = "/report/deleteReport")
    @ApiOperation(value = "删除报表")
    @PostMapping(value = "/deleteReport", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> deleteReport(@Valid @RequestBody ReportDeleteDto reportDeleteDto,
                                              HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/report/deleteReport";
        HttpEntity httpEntity = new HttpEntity(reportDeleteDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("删除报表异常");
            throw new NullPointerException("删除报表异常");
        }
    }



    /**
     * 分页查询
     *
     * @param queryReportPageDto
     * @return
     */
    @OperateLog(operateName = "分页查询",interfacePath = "/report/pageInfo")
    @ApiOperation(value = "分页查询")
    @PostMapping("/pageInfo")
    public WfResult<ReportPageInfo<Report>> pageInfo(@Valid @RequestBody QueryReportPageDto queryReportPageDto,HttpServletRequest request) {
        log.info("报表分页查询：{}", JSON.toJSONString(queryReportPageDto));
        WfResult result = new WfResult();
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/report/pageInfo";
            HttpEntity httpEntity = new HttpEntity(queryReportPageDto, httpHeaders);
            ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(responseEntity.getBody()!=null){
                ReportPageInfo<Report> pageInfo = new ReportPageInfo<>();
                ApiResult apiResult = responseEntity.getBody();
                LinkedHashMap linkedHashMap = (LinkedHashMap) apiResult.getData();
                List<Report> reportList = (List<Report>) linkedHashMap.get("list");
                pageInfo.setList(reportList);
                pageInfo.setTotal((Integer) linkedHashMap.get("total"));
                pageInfo.setPageNum((Integer) linkedHashMap.get("pageNum"));
                pageInfo.setPageSize((Integer) linkedHashMap.get("pageSize"));
                result.data(pageInfo);
            }else{
                log.info("分页查询异常");
                throw new NullPointerException("分页查询异常");
            }
        }catch (Exception e){
            result.error("获取报表分页数据异常");
        }
        return result;
    }

    /**
     * 根据ID获取报表
     *
     * @param reportUniqueDto
     * @return
     */
    @OperateLog(operateName = "根据ID获取报表",interfacePath = "/report/findById")
    @ApiOperation(value = "根据ID获取报表")
    @PostMapping("/findById")
    public WfResult<Report> findById(@Valid @RequestBody ReportUniqueDto reportUniqueDto,HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/report/findById";
        HttpEntity httpEntity = new HttpEntity(reportUniqueDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("根据ID获取报表异常");
            throw new NullPointerException("根据ID获取报表异常");
        }
    }

    /**
     * 根据数据集和报表类型查询报表
     *
     * @param reportQueryDto
     * @return
     */
    @OperateLog(operateName = "根据数据集和报表类型查询报表",interfacePath = "/report/findByViewsAndType")
    @ApiOperation(value = "根据数据集和报表类型查询报表")
    @PostMapping("/findByViewsAndType")
    public WfResult<List<Report>> findByViewsAndType(@Valid @RequestBody ReportQueryDto reportQueryDto,HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/report/findByViewsAndType";
        HttpEntity httpEntity = new HttpEntity(reportQueryDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("根据数据集和报表类型查询报表异常");
            throw new NullPointerException("根据数据集和报表类型查询报表异常");
        }
    }

}

