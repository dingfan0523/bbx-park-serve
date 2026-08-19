package com.cgnpc.ereport.controller;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.report.chart.common.annotation.OperateLog;
import com.cgnpc.report.chart.common.domain.bigscreen.Bigscreen;
import com.cgnpc.report.chart.common.dto.bigscreen.*;
import com.cgnpc.report.chart.common.exception.BusinessException;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author P637785
 * @description 大屏前端控制器
 * @date 2024/03/28
 */
@CrossOrigin("*")
@Api(value = "/bigscreen", tags = "大屏控制器")
@ApiResponses(@ApiResponse(code = 404, message = "bigscreen not found"))
@RestController
@RequestMapping("/bigscreen")
@Slf4j
public class CudBigscreenController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    @OperateLog(operateName = "创建大屏",interfacePath = "/bigscreen/createBigscreen")
    @ApiOperation(value = "创建大屏")
    @PostMapping(value = "/createBigscreen", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Bigscreen> createBigscreen(@Valid @RequestBody BigscreenCreateDto bigscreenCreateDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/createBigscreen";
            HttpEntity httpEntity = new HttpEntity(bigscreenCreateDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("创建大屏异常");
                throw new NullPointerException("创建大屏异常");
            }
        } catch (Exception e) {
            log.error("创建大屏失败：{}", e.getMessage(), e);
            String msg = "创建大屏失败！";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            }
            return new WfResult<>("500", msg, null);
        }
    }

    /**
     * 分页查询
     *
     * @param queryBigscreenPageDto
     * @return
     */
    @OperateLog(operateName = "分页查询",interfacePath = "/bigscreen/pageInfo")
    @ApiOperation(value = "分页查询")
    @PostMapping("/pageInfo")
    public WfResult<PageInfo<Bigscreen>> pageInfo(@Valid @RequestBody QueryBigscreenPageDto queryBigscreenPageDto,HttpServletRequest request) {
        log.info("大屏分页查询：{}", JSON.toJSONString(queryBigscreenPageDto));
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/pageInfo";
            HttpEntity httpEntity = new HttpEntity(queryBigscreenPageDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("大屏分页查询异常");
                throw new NullPointerException("屏分页查询异常");
            }
        }catch (Exception e){
            log.error("大屏查询失败：{}",e.getMessage(),e);
            return new WfResult<>("500","大屏查询失败！",null);
        }
    }

    @OperateLog(operateName = "按主键查询详细信息",interfacePath = "/bigscreen/getDetail")
    @ApiOperation(value = "按主键查询详细信息")
    @PostMapping(value = "/getDetail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<BigscreenObjectDto> getDetail(@Valid @RequestBody BigscreenQueryDto bigscreenQueryDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/getDetail";
            HttpEntity httpEntity = new HttpEntity(bigscreenQueryDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("按主键查询详细信息查询异常");
                throw new NullPointerException("按主键查询详细信息查询异常");
            }
        }catch (Exception e){
            log.error("查询详细信息失败：{}",e.getMessage(),e);
            return new WfResult<>("500","查询详细信息失败！",null);
        }
    }

    /**
     * 删除大屏
     *
     * @param bigscreenDeleteDto
     * @return
     */
    @OperateLog(operateName = "删除大屏",interfacePath = "/bigscreen/deleteBigscreen")
    @ApiOperation(value = "删除大屏")
    @PostMapping(value = "/deleteBigscreen", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WfResult<Boolean> deleteBigscreen(@Valid @RequestBody BigscreenDeleteDto bigscreenDeleteDto,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/deleteBigscreen";
            HttpEntity httpEntity = new HttpEntity(bigscreenDeleteDto, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("删除大屏异常");
                throw new NullPointerException("删除大屏异常");
            }
        }catch (Exception e){
            log.error("删除大屏失败：{}",e.getMessage(),e);
            return new WfResult<>("500","删除大屏失败！",null);
        }
    }

    @OperateLog(operateName = "获取数据字典",interfacePath = "/bigscreen/fetchDictItems")
    @ApiOperation(value = "获取数据字典")
    @ApiImplicitParam(paramType = "path", name = "dictCode", value = "字典码", required = true, dataType = "string")
    @GetMapping(value = "/fetchDictItems/{dictCode}")
    public WfResult<List<BigscreenDictItemDto>> fetchDictItems(@PathVariable("dictCode") String dictCode,HttpServletRequest request) {
        log.error("获取数据字典集合fetchDictItems：{}", dictCode);
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/fetchDictItems/"+dictCode;
            HttpEntity httpEntity = new HttpEntity(dictCode, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取数据字典异常");
                throw new NullPointerException("获取数据字典异常");
            }
        }catch (Exception e){
            log.error("获取数据字典集合失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取数据字典集合失败！",null);
        }
    }

    @OperateLog(operateName = "获取图表维度字段的操作列表",interfacePath = "/bigscreen/getOperationType")
    @ApiOperation(value = "获取图表维度字段的操作列表")
    @ApiImplicitParam(paramType = "path", name = "fieldType", value = "字段类型", required = true, dataType = "string")
    @GetMapping(value = "/getOperationType/{fieldType}")
    public WfResult<List<JSONObject>> getOperationType(@PathVariable final String fieldType,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/getOperationType/"+fieldType;
            HttpEntity httpEntity = new HttpEntity(fieldType, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取图表维度字段的操作列表异常");
                throw new NullPointerException("获取图表维度字段的操作列表异常");
            }
        }catch (Exception e){
            log.error("获取数据字典集合失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取数据字典集合失败！",null);
        }
    }

    @ApiOperation(value = "查询地区")
    @GetMapping("/areaEntitys/{pcode}")
    public WfResult<List<AreaEntity>> areaEntitys (@PathVariable String pcode,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/areaEntitys/"+pcode;
            HttpEntity httpEntity = new HttpEntity(pcode, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("查询地区异常");
                throw new NullPointerException("查询地区异常");
            }
        }catch (Exception e){
            log.error("查询地区失败：{}",e.getMessage(),e);
            return new WfResult<>("500","查询地区失败！",null);
        }
    }

    @ApiOperation(value = "获取函数运算列表")
    @ApiImplicitParam(paramType = "path", name = "fieldType", value = "字段类型", required = true, dataType = "string")
    @GetMapping(value = "/getFunctionalOperator/{fieldType}")
    public WfResult<List<JSONObject>> getFunctionalOperator(@PathVariable final String fieldType,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/getFunctionalOperator/"+fieldType;
            HttpEntity httpEntity = new HttpEntity(fieldType, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取函数运算列表异常");
                throw new NullPointerException("获取函数运算列表异常");
            }
        }catch (Exception e){
            log.error("获取函数运算列表失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取函数运算列表失败！",null);
        }
    }

    @ApiOperation(value = "查看大屏-获取单个图表数据")
    @ApiImplicitParam(name = "chartParam", value = "图表", required = true, dataType = "ChartParam")
    @PostMapping("/getData")
    public WfResult<List<JSONObject>> getData(@RequestBody ChartParam chartParam, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/getData";
            HttpEntity httpEntity = new HttpEntity(chartParam, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取单个图表数据异常");
                throw new NullPointerException("获取单个图表数据异常");
            }
        }catch (Exception e){
            log.error("获取单个图表数据失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取单个图表数据失败！",null);
        }
    }

    @ApiOperation(value = "查看大屏-获取单个图表分页数据")
    @ApiImplicitParam(name = "chartParam", value = "图表", required = true, dataType = "ChartParam")
    @PostMapping("/getPageData")
    public WfResult<IPage<JSONObject>> getPageData(@RequestBody ChartParam chartParam, HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/getPageData";
            HttpEntity httpEntity = new HttpEntity(chartParam, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("获取单个图表分页数据异常");
                throw new NullPointerException("获取单个图表分页数据异常");
            }
        }catch (Exception e){
            log.error("获取单个图表分页数据失败：{}",e.getMessage(),e);
            return new WfResult<>("500","获取单个图表分页数据失败！",null);
        }
    }

    @ApiOperation(value = "上传图片")
    @PostMapping("/uploadImg")
    public WfResult<String> uploadImg(@RequestParam("file") MultipartFile file, @RequestParam("dataKey") Integer dataKey,
                                          @RequestParam("tenantId") String tenantId,HttpServletRequest request) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/uploadImg";
            HttpEntity httpEntity = new HttpEntity(file, httpHeaders);
            ResponseEntity<ApiResult> apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
            if(apiResult.getBody()!=null){
                return new WfResult(apiResult.getBody().getData());
            }else{
                log.info("上传图片异常");
                throw new NullPointerException("上传图片异常");
            }
        }catch (Exception e){
            log.error("上传图片失败：{}",e.getMessage(),e);
            return new WfResult<>("500","上传图片失败！",null);
        }

    }

    @ApiOperation(value = "获取图片")
    @ApiImplicitParam(paramType = "path", name = "id", value = "图片id", required = true, dataType = "string")
    @GetMapping(value = "/getImg/{id}")
    public void getImg(HttpServletResponse response,HttpServletRequest request, @PathVariable String id) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/getImg/"+id;
            HttpEntity httpEntity = new HttpEntity(id, httpHeaders);
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        }catch (Exception e){
            log.error("上传图片失败：{}",e.getMessage(),e);
        }
    }

    @ApiOperation(value = "导出明细表")
    @ApiImplicitParam(name = "chartParam", value = "图表", required = true, dataType = "ChartParam")
    @PostMapping(value = "/exportData")
    public void exportData(@RequestBody ChartParam chartParam, HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader("Access-token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Access-token", token);
            String url = ereportProperties.getAscUrl()+"/bigscreen/exportData";
            HttpEntity httpEntity = new HttpEntity(chartParam, httpHeaders);
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class);
        }catch (Exception e){
            log.error("导出明细表失败：{}",e.getMessage(),e);
        }
    }

}
