package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.alibaba.fastjson.JSON;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.domain.PermissionSource;
import com.cgnpc.report.chart.common.domain.PermissionSourceMember;
import com.cgnpc.report.chart.common.domain.Source;
import com.cgnpc.report.chart.common.dto.permission.*;
import com.cgnpc.report.chart.common.dto.source.req.QuerySourcePermMemberPageDto;
import com.cgnpc.report.chart.common.dto.source.req.QuerySourcePermPageDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 数据源权限 前端控制器
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-10-09
 */
@CrossOrigin("*")
@Api(value = "/mapper/report", tags = "数据源权限控制器")
@RestController
@RequestMapping("/permission-source")
@Slf4j
public class CudPermissionSourceController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    @OperateLog(operateName = "创建数据源权限",interfacePath = "/permission-source/createPermission")
    @ApiOperation(value = "创建数据源权限")
    @PostMapping(value = "/createPermission", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> createPermission(@Valid @RequestBody PermissionSourceCreateDto permissionSourceCreateDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-source/createPermission";
            HttpEntity httpEntity = new HttpEntity(permissionSourceCreateDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("创建数据源权限异常");
                throw new NullPointerException("创建数据源权限异常");
            }
        }catch (Exception e){
            log.error("创建数据源权限失败：{}",e.getMessage(),e);
            return new WfResult<>("500","创建数据源权限失败！",null);
        }

    }

    @OperateLog(operateName = "更新数据源权限",interfacePath = "/permission-source/updatePermission")
    @ApiOperation(value = "更新数据源权限")
    @PostMapping(value = "/updatePermission", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> updatePermission(@Valid @RequestBody PermissionSourceUpdateDto permissionSourceUpdateDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-source/updatePermission";
            HttpEntity httpEntity = new HttpEntity(permissionSourceUpdateDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(permissionSourceUpdateDto.getId());
            }else{
                log.info("更新数据源权限异常");
                throw new NullPointerException("更新数据源权限异常");
            }
        }catch (Exception e){
            log.error("更新数据源权限失败：{}",e.getMessage(),e);
            return new WfResult<>("500","更新数据源权限表失败！",null);
        }

    }

    @OperateLog(operateName = "根据ID获取数据源权限",interfacePath = "/permission-source/findById")
    @ApiOperation(value = "根据ID获取数据源权限")
    @PostMapping(value = "/findById", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<PermissionSource> findById(@Valid @RequestBody PermissionSourceUniqueDto permissionSourceUniqueDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-source/findById";
            HttpEntity httpEntity = new HttpEntity(permissionSourceUniqueDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("根据ID获取数据源权限异常");
                throw new NullPointerException("根据ID获取数据源权限异常");
            }
        }catch (Exception e){
            log.error("根据ID获取数据源权限失败：{}",e.getMessage(),e);
            return new WfResult<>("500","根据ID获取数据源权限失败！",null);
        }

    }

    /**
     * 分页查询
     *
     * @param querySourcePermPageDto
     * @param request
     * @return
     */
    @OperateLog(operateName = "分页查询",interfacePath = "/permission-source/pageInfo")
    @ApiOperation(value = "query pageInfo", notes = "分页查询")
    @PostMapping("/pageInfo")
    public WfResult<PageInfo<PermissionSource>> pageInfo(@Valid @RequestBody QuerySourcePermPageDto querySourcePermPageDto,
                                                             HttpServletRequest request) {
        WfResult result = new WfResult();
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-source/pageInfo";
            HttpEntity httpEntity = new HttpEntity(querySourcePermPageDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("分页查询异常");
                throw new NullPointerException("分页查询异常");
            }
        }catch (Exception e){
            log.error("query pageSourceInfo occurred Exception:{}",e.getMessage(),e);
            result.error(e.getMessage());
            return result;
        }
    }

    /**
     * 分页查询
     *
     * @param querySourcePermPageDto
     * @param request
     * @return
     */
    @OperateLog(operateName = "分页查询",interfacePath = "/permission-source/pagePermInfo")
    @ApiOperation(value = "query pagePermInfo", notes = "分页查询")
    @PostMapping("/pagePermInfo")
    public WfResult<PageInfo<PermissionSource>> pagePermInfo(@Valid @RequestBody QuerySourcePermPageDto querySourcePermPageDto,
                                                                 HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/permission-source/pagePermInfo";
        HttpEntity httpEntity = new HttpEntity(querySourcePermPageDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("分页查询异常");
            throw new NullPointerException("分页查询异常");
        }
    }

    /**
     * 分页查询
     *
     * @param querySourcePermMemberPageDto
     * @param request
     * @return
     */
    @OperateLog(operateName = "分页查询权限成员",interfacePath = "/permission-source/pageMemberInfo")
    @ApiOperation(value = "query pageMemberInfo", notes = "分页查询权限成员")
    @PostMapping("/pageMemberInfo")
    public WfResult<PageInfo<PermissionSourceMember>> pageMemberInfo(@Valid @RequestBody QuerySourcePermMemberPageDto querySourcePermMemberPageDto,
                                                                         HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/permission-source/pageMemberInfo";
        HttpEntity httpEntity = new HttpEntity(querySourcePermMemberPageDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("分页查询异常");
            throw new NullPointerException("分页查询异常");
        }
    }

    /**
     * 删除数据源权限成员
     *
     * @param deleteDto
     * @param request
     * @return
     */
    @OperateLog(operateName = "删除数据源权限成员",interfacePath = "/permission-source/deleteMemberByIds")
    @ApiOperation(value = "delete members by ids", notes = "删除数据源权限成员")
    @PostMapping("/deleteMemberByIds")
    public WfResult<Boolean> deleteMemberByIds(@Valid @RequestBody PermissionSourceDeleteDto deleteDto,
                                                   HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/permission-source/deleteMemberByIds";
        HttpEntity httpEntity = new HttpEntity(deleteDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("删除数据源权限成员异常");
            throw new NullPointerException("删除数据源权限成员异常");
        }
    }

    @OperateLog(operateName = "根据用户和租户ID查询已授权的数据源",interfacePath = "/permission-source/findUserSource")
    @ApiOperation(value = "根据用户和租户ID查询已授权的数据源")
    @PostMapping(value = "/findUserSource", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Source> findUserSource(@RequestBody PermissionUserSourceDto userSourceDto,HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/permission-source/findUserSource";
        HttpEntity httpEntity = new HttpEntity(userSourceDto, httpHeaders);
        ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(responseEntity.getBody()!=null){
            List<Source> sourceList =JSON.parseArray(JSON.toJSONString(responseEntity.getBody().getData()), Source.class);
            return new WfResult(sourceList);
        }else{
            log.info("删除数据源权限成员异常");
            throw new NullPointerException("删除数据源权限成员异常");
        }
    }

}

