package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.annotation.OperatorType;
import com.cgnpc.report.annotation.UBA;
import com.cgnpc.report.httpclient.application.constants.HttpClientConstants;
import com.cgnpc.report.httpclient.application.result.HttpClientResult;
import com.cgnpc.report.httpclient.controller.dto.*;
import com.cgnpc.report.httpclient.controller.vo.CudClassTypeVO;
import com.cgnpc.report.httpclient.domain.CudConnector;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 文件夹 前端控制器
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-10-18
 */
@CrossOrigin("*")
@Api(value = "/connection", tags = "ereport-http连接器")
@ApiResponses(@ApiResponse(code = 404, message = "connection not found"))
@RestController
@RequestMapping("/connection")
@Slf4j
public class CudHttpClientController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;


    /**
     * 配置并发送相关请求
     *
     * @return
     */
    @ApiOperation(value = "配置并发送相关请求")
    @PostMapping(value = "/untitled/request")
    @UBA(module = "连接器管理-http连接器", action = "配置并发送相关请求", channel = OperatorType.Button)
    public WfResult untitledRequest(@Validated @RequestBody HttpRequestParamDTO httpRequestParamDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection/untitled/request";
            Integer interfaceType = httpRequestParamDTO.getInterfaceType();
            //默认为JSON请求
            if (Objects.isNull(interfaceType)) {
                httpRequestParamDTO.setInterfaceType(HttpClientConstants.INTERFACE_TYPE_JSON);
            }
            HttpEntity httpEntity = new HttpEntity(httpRequestParamDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("配置并发送相关请求异常");
                throw new NullPointerException("配置并发送相关请求异常");
            }
        }catch (Exception e){
            log.error("配置并发送相关请求失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }

    }

    /**
     * @Author P629988
     * @Description  验证接口返回参数规则
     * @Param [connectorVerificationVO, request]
     * @return HttpClientResult
     **/
    @ApiOperation(value = "验证接口返回参数规则")
    @PostMapping("Connector/getVerificationParameters")
    @UBA(module = "连接器管理-http连接器", action = "验证接口返回参数规则", channel = OperatorType.Button)
    public HttpClientResult getVerificationParameters(@RequestBody QueryConnectorDetailDTO connectorVerificationVO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection/Connector/getVerificationParameters";
            HttpEntity httpEntity = new HttpEntity(connectorVerificationVO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new HttpClientResult(apiResult.getBody());
            }else{
                log.info("验证接口返回参数规则异常");
                throw new NullPointerException("验证接口返回参数规则异常");
            }
        }catch (Exception e){
            log.error("验证接口返回参数规则失败：{}",e.getMessage(),e);
            return new HttpClientResult("500",null);
        }
    }

    /**
     * 查看所有连接信息分类
     *
     * @return
     */
    @ApiOperation(value = "查看所有连接信息分类（支持模糊搜索）")
    @PostMapping(value = "/query/classType")
    @UBA(module = "连接器管理-http连接器", action = "查看所有连接信息分类", channel = OperatorType.Button)
    public WfResult<List<CudClassTypeVO>> queryClassType(@Validated @RequestBody ClassTypeDTO classTypeDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection/query/classType";
            HttpEntity httpEntity = new HttpEntity(classTypeDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new  WfResult(apiResult.getBody());
            }else{
                log.info("查看所有连接信息分类异常");
                throw new NullPointerException("查看所有连接信息分类异常");
            }
        }catch (Exception e){
            log.error("查看所有连接信息分类失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * 分页查看连接信息
     *
     * @return
     */
    @ApiOperation(value = "分页查看连接信息")
    @PostMapping(value = "/query/connector")
    @UBA(module = "连接器管理-http连接器", action = "分页查看连接信息", channel = OperatorType.Button)
    public WfResult<IPage<CudConnector>> queryConnector(@Validated @RequestBody QueryConnectorDTO queryConnectorDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection/query/connector";
            HttpEntity httpEntity = new HttpEntity(queryConnectorDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("分页查看连接信息异常");
                throw new NullPointerException("分页查看连接信息异常");
            }
        }catch (Exception e){
            log.error("分页查看连接信息失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }

    }

    /**
     * 查看连接信息详情
     *
     * @return
     */
    @ApiOperation(value = "查看连接信息详情")
    @GetMapping(value = "/query/connectorDetail")
    @UBA(module = "连接器管理-http连接器", action = "查看连接信息详情", channel = OperatorType.Button)
    public WfResult<IPage<CudConnector>> queryConnectorDetail(@RequestParam("connectorId") String connectorId, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection/query/connector";
            HttpEntity httpEntity = new HttpEntity(connectorId, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("分页查看连接信息异常");
                throw new NullPointerException("分页查看连接信息异常");
            }
        }catch (Exception e){
            log.error("分页查看连接信息失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }

    }

    /**
     * 分类信息录入/修改分类信息
     *
     * @return
     */
    @ApiOperation(value = "分类信息录入/修改分类信息")
    @PostMapping(value = "/ClassType/insClassType")
    @UBA(module = "连接器管理-http连接器", action = "分类信息录入/修改分类信息", channel = OperatorType.Button)
    public WfResult setClassType(@Validated @RequestBody ClassTypeAddDTO classTypeAddDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection/ClassType/insClassType";
            HttpEntity httpEntity = new HttpEntity(classTypeAddDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("修改分类信息异常");
                throw new NullPointerException("修改分类信息异常");
            }
        }catch (Exception e){
            log.error("修改分类信息失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * 分类信息删除
     *
     * @return
     */
    @ApiOperation(value = "分类信息删除")
    @PostMapping(value = "/ClassType/delClassType")
    @UBA(module = "连接器管理-http连接器", action = "分类信息删除", channel = OperatorType.Button)
    public WfResult deleteClassType(@Validated @RequestBody DeleteClassTypeDTO deleteClassTypeDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection/ClassType/delClassType";
            HttpEntity httpEntity = new HttpEntity(deleteClassTypeDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("分类信息删除异常");
                throw new NullPointerException("分类信息删除异常");
            }
        }catch (Exception e){
            log.error("分类信息删除失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }

    }

    /**
     * http连接信息删除
     *
     * @return
     */
    @ApiOperation(value = "http连接信息删除")
    @PostMapping(value = "/Connector/delConnector")
    @UBA(module = "连接器管理-http连接器", action = "http连接信息删除", channel = OperatorType.Button)
    public WfResult deleteConnector(@Validated @RequestBody DeleteControllerSingleDTO deleteControllerSingleDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection/Connector/delConnector";
            HttpEntity httpEntity = new HttpEntity(deleteControllerSingleDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("http连接信息删除异常");
                throw new NullPointerException("http连接信息删除异常");
            }
        }catch (Exception e){
            log.error("http连接信息删除失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }

    }

    @PostMapping(value = "/getConnectorTree")
    public WfResult getConnectorTree(@RequestBody QueryConnectorDTO baseDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection/getConnectorTree";
            HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("获取连接信息异常");
                throw new NullPointerException("获取连接信息异常");
            }
        }catch (Exception e){
            log.error("获取连接信息失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

}

