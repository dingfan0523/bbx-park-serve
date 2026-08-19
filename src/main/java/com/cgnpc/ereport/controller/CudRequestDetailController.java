package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.annotation.OperatorType;
import com.cgnpc.report.annotation.UBA;
import com.cgnpc.report.httpclient.controller.dto.*;
import com.cgnpc.report.httpclient.controller.vo.ConnectorDetailVO;
import com.cgnpc.report.httpclient.controller.vo.CudConnectorDetailVO;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件夹 前端控制器
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-10-18
 */
@CrossOrigin("*")
@Api(value = "/connection-detail", tags = "连接器管理")
@ApiResponses(@ApiResponse(code = 404, message = "client-detail not found"))
@RestController
@RequestMapping("/connection-detail")
@Slf4j
public class CudRequestDetailController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    @ApiOperation(value = "格式化soap 协议xml数据")
    @PostMapping(value = "/format/xmlToJsonBySoap")
    @UBA(module = "连接器管理", action = "格式化soap 协议xml数据", channel = OperatorType.Button)
    public WfResult<Map> formatXmlToJsonBySoap(@RequestBody FormatXmlDTO formatXmlDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/format/xmlToJsonBySoap";
            HttpEntity httpEntity = new HttpEntity(formatXmlDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("格式化soap协议xml数据异常");
                throw new NullPointerException("格式化soap协议xml数据异常");
            }
        }catch (Exception e){
            log.error("格式化soap协议xml数据失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * http连接器信息及动作录入
     *
     * @return
     */
    @ApiOperation(value = "批量http连接器动作录入或修改（此接口只操作连接器动作）")
    @PostMapping(value = "/insert/RequestDetail")
    @UBA(module = "连接器管理", action = "批量http连接器动作录入或修改（此接口只操作连接器动作）", channel = OperatorType.Button)
    public WfResult<Boolean> insertRequestDetail(@Validated @RequestBody List<InsertConnectorDetailDTO> insertConnectorDetailDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/insert/RequestDetail";
            HttpEntity httpEntity = new HttpEntity(insertConnectorDetailDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("批量http连接器动作录入或修改异常");
                throw new NullPointerException("批量http连接器动作录入或修改异常");
            }
        }catch (Exception e){
            log.error("批量http连接器动作录入或修改失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * http连接器信息录入
     *
     * @return
     */
    @ApiOperation(value = "http连接器信息录入或修改（此接口只操作连接器信息")
    @PostMapping(value = "/insert/Connector/v2")
    @UBA(module = "连接器管理", action = "http连接器信息录入或修改（此接口只操作连接器信息）", channel = OperatorType.Button)
    public WfResult<Boolean> insertConnector(@Validated @RequestBody
                                                             InsertConnectorAndRequestDTO insertConnectorAndRequestDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/insert/Connector/v2";
            HttpEntity httpEntity = new HttpEntity(insertConnectorAndRequestDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("http连接器信息录入或修改异常");
                throw new NullPointerException("http连接器信息录入或修改异常");
            }
        }catch (Exception e){
            log.error("http连接器信息录入或修改失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * http连接器 修改分类
     *
     * @return
     */
    @ApiOperation(value = "http连接器 修改分类")
    @PostMapping(value = "/update/connectorType")
    @UBA(module = "连接器管理", action = "http连接器 修改分类", channel = OperatorType.Button)
    public WfResult<Boolean> updateConnectorType(@Validated @RequestBody
                                                                 UpdateClassTypeDTO updateClassTypeDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/update/connectorType";
            HttpEntity httpEntity = new HttpEntity(updateClassTypeDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("修改分类异常");
                throw new NullPointerException("修改分类异常");
            }
        }catch (Exception e){
            log.error("修改分类失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * 批量删除http连接器动作
     *
     * @return
     */
    @ApiOperation(value = "批量删除http连接器动作")
    @PostMapping(value = "/delete/RequestDetail/v2")
    @UBA(module = "连接器管理", action = "批量删除http连接器动作", channel = OperatorType.Button)
    public WfResult<Boolean> deleteConnector(@Validated @RequestBody DeleteConnectorDTO deleteConnectorDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/delete/RequestDetail/v2";
            HttpEntity httpEntity = new HttpEntity(deleteConnectorDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("批量删除http连接器动作异常");
                throw new NullPointerException("批量删除http连接器动作异常");
            }
        }catch (Exception e){
            log.error("批量删除http连接器动作失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * 根据连接器id查看连接器信息及动作详情
     *
     * @return
     */
    @ApiOperation(value = "根据连接器id查看连接器信息及动作详情")
    @GetMapping(value = "/query/connectorDetail")
    @UBA(module = "连接器管理", action = "根据连接器id查看连接器信息及动作详情", channel = OperatorType.Button)
    public WfResult<ConnectorDetailVO> queryConnectorDetail(@RequestParam("connectorId") String connectorId, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/query/connectorDetail";
            HttpEntity httpEntity = new HttpEntity(connectorId, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("根据连接器id查看连接器信息及动作详情异常");
                throw new NullPointerException("根据连接器id查看连接器信息及动作详情异常");
            }
        }catch (Exception e){
            log.error("根据连接器id查看连接器信息及动作详情失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * 根据连接器id查看连接器信息及动作详情集合
     *
     * @return
     */
    @ApiOperation(value = "根据连接器id查看连接器信息及动作详情集合")
    @PostMapping(value = "/query/connectorDetail/v2")
    @UBA(module = "连接器管理", action = "根据连接器id查看连接器信息及动作详情集合", channel = OperatorType.Button)
    public WfResult<List<ConnectorDetailVO>> queryConnectorDetailVersion2(@Validated @RequestBody QueryConnectorDetailListDTO queryConnectorDetailListDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/query/connectorDetail/V2";
            HttpEntity httpEntity = new HttpEntity(queryConnectorDetailListDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("根据连接器id查看连接器信息及动作详情集合异常");
                throw new NullPointerException("根据连接器id查看连接器信息及动作详情集合异常");
            }
        }catch (Exception e){
            log.error("根据连接器id查看连接器信息及动作详情集合失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * 生成指定长度连接器动作UUID
     *
     * @return
     */
    @ApiOperation(value = "生成指定长度连接器动作UUID")
    @GetMapping(value = "/randomUUID")
    @UBA(module = "连接器管理", action = "生成指定长度连接器动作UUID", channel = OperatorType.Button)
    public WfResult<String> randomUUID(@RequestParam("length") Integer length, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/randomUUID";
            HttpEntity httpEntity = new HttpEntity(length, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("生成指定长度连接器动作UUID异常");
                throw new NullPointerException("生成指定长度连接器动作UUID异常");
            }
        }catch (Exception e){
            log.error("生成指定长度连接器动作UUID失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * 验证连接器动作id唯一性
     *
     * @return
     */
    @ApiOperation(value = "验证连接器动作id唯一性",notes = "存在为false; 不存在为true")
    @PostMapping(value = "/validated/requestId")
    @UBA(module = "连接器管理", action = "验证连接器动作id唯一性", channel = OperatorType.Button)
    public WfResult<Boolean> validatedRequestId(@RequestParam("requestId") String requestId, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/validated/requestId";
            HttpEntity httpEntity = new HttpEntity(requestId, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("验证连接器动作id唯一性异常");
                throw new NullPointerException("验证连接器动作id唯一性异常");
            }
        }catch (Exception e){
            log.error("验证连接器动作id唯一性失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

    /**
     * 验证连接器名称唯一性
     *
     * @return
     */
    @ApiOperation(value = "验证连接器名称唯一性",notes = "存在为false; 不存在为true")
    @PostMapping(value = "/validated/connectorName")
    @UBA(module = "连接器管理", action = "验证连接器名称唯一性", channel = OperatorType.Button)
    public WfResult<Boolean> validatedCnnectorName(@RequestParam("connectorName") String connectorName, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/validated/connectorName";
            HttpEntity httpEntity = new HttpEntity(connectorName, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("验证连接器动作id唯一性异常");
                throw new NullPointerException("验证连接器动作id唯一性异常");
            }
        }catch (Exception e){
            log.error("验证连接器动作id唯一性失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }


    /**
     * 导出连接器信息数据
     *
     * @return
     */
    @ApiOperation(value = "导出连接器信息数据")
    @GetMapping(value = "/export/connector")
    @UBA(module = "连接器管理", action = "导出连接器信息数据", channel = OperatorType.Button)
    public void exportConnector(HttpServletResponse response, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/export/connector";
            HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                throw new NullPointerException("导出连接器信息数据异常");
            }
        }catch (Exception e){
            log.error("导出连接器信息数据失败：{}",e.getMessage(),e);
            throw new NullPointerException("导出连接器信息数据失败");
        }
    }

    @GetMapping(value = "/")
    public WfResult<CudConnectorDetailVO> getConnectorDetailById(@RequestParam("id") String id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-detail/";
            HttpEntity httpEntity = new HttpEntity(id, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                throw new NullPointerException("获取连接器明细异常");
            }
        }catch (Exception e){
            log.error("获取连接器明细失败：{}",e.getMessage(),e);
            throw new NullPointerException("获取连接器明细失败");
        }
    }

}

