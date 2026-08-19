package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.annotation.OperatorType;
import com.cgnpc.report.annotation.UBA;
import com.cgnpc.report.httpclient.controller.dto.QueryRequestLogDTO;
import com.cgnpc.report.httpclient.controller.vo.CudRequestLogVO;
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

/**
 * <p>
 * 文件夹 前端控制器
 * </p>
 *
 * @author P629988
 * @since 2024-08-06
 */
@CrossOrigin("*")
@Api(value = "/connection-log", tags = "连接器日志控制器")
@ApiResponses(@ApiResponse(code = 404, message = "client-log not found"))
@RestController
@RequestMapping("/connection-log")
@Slf4j
public class CudRequestLogController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    /**
     * 查看连接器日志列表
     *
     * @return
     */
    @ApiOperation(value = "查看连接器日志列表")
    @PostMapping(value = "/query/requestLog")
    @UBA(module = "连接器管理-连接器日志", action = "查看连接器日志列表", channel = OperatorType.Button)
    public WfResult<IPage<CudRequestLogVO>> queryRequestLog(@Validated @RequestBody QueryRequestLogDTO queryRequestLogDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/connection-log/query/requestLog";
            HttpEntity httpEntity = new HttpEntity(queryRequestLogDTO, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody());
            }else{
                log.info("查看连接器日志列表异常");
                throw new NullPointerException("查看连接器日志列表异常");
            }
        }catch (Exception e){
            log.error("查看连接器日志列表失败：{}",e.getMessage(),e);
            return new WfResult("500",null);
        }
    }

}

