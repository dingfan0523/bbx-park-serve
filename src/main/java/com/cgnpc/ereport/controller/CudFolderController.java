package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.domain.Folder;
import com.cgnpc.report.chart.common.domain.Report;
import com.cgnpc.report.chart.common.dto.folder.FolderCreateDto;
import com.cgnpc.report.chart.common.dto.folder.FolderDeleteDto;
import com.cgnpc.report.chart.common.dto.folder.FolderQueryDto;
import com.cgnpc.report.chart.common.dto.folder.FolderUpdateDto;
import com.cgnpc.report.chart.common.exception.BusinessException;
import com.github.pagehelper.util.StringUtil;
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
import java.util.List;

/**
 * <p>
 * 文件夹 前端控制器
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-10-18
 */
@CrossOrigin("*")
@Api(value = "/mapper/folder", tags = "文件夹控制器")
@ApiResponses(@ApiResponse(code = 404, message = "folder not found"))
@RestController
@RequestMapping("/folder")
@Slf4j
public class CudFolderController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    @ApiOperation(value = "创建文件夹")
    @PostMapping(value = "/createFolder", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> createReport(@Valid @RequestBody FolderCreateDto folderCreateDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/folder/createFolder";
            HttpEntity httpEntity = new HttpEntity(folderCreateDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("创建用户文件夹集合异常");
                throw new NullPointerException("获取用户文件夹集合异常");
            }
        }catch (Exception e){
            log.error("创建文件夹失败：{}",e.getMessage(),e);
            return new WfResult<>("500","创建文件夹失败！",null);
        }
    }

    @OperateLog(operateName = "更新文件夹",interfacePath = "/folder/updateFolder")
    @ApiOperation(value = "更新文件夹")
    @PostMapping(value = "/updateFolder", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> updateFolder(@Valid @RequestBody FolderUpdateDto folderUpdateDto, HttpServletRequest request) {
        Assert.isTrue(StringUtil.isEmpty(folderUpdateDto.getParentId())
                        &&folderUpdateDto.getIsRoot().compareTo(Boolean.TRUE)==0,
                "非根目录parentId不能为空！");
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/folder/updateFolder";
            HttpEntity httpEntity = new HttpEntity(folderUpdateDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("更新文件夹异常");
                throw new NullPointerException("更新文件夹异常");
            }
        }catch (Exception e){
            log.error("更新文件夹失败：{}",e.getMessage(),e);
            return new WfResult<>("500","更新文件夹失败！",null);
        }

    }

    @OperateLog(operateName = "获取用户文件夹集合",interfacePath = "/folder/fetchFolders")
    @ApiOperation(value = "获取用户文件夹集合")
    @PostMapping(value = "/fetchFolders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<List<Report>> fetchFolders(@Valid @RequestBody FolderQueryDto folderQueryDto, HttpServletRequest request) {
        log.error("获取文件夹集合fetchFolders：{}", JSON.toJSONString(folderQueryDto));
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/folder/fetchFolders";
            HttpEntity httpEntity = new HttpEntity(folderQueryDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                List<Folder> folders = (List<Folder>) apiResult.getBody().getData();
                return new WfResult(folders);
            }else{
                log.info("获取用户文件夹集合异常");
                throw new NullPointerException("获取用户文件夹集合异常");
            }
        }catch (Exception e){
            log.error("获取用户文件夹集合失败：{}",e.getMessage(),e);
            return new WfResult<>("获取用户文件夹集合失败！",null);
        }
    }

    @OperateLog(operateName = "获取所有文件夹集合",interfacePath = "/folder/fetchAllFolders")
    @ApiOperation(value = "获取所有文件夹集合")
    @PostMapping(value = "/fetchAllFolders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<List<Report>> fetchAllFolders(@Valid @RequestBody FolderQueryDto folderQueryDto, HttpServletRequest request) {
        log.info("获取所有文件夹集合fetchAllFolders：{}", JSON.toJSONString(folderQueryDto));
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/folder/fetchAllFolders";
            HttpEntity httpEntity = new HttpEntity(folderQueryDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取所有文件夹集合异常");
                throw new NullPointerException("获取所有文件夹集合异常");
            }
        }catch (Exception e){
            log.error("获取所有文件夹集合失败：{}",e.getMessage(),e);
            if(e instanceof BusinessException){
                return new WfResult<>("500","获取文件夹集合失败！"+e.getMessage(),null);
            }else {
                return new WfResult<>("500", "获取文件夹集合失败！", null);
            }
        }
    }

    @OperateLog(operateName = "根据ID集合删除文件夹",interfacePath = "/folder/deleteByIds")
    @ApiOperation(value = "根据ID集合删除文件夹")
    @PostMapping(value = "/deleteByIds", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> deleteByIds(@Valid @RequestBody FolderDeleteDto folderDeleteDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/folder/deleteByIds";
            HttpEntity httpEntity = new HttpEntity(folderDeleteDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("根据ID集合删除文件夹异常");
                throw new NullPointerException("根据ID集合删除文件夹异常");
            }
        }catch (Exception e){
            log.error("根据ID集合删除文件夹失败：{}",e.getMessage(),e);
            return new WfResult<>("根据ID集合删除文件夹失败！",null);
        }
    }
}

