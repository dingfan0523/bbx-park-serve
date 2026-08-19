package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.domain.dashboard.Dashboard;
import com.cgnpc.report.chart.common.dto.dashboard.*;
import com.cgnpc.report.chart.common.exception.BusinessException;
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

/**
 * @author P637785
 * @description 仪表盘前端控制器
 * @date 2024/04/16
 */
@CrossOrigin("*")
@Api(value = "/dashboard", tags = "仪表盘控制器")
@ApiResponses(@ApiResponse(code = 404, message = "dashboard not found"))
@RestController
@RequestMapping("/dashboard")
@Slf4j
public class CudDashboardController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    /**
     * 分页查询
     *
     * @param queryDashboardPageDto
     * @return
     */
    @OperateLog(operateName = "分页查询",interfacePath = "/dashboard/pageInfo")
    @ApiOperation(value = "分页查询")
    @PostMapping("/pageInfo")
    public WfResult<PageInfo<Dashboard>> pageInfo(@Valid @RequestBody QueryDashboardPageDto queryDashboardPageDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/dashboard/pageInfo";
            HttpEntity httpEntity = new HttpEntity(queryDashboardPageDto, httpHeaders);
            ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(responseEntity.getBody()!=null){
                return new WfResult(responseEntity.getBody().getData());
            }else{
                log.info("获取AccessToken异常");
                throw new NullPointerException("获取AccessToken异常");
            }
        }catch (Exception e){
            log.error("查询仪表盘失败：{}",e.getMessage(),e);
            return new WfResult<>("查询仪表盘失败！",null);
        }
    }

    /**
     * 删除仪表盘
     *
     * @param dashboardDeleteDto
     * @return
     */
    @OperateLog(operateName = "删除仪表盘",interfacePath = "/dashboard/deleteDashboard")
    @ApiOperation(value = "删除仪表盘")
    @PostMapping(value = "/deleteDashboard", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> deleteDashboard(@Valid @RequestBody DashboardDeleteDto dashboardDeleteDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/dashboard/deleteDashboard";
            HttpEntity httpEntity = new HttpEntity(dashboardDeleteDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("删除仪表盘异常");
                throw new NullPointerException("删除仪表盘异常");
            }
        }catch (Exception e){
            log.error("删除仪表盘失败：{}",e.getMessage(),e);
            return new WfResult<>("删除仪表盘失败！",null);
        }
    }

    /**
     * 保存/发布仪表盘
     *
     * @param dashboardSaveDto
     * @return
     */
    @OperateLog(operateName = "保存/发布仪表盘",interfacePath = "/bigscreen/saveDashboard")
    @ApiOperation(value = "保存/发布仪表盘")
    @PostMapping(value = "/saveDashboard", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> saveDashboard(@Valid @RequestBody DashboardSaveDto dashboardSaveDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/dashboard/saveDashboard";
            HttpEntity httpEntity = new HttpEntity(dashboardSaveDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("保存/发布仪表盘异常");
                throw new NullPointerException("保存/发布仪表盘异常");
            }
        } catch (Exception e) {
            log.error("保存/发布仪表盘失败：{}", e.getMessage(), e);
            String msg = "保存/发布仪表盘失败！";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            }
            return new WfResult<>("500", msg, null);
        }
    }

    /**
     * 查询详细信息
     *
     * @param dashboardQueryDto
     * @return
     */
    @OperateLog(operateName = "按主键查询详细信息",interfacePath = "/dashboard/getDetail")
    @ApiOperation(value = "按主键查询详细信息")
    @PostMapping(value = "/getDetail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<DashboardDto> getDetail(@Valid @RequestBody DashboardQueryDto dashboardQueryDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/dashboard/getDetail";
            HttpEntity httpEntity = new HttpEntity(dashboardQueryDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("按主键查询详细信息异常");
                throw new NullPointerException("按主键查询详细信息异常");
            }
        }catch (Exception e){
            log.error("按主键查询详细信息失败：{}",e.getMessage(),e);
            return new WfResult<>("按主键查询详细信息失败！",null);
        }
    }
}
