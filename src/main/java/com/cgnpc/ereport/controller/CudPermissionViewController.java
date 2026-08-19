package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.domain.PermissionView;
import com.cgnpc.report.chart.common.dto.permission.PermissionViewColumnsDto;
import com.cgnpc.report.chart.common.dto.permission.PermissionViewCreateDto;
import com.cgnpc.report.chart.common.dto.permission.PermissionViewQueryDto;
import com.cgnpc.report.chart.common.dto.permission.PermissionViewUpdateDto;
import com.cgnpc.report.chart.common.dto.source.req.QuerySourceTenantDto;
import com.cgnpc.report.chart.common.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 数据集权限 前端控制器
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-10-09
 */
@CrossOrigin("*")
@Api(value = "/permission-view", tags = "数据集权限控制器")
@RestController
@RequestMapping("/permission-view")
@Slf4j
public class CudPermissionViewController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    @OperateLog(operateName = "创建数据集权限",interfacePath = "/permission-view/createPermission")
    @ApiOperation(value = "创建数据集权限")
    @PostMapping(value = "/createPermission", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> createPermission(@Valid @RequestBody PermissionViewCreateDto permissionViewCreateDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-view/createPermission";
            HttpEntity httpEntity = new HttpEntity(permissionViewCreateDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("创建数据集权限异常");
                throw new NullPointerException("创建数据集权限异常");
            }
        }catch (Exception e){
            log.error("创建数据集权限失败：{}",e.getMessage(),e);
            return new WfResult<>("500","创建数据集权限失败！",null);
        }

    }

    @OperateLog(operateName = "更新数据集权限",interfacePath = "/permission-view/updatePermission")
    @ApiOperation(value = "更新数据集权限")
    @PostMapping(value = "/updatePermission", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> updatePermission(@Valid @RequestBody PermissionViewUpdateDto permissionViewUpdateDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-view/updatePermission";
            HttpEntity httpEntity = new HttpEntity(permissionViewUpdateDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("创建数据集权限异常");
                throw new NullPointerException("创建数据集权限异常");
            }
        }catch (Exception e){
            log.error("更新数据集权限失败：{}",e.getMessage(),e);
            return new WfResult<>("500","更新数据集权限表失败！",null);
        }

    }

    @OperateLog(operateName = "更新数据集对应人员列权限",interfacePath = "/permission-view/updateColumns")
    @ApiOperation(value = "更新数据集对应人员列权限")
    @PostMapping(value = "/updateColumns", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> updateColumns(@Valid @RequestBody PermissionViewColumnsDto viewColumnsDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-view/updatePermission";
            HttpEntity httpEntity = new HttpEntity(viewColumnsDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("更新数据集对应人员列权限异常");
                throw new NullPointerException("更新数据集对应人员列权限异常");
            }
        }catch (Exception e){
            log.error("更新数据集对应列权限失败：{}",e.getMessage(),e);
            return new WfResult<>("500","更新数据集对应列权限失败！",null);
        }

    }

    @OperateLog(operateName = "删除数据集对应人员列权限",interfacePath = "/permission-view/deleteUserPerm")
    @ApiOperation(value = "删除数据集对应人员列权限")
    @PostMapping(value = "/deleteUserPerm", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> deleteUserPerm(@Valid @RequestBody PermissionViewColumnsDto viewColumnsDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-view/deleteUserPerm";
            HttpEntity httpEntity = new HttpEntity(viewColumnsDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("删除数据集对应人员列权限异常");
                throw new NullPointerException("删除数据集对应人员列权限异常");
            }
        }catch (Exception e){
            log.error("删除数据集对应人员列权限失败：{}",e.getMessage(),e);
            return new WfResult<>("500","删除数据集对应人员列权限失败！",null);
        }

    }

    @OperateLog(operateName = "查询数据集对应人员列权限",interfacePath = "/permission-view/findUserPerm")
    @ApiOperation(value = "查询数据集对应人员列权限")
    @PostMapping(value = "/findUserPerm", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<?> findUserPerm(@Valid @RequestBody PermissionViewColumnsDto viewColumnsDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-view/findUserPerm";
            HttpEntity httpEntity = new HttpEntity(viewColumnsDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("查询数据集对应人员列权限异常");
                throw new NullPointerException("查询数据集对应人员列权限异常");
            }
        }catch (Exception e){
            log.error("查询数据集对应人员列权限失败：{}",e.getMessage(),e);
            return new WfResult<>("500","查询数据集对应人员列权限失败！",null);
        }

    }

    @OperateLog(operateName = "根据ID获取数据集权限",interfacePath = "/permission-view/findById")
    @ApiOperation(value = "根据ID获取数据集权限")
    @PostMapping(value = "/findById", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<PermissionView> findById(@Valid @RequestBody PermissionViewQueryDto permissionViewQueryDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-view/findById";
            HttpEntity httpEntity = new HttpEntity(permissionViewQueryDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("根据ID获取数据集权限异常");
                throw new NullPointerException("根据ID获取数据集权限异常");
            }
        }catch (Exception e){
            log.error("根据ID获取数据集权限失败：{}",e.getMessage(),e);
            return new WfResult<>("500","根据ID获取数据集权限失败！",null);
        }

    }


    /**
     * 根据数据源ID和租户ID获取数据集
     *
     * @param querySourceTenantDto
     * @param request
     * @return
     */
    @OperateLog(operateName = "根据数据源ID和租户ID获取数据集",interfacePath = "/permission-view/testSource")
    @ApiOperation(value = "根据数据源ID和租户ID获取数据集", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/testSource", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<?> getViewBySourceAndTenant(@Valid @RequestBody QuerySourceTenantDto querySourceTenantDto,
                                                    HttpServletRequest request) {
        WfResult result = new WfResult();
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/permission-view/testSource";
            HttpEntity httpEntity = new HttpEntity(querySourceTenantDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("根据数据源ID和租户ID获取数据集异常");
                throw new NullPointerException("根据数据源ID和租户ID获取数据集异常");
            }
        }catch (Exception e){
            if(e instanceof BusinessException){
                result.error("根据数据源ID和租户ID获取数据集出现异常:"+e.getMessage());
            }else{
                result.error("根据数据源ID和租户ID获取数据集服务异常");
            }
        }
        return result;
    }

}

