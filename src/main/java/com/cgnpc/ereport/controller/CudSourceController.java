package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.alibaba.fastjson.JSON;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.dto.source.DatasourceTypeDto;
import com.cgnpc.report.chart.common.dto.source.SourceDBInfoDto;
import com.cgnpc.report.chart.common.dto.source.req.QuerySourceDto;
import com.cgnpc.report.core.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author P629988
 */
@CrossOrigin("*")
@Api(value = "/sources", tags = "数据源控制器")
@ApiResponses(@ApiResponse(code = 404, message = "sources not found"))
@Slf4j
@RestController
@RequestMapping("/sources")
public class CudSourceController extends BaseController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    /**
     * source 的数据库表
     *
     * @param querySourceDto
     * @param request
     * @return
     */
    @OperateLog(operateName = "get tables",interfacePath = "/sources/tables")
    @ApiOperation(value = "get tables")
    @PostMapping("/tables")
    public WfResult<List<String>> getSourceTables(@Valid @RequestBody QuerySourceDto querySourceDto,
                                          HttpServletRequest request) {

        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/sources/tables";
        HttpEntity httpEntity = new HttpEntity(querySourceDto, httpHeaders);
        ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(responseEntity.getBody()!=null){
            SourceDBInfoDto dbTableInfo = JSON.parseObject(JSON.toJSONString(responseEntity.getBody().getData()), SourceDBInfoDto.class);
            return new WfResult(dbTableInfo);
        }else{
            log.info("获取数据集明细异常");
            throw new NullPointerException("获取数据集明细异常");
        }
    }

    /**
     * 获取系统支持jdbc数据源
     *
     * @param request
     * @return
     */
    @OperateLog(operateName = "get jdbc datasources",interfacePath = "/sources/jdbc/datasources")
    @ApiOperation(value = "get jdbc datasources")
    @PostMapping("/jdbc/datasources")
    public WfResult<List<DatasourceTypeDto>> getJdbcDataSources(HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/sources/jdbc/datasources";
        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
        ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(responseEntity.getBody()!=null){
            return new WfResult(responseEntity.getBody());
        }else{
            log.info("获取数据集明细异常");
            throw new NullPointerException("获取数据集明细异常");
        }
    }

}
