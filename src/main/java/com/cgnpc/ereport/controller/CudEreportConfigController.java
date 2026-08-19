package com.cgnpc.ereport.controller;

import com.cgnpc.ereport.model.ReportTokenModel;
import com.cgnpc.ereport.service.IEreportConfigService;
import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.dto.auth.ReportAuthDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用途说明: 用户相关  前端控制器
 * 创建时间: 2024/01/11 09:14
 * @author P629988
 */
@Slf4j
@RestController
@RequestMapping("/rpt")
public class CudEreportConfigController extends BaseController {

    @Autowired
    IEreportConfigService ereportConfigService;

    /**
     * 用途说明: 获取当前用户信息
     * 参数说明 request
     * 返回值说明:
     */
    @PostMapping("/getToken")
    public WfResult<ReportAuthDto> getToken(HttpServletRequest request) {
        WfResult<ReportAuthDto> responseDto = new WfResult<>();
        try {
            log.info("getToken获取报表信息！");
            ReportTokenModel wfAuth = ereportConfigService.getAuthToken();
            if(wfAuth!=null){
                ReportAuthDto resultWfAuthDto = new ReportAuthDto();
                BeanUtils.copyProperties(wfAuth, resultWfAuthDto);
                responseDto.data(resultWfAuthDto);
            }
        } catch (Exception e) {
            log.error("获取报表信息！");
            responseDto.error("获取报表信息！");
        }
        return responseDto;
    }

    /**
     * 用途说明: 获取报表租户编码
     * 参数说明 request
     * 返回值说明:
     */
    @PostMapping("/getEreportCode")
    public WfResult<String> getEreportCode(HttpServletRequest request) {
        WfResult<String> responseDto = new WfResult<>();
        try {
            String code = ereportConfigService.getEreportCode();
            responseDto.data(code);
        } catch (Exception e) {
            log.error("获取报表租户编码！");
            responseDto.error("获取报表租户编码！");
        }
        return responseDto;
    }
}