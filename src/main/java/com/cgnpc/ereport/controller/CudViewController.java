package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.alibaba.fastjson.JSON;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.core.model.Paginate;
import com.cgnpc.report.chart.common.core.model.PaginateWithQueryColumns;
import com.cgnpc.report.chart.common.core.page.ReportPageInfo;
import com.cgnpc.report.chart.common.domain.View;
import com.cgnpc.report.chart.common.dto.permission.UserPermitViewDto;
import com.cgnpc.report.chart.common.dto.view.*;
import com.cgnpc.report.core.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;

/**
 * @author P629988
 */
@CrossOrigin("*")
@Api(value = "/views", tags = "数据集控制器")
@ApiResponses(@ApiResponse(code = 404, message = "view not found"))
@Slf4j
@RestController
@RequestMapping("/views")
public class CudViewController extends BaseController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    /**
     * 分页查询
     *
     * @param queryViewPageDto
     * @param request
     * @return
     */
    @OperateLog(operateName = "数据集分页查询",interfacePath = "/views/pageViewInfo")
    @ApiOperation(value = "数据集分页查询")
    @PostMapping("/pageViewInfo")
    public WfResult<ReportPageInfo<UserPermitViewDto>> pageViewInfo(@Valid @RequestBody QueryViewPageDto queryViewPageDto,
                                                                    HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/pageViewInfo";
        HttpEntity httpEntity = new HttpEntity(queryViewPageDto, httpHeaders);
        ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(responseEntity.getStatusCodeValue()==200){
//            ReportPageInfo<UserPermitViewDto> pageInfo = new ReportPageInfo<>();
//            ApiResult apiResult = responseEntity.getBody();
//            LinkedHashMap linkedHashMap = (LinkedHashMap) apiResult.getData();
//            List<UserPermitViewDto> userPermitViewDtoList = (List<UserPermitViewDto>) linkedHashMap.get("list");
//            pageInfo.setList(userPermitViewDtoList);
//            pageInfo.setTotal((Integer) linkedHashMap.get("total"));
//            pageInfo.setPageNum((Integer) linkedHashMap.get("pageNum"));
//            pageInfo.setPageSize((Integer) linkedHashMap.get("pageSize"));
//            result.data(pageInfo);
            return new WfResult(responseEntity.getBody().getData());
        }else{
            log.info("数据集分页查询异常");
            throw new NullPointerException("数据集分页查询异常");
        }
    }

    /**
     * 获取数据集树
     *
     * @param queryViewPageDto
     * @param request
     * @return
     */
    @OperateLog(operateName = "查询数据集树",interfacePath = "/views/getViewTree")
    @ApiOperation(value = "查询数据集树")
    @PostMapping("/getViewTree")
    public WfResult<ArrayList<HashMap<String, Object>>> getViewTree(@Valid @RequestBody QueryViewPageDto queryViewPageDto,
                                                       HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/getViewTree";
        HttpEntity httpEntity = new HttpEntity(queryViewPageDto, httpHeaders);
        ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(responseEntity.getBody()!=null){
            ApiResult apiResult = responseEntity.getBody();
            LinkedHashMap linkedHashMap = (LinkedHashMap) apiResult.getData();
//            ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) linkedHashMap.get("data");
             return new WfResult(linkedHashMap);
        }else{
            log.info("查询数据集树异常");
            throw new NullPointerException("查询数据集树异常");
        }
    }

    /**
     * --------------------------------------------
     * 获取view
     *
     * @param viewQueryDto 数据源id
     * @param request
     * @return
     */
    @OperateLog(operateName = "获取数据集列表",interfacePath = "/views/getViews")
    @ApiOperation(value = "获取数据集列表")
    @PostMapping("/getViews")
    public WfResult<List<View>> getViews(@Valid @RequestBody ViewQueryDto viewQueryDto,
                                             HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/getViews";
        HttpEntity httpEntity = new HttpEntity(viewQueryDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody());
        }else{
            log.info("获取数据集列表异常");
            throw new NullPointerException("获取数据集列表异常");
        }
    }

    /**
     * get view info
     *
     * @param id 数据集is
     * @param request
     * @return
     */
    @OperateLog(operateName = "获取数据集明细",interfacePath = "/views/getView/{id}")
    @ApiOperation(value = "获取数据集明细")
    @PostMapping("getView/{id}")
    public WfResult<ViewWithSourceBaseInfoDto> getView(@PathVariable String id,
                                                           @Valid @RequestBody ViewQueryDto viewQueryDto,
                                                           HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        httpHeaders.add("Content-type", "application/json");
        String url = ereportProperties.getAscUrl()+"/views/getView/"+id;
        Map<String,Object> urlVariables = new HashMap<>();
        urlVariables.put("id",id);
        HttpEntity httpEntity = new HttpEntity(viewQueryDto, httpHeaders);
        ResponseEntity<ApiResult> responseEntity = restTemplate.postForEntity(url, httpEntity, ApiResult.class);
        if(responseEntity.getBody()!=null){
            ApiResult apiResult = responseEntity.getBody();
            ViewWithSourceBaseInfoDto viewWithSourceBaseInfoDto = JSON.parseObject(JSON.toJSONString(apiResult.getData()), ViewWithSourceBaseInfoDto.class);
            return new WfResult(viewWithSourceBaseInfoDto);
        }else{
            log.info("获取数据集明细异常");
            throw new NullPointerException("获取数据集明细异常");
        }
    }

    /**
     * 新建view
     *
     * @param view
     * @param request
     * @return
     */
    @OperateLog(operateName = "创建数据集",interfacePath = "/views/createView")
    @ApiOperation(value = "创建数据集")
    @PostMapping(value = "/createView", consumes = MediaType.APPLICATION_JSON_VALUE)
    public  WfResult<ViewWithSourceBaseInfoDto> createView(@Valid @RequestBody ViewCreateDto view,
                                                               HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/createView";
        HttpEntity httpEntity = new HttpEntity(view, httpHeaders);
        ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(responseEntity.getBody()!=null){
            ViewWithSourceBaseInfoDto viewWithSourceBaseInfoDto = JSON.parseObject(JSON.toJSONString(responseEntity.getBody().getData()), ViewWithSourceBaseInfoDto.class);
            return new WfResult(viewWithSourceBaseInfoDto);
        }else{
            log.info("创建数据集异常");
            throw new NullPointerException("创建数据集异常");
        }
    }

    /**
     * 修改View
     *
     * @param viewUpdate
     * @param request
     * @return
     */
    @OperateLog(operateName = "更新数据集",interfacePath = "/views/updateView")
    @ApiOperation(value = "更新数据集")
    @PostMapping(value = "/updateView", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<?> updateView(@Valid @RequestBody ViewUpdateDto viewUpdate,
                                      HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/updateView";
        HttpEntity httpEntity = new HttpEntity(viewUpdate, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("更新数据集异常");
            throw new NullPointerException("更新数据集异常");
        }
    }

    /**
     * 删除View
     *
     * @param deleteDto
     * @param request
     * @return
     */
    @OperateLog(operateName = "删除数据集",interfacePath = "/views/deleteView")
    @ApiOperation(value = "删除数据集")
    @PostMapping("/deleteView")
    public WfResult<?> deleteView(@Valid @RequestBody ViewDeleteDto deleteDto,
                                      HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/deleteView";
        HttpEntity httpEntity = new HttpEntity(deleteDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody());
        }else{
            log.info("删除数据集异常");
            throw new NullPointerException("删除数据集异常");
        }
    }

    /**
     * 批量删除View
     *
     * @param deleteDto id主键数组
     * @param request
     * @return
     */
    @OperateLog(operateName = "批量删除数据集",interfacePath = "/views/deleteMulView")
    @ApiOperation(value = "批量删除数据集")
    @PostMapping("/deleteMulView")
    public WfResult<?> deleteMulView(@Valid @RequestBody ViewDeleteDto deleteDto,
                                         HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/deleteMulView";
        HttpEntity httpEntity = new HttpEntity(deleteDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody());
        }else{
            log.info("批量删除数据集异常");
            throw new NullPointerException("批量删除数据集异常");
        }
    }

    /**
     * 执行sql
     *
     * @param executeSql
     * @param request
     * @return
     */
    @OperateLog(operateName = "执行sql",interfacePath = "/views/execute")
    @ApiOperation(value = "执行sql")
    @PostMapping(value = "/execute", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<PaginateWithQueryColumns> executeSql(@Valid @RequestBody ViewExecuteSql executeSql,
                                                             HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/views/execute";
            HttpEntity httpEntity = new HttpEntity(executeSql, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                PaginateWithQueryColumns viewWithSourceBaseInfoDto = JSON.parseObject(JSON.toJSONString(apiResult.getBody().getData()), PaginateWithQueryColumns.class);
                return new WfResult(viewWithSourceBaseInfoDto);
            }else{
                log.info("执行sql异常");
                throw new NullPointerException("执行sql异常");
            }
        }catch (Exception e){
            log.error("执行sql失败{}",e.getMessage(),e);
            return new WfResult<>("500","执行sql失败！",null);
        }
    }

    /**
     * 执行sql
     *
     * @param executeSql
     * @param request
     * @return
     */
    @OperateLog(operateName = "获取选择的系统表数据",interfacePath = "/views/getDatabaseData")
    @ApiOperation(value = "获取选择的系统表数据")
    @PostMapping(value = "/getDatabaseData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<List<PaginateWithQueryColumns>> getDatabaseData(@Valid @RequestBody BaseDataExecuteSql executeSql,
                                                                        HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/getDatabaseData";
        HttpEntity httpEntity = new HttpEntity(executeSql, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody());
        }else{
            log.info("获取选择的系统表数据异常");
            throw new NullPointerException("获取选择的系统表数据异常");
        }
    }

    /**
     * 获取当前view对应的源数据
     *
     * @param id
     * @param executeParam
     * @param request
     * @return
     */
    @OperateLog(operateName = "获取数据集数据",interfacePath = "/views//{id}/getdata")
    @ApiOperation(value = "获取数据集数据")
    @PostMapping(value = "/{id}/getdata", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Paginate<Map<String, Object>>> getData(@PathVariable String id,
                                                               @RequestBody(required = false) ViewExecuteParam executeParam,
                                                               HttpServletRequest request) throws SQLException {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/"+id+"/getdata";
        HttpEntity httpEntity = new HttpEntity(executeParam, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody());
        }else{
            log.info("获取数据集数据异常");
            throw new NullPointerException("获取数据集数据异常");
        }
    }

    /**
     * 获取当前view对应的字段映射
     *
     * @param dataColumnsDto
     * @return
     */
    @OperateLog(operateName = "获取数据集字段映射",interfacePath = "/views/getDataColumns")
    @ApiOperation(value = "获取数据集字段映射")
    @PostMapping(value = "/getDataColumns", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<List<PaginateWithQueryColumns>> getDataColumns(@Valid @RequestBody DataColumnsDto dataColumnsDto,HttpServletRequest request) throws SQLException {
        try{
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/views/getDataColumns";
            HttpEntity httpEntity = new HttpEntity(dataColumnsDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取数据集字段映射异常");
                throw new NullPointerException("获取数据集字段映射异常");
            }
        }catch(Exception e) {
            log.error("获取数据集字段异常:{}",e.getMessage(), e);
            return new WfResult<>("500", "获取数据集字段异常", null);
        }
    }

    @OperateLog(operateName = "get distinct value",interfacePath = "/views/{id}/getdistinctvalue")
    @ApiOperation(value = "get distinct value")
    @PostMapping(value = "/{id}/getdistinctvalue", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult< List<Map<String, Object>>>  getDistinctValue(@PathVariable String id,
                                                                      @Valid @RequestBody DistinctParam param,
                                                                      HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/"+id+"/getdistinctvalue";
        HttpEntity httpEntity = new HttpEntity(param, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("获取数据异常");
            throw new NullPointerException("获取数据异常");
        }
    }

    /**
     * execl文件上传
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/analysisExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WfResult analysisExcel(MultipartFile  file,HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/analysisExcel";
        HttpEntity httpEntity = new HttpEntity(file, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("execl文件上传异常");
            throw new NullPointerException("execl文件上传异常");
        }
    }

    /**
     * 新建excelview
     *
     * @param view
     * @param
     * @return
     */
    @PostMapping(value = "/createExcelView", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> createExcelView(@Valid @RequestBody ViewExcelDto view,HttpServletRequest request) {
        WfResult rr = new WfResult();
        try{
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/views/createExcelView";
            HttpEntity httpEntity = new HttpEntity(view, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("新建excelview异常");
                throw new NullPointerException("新建excelview异常");
            }
        }catch(Exception e) {
            log.error("创建Excel数据集异常:{}",e.getMessage(), e);
            rr.error("创建Excel数据集失败！");
        }
        return rr;
    }

    @PostMapping(value = "/getExcelData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<List> getExcelData(@RequestBody ViewQueryDto viewQueryDto,HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/getExcelData";
        HttpEntity httpEntity = new HttpEntity(viewQueryDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("获取excel数据异常");
            throw new NullPointerException("获取excel数据异常");
        }
    }

    @PostMapping("/getExcelView/{id}")
    public WfResult<ViewDto> getExcelView(@PathVariable String id,
                                              @Valid @RequestBody ViewQueryDto viewQueryDto, HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/getExcelView/"+id;
        HttpEntity httpEntity = new HttpEntity(viewQueryDto, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("获取excel数据异常");
            throw new NullPointerException("获取excel数据异常");
        }
    }

    @PostMapping(value = "/updateExeclAuth", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> updateExeclAuth(@Valid @RequestBody ViewExcelDto view, HttpServletRequest request) {
        WfResult rr = new WfResult();
        try{
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/views/updateExeclAuth";
            HttpEntity httpEntity = new HttpEntity(view, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取excel数据异常");
                throw new NullPointerException("获取excel数据异常");
            }
        }catch(Exception e) {
            log.error("更新Excel数据集异常:{}",e.getMessage(), e);
            rr.success("更新Excel数据集失败！");
        }
        return rr;
    }

    /**
     * 新建apiview
     *
     * @param view
     * @param
     * @return
     */
    @PostMapping(value = "/createApiView", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> createApiView(@Valid @RequestBody ViewApiDto view, HttpServletRequest request) {
        WfResult rr = new WfResult();
        try{
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/views/createApiView";
            HttpEntity httpEntity = new HttpEntity(view, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("创建API数据集异常。");
                throw new NullPointerException("创建API数据集异常。");
            }
        }catch(Exception e) {
            log.error("创建API数据集异常:{}",e.getMessage(), e);
            rr.error("创建API数据集失败！");
        }
        return rr;
    }

    @PostMapping("/getApiView/{id}")
    public WfResult<ViewDto> getApiView(@PathVariable String id,
                                            @Valid @RequestBody ViewApiDto apiView, HttpServletRequest request) {
        String token = request.getHeader("Access-token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-token", token);
        String url = ereportProperties.getAscUrl()+"/views/getApiView/"+id;
        HttpEntity httpEntity = new HttpEntity(apiView, httpHeaders);
        ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        if(apiResult.getBody()!=null){
            return new WfResult(apiResult.getBody().getData());
        }else{
            log.info("获取API数据集异常。");
            throw new NullPointerException("获取API数据集异常。");
        }
    }

    @PostMapping(value = "/updateApiView", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<String> updateApiView(@Valid @RequestBody ViewApiDto view, HttpServletRequest request) {
        WfResult rr = new WfResult();
        try{
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/views/updateApiView";
            HttpEntity httpEntity = new HttpEntity(view, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("更新API数据集异常。");
                throw new NullPointerException("更新API数据集异常。");
            }
        }catch(Exception e) {
            log.error("更新API数据集异常:{}",e.getMessage(), e);
            rr.success("更新API数据集失败！");
        }
        return rr;
    }

}
