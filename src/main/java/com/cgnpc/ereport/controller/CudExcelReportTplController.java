package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.domain.excelreport.ExcelReportTpl;
import com.cgnpc.report.chart.common.domain.excelreport.ExcelReportTplDataset;
import com.cgnpc.report.chart.common.dto.excelreport.reporttpl.*;
import com.cgnpc.report.chart.common.dto.excelreport.reporttpldataset.ReportDatasetDto;
import com.cgnpc.report.chart.common.dto.excelreport.reporttpldataset.ReportTplDatasetDto;
import com.cgnpc.report.chart.common.exception.BusinessException;
import com.cgnpc.report.chart.common.exception.ServerException;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author P637785
 * @description Excel报表控制器
 * @date 2024/04/24
 */
@CrossOrigin("*")
@Api(value = "/excelreport", tags = "excel报表")
@ApiResponses(@ApiResponse(code = 404, message = "excelreport not found"))
@RestController
@RequestMapping("/excelreport")
@Slf4j
public class CudExcelReportTplController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    @OperateLog(operateName = "创建Excel报表",interfacePath = "/excelreport/createReport")
    @ApiOperation(value = "创建Excel报表")
    @PostMapping(value = "/createReport", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<SaveLuckySheetTplDto> createReport(@Valid @RequestBody MesLuckysheetsTplDto mesLuckysheetsTplDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/createReport";
            HttpEntity httpEntity = new HttpEntity(mesLuckysheetsTplDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("创建Excel报表异常");
                throw new NullPointerException("创建Excel报表异常");
            }
        } catch (Exception e) {
            log.error("创建Excel报表失败：{}", e.getMessage(), e);
            String msg = "创建Excel报表失败！";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            }
            return new WfResult<>("500", msg, null);
        }
    }

    /**
     * 删除Excel报表
     *
     * @param excelReportDeleteDto
     * @return
     */
    @OperateLog(operateName = "删除Excel报表",interfacePath = "/excelreport/deleteReport")
    @ApiOperation(value = "删除Excel报表")
    @PostMapping(value = "/deleteReport", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> deleteReport(@Valid @RequestBody ExcelReportDeleteDto excelReportDeleteDto, HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/excelreport/deleteReport";
        HttpEntity httpEntity = new HttpEntity(excelReportDeleteDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("删除Excel报表异常");
            throw new NullPointerException("删除Excel报表异常");
        }
    }

    @OperateLog(operateName = "获取数据集",interfacePath = "/excelreport/getTplDatasets")
    @ApiOperation(value = "获取数据集")
    @PostMapping(value = "/getTplDatasets", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<List<ReportDatasetDto>> getTplDatasets(@RequestBody ExcelReportTplDataset dataset, HttpServletRequest request){
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/getTplDatasets";
            HttpEntity httpEntity = new HttpEntity(dataset, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取数据集异常");
                throw new NullPointerException("获取数据集异常");
            }
        }catch (Exception e){
            log.error("获取数据集失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取数据集失败！",null);
        }
    }

    @OperateLog(operateName = "获取数据集字段",interfacePath = "/excelreport/getDatasetColumns")
    @ApiOperation(value = "获取数据集字段")
    @PostMapping(value = "/getDatasetColumns", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<List<Map<String, Object>>> getDatasetColumns(@RequestBody ExcelReportTplDataset dataset,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/getDatasetColumns";
            HttpEntity httpEntity = new HttpEntity(dataset, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取数据集字段异常");
                throw new NullPointerException("获取数据集字段异常");
            }
        }catch (Exception e){
            log.error("获取数据集字段失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取数据集字段失败！",null);
        }
    }

    @OperateLog(operateName = "获取模板设置",interfacePath = "/excelreport/getLuckySheetTplSettings")
    @ApiOperation(value = "获取模板设置")
    @PostMapping(value = "/getLuckySheetTplSettings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<ResSheetsSettingsDto> getLuckySheetTplSettings(@RequestBody ExcelReportTpl reportTpl,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/getLuckySheetTplSettings";
            HttpEntity httpEntity = new HttpEntity(reportTpl, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class, reportTpl);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取数据集字段异常");
                throw new NullPointerException("获取数据集字段异常");
            }
        }catch (Exception e){
            log.error("获取模板失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取模板失败！",null);
        }
    }

    @OperateLog(operateName = "获取报表数据集参数",interfacePath = "/excelreport/getReportDatasetsParam")
    @ApiOperation(value = "获取报表数据集参数")
    @PostMapping(value = "/getReportDatasetsParam", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Map<String, Object>> getReportDatasetsParam(@RequestBody ReportTplDatasetDto reportTplDataset,HttpServletRequest request) throws ParseException {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/getReportDatasetsParam";
            HttpEntity httpEntity = new HttpEntity(reportTplDataset, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取报表数据集参数异常");
                throw new NullPointerException("获取报表数据集参数异常");
            }
        } catch (Exception e){
            log.error("获取报表数据集参数失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取报表数据集参数失败！",null);
        }
    }

    @OperateLog(operateName = "预览报表",interfacePath = "/excelreport/previewLuckysheetReportData")
    @ApiOperation(value = "预览报表")
    @PostMapping(value = "/previewLuckysheetReportData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<ResPreviewData> previewLuckysheetReportData(@RequestBody MesGenerateReportDto mesGenerateReportDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/previewLuckysheetReportData";
            HttpEntity httpEntity = new HttpEntity(mesGenerateReportDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("预览报表异常");
                throw new NullPointerException("预览报表异常");
            }
        } catch (Exception e){
            log.error("预览报表失败：{}",e.getMessage(),e);
            return new WfResult<>("500","预览报表失败！",null);
        }
    }

    @OperateLog(operateName = "获取详细信息",interfacePath = "/excelreport/getDetail")
    @ApiOperation(value = "获取详细信息")
    @PostMapping(value = "/getDetail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<ExcelReportTplDto> getDetail(@RequestBody ExcelReportTplDto excelReportTplDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/getDetail";
            HttpEntity httpEntity = new HttpEntity(excelReportTplDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取详细信息异常");
                throw new NullPointerException("获取详细信息异常");
            }
        } catch (Exception e){
            log.error("获取报表详细信息失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取报表详细信息失败！",null);
        }
    }

    @OperateLog(operateName = "导出Excel",interfacePath = "/excelreport/luckySheetExportExcel")
    @ApiOperation(value = "导出Excel")
    @PostMapping(value = "/luckySheetExportExcel", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void luckySheetExportExcel(@RequestBody MesGenerateReportDto mesGenerateReportDto, HttpServletResponse response,HttpServletRequest request) throws Exception {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/getDetail";
            HttpEntity httpEntity = new HttpEntity(mesGenerateReportDto, httpHeaders);
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        } catch (Exception e){
            log.error("导出Excel失败：{}",e.getMessage(),e);
            throw new ServerException("导出Excel失败");
        }
    }

    @OperateLog(operateName = "生成pdf并返回访问路径",interfacePath = "/excelreport/getSheetPdf")
    @ApiOperation(value = "生成pdf")
    @PostMapping(value = "/getSheetPdf", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Map<String, Object>> getSheetPdf(@RequestBody MesGenerateReportDto mesGenerateReportDto, HttpServletResponse response,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/getSheetPdf";
            HttpEntity httpEntity = new HttpEntity(mesGenerateReportDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("生成pdf并返回访问路径异常");
                throw new NullPointerException("生成pdf并返回访问路径异常");
            }
        } catch (Exception e){
            log.error("生成pdf失败：{}",e.getMessage(),e);
            return new WfResult<>("500","生成pdf失败！",null);
        }
    }

    @OperateLog(operateName = "获取pdf流",interfacePath = "/excelreport/getSheetPdfStream")
    @ApiOperation(value = "获取pdf流")
    @PostMapping(value = "/getSheetPdfStream", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void getSheetPdfStream(@RequestBody MesGenerateReportDto mesGenerateReportDto, HttpServletResponse response,HttpServletRequest request) throws Exception {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/excelreport/getSheetPdfStream";
            HttpEntity httpEntity = new HttpEntity(mesGenerateReportDto, httpHeaders);
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        } catch (Exception e){
            log.error("生成pdf失败：{}",e.getMessage(),e);
            throw new ServerException("生成pdf失败");
        }
    }

}
