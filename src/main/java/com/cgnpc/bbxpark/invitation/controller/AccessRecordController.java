
package com.cgnpc.bbxpark.invitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.invitation.dto.model.AccessRecordModel;
import com.cgnpc.bbxpark.invitation.dto.param.AccessRecordPageParam;
import com.cgnpc.bbxpark.invitation.service.IAccessRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping(Constant.BASE_PATH + "/access/record")
@Api(tags = "PC端-通行记录")
@Slf4j
public class AccessRecordController {

    /**
     * 来访记录服务接口.
     */
    @Autowired
    private IAccessRecordService accessRecordService;


    /**
     * 获取访客通行记录列表(分页).
     */
    @ApiOperation(value = "获取访客通行记录列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<AccessRecordModel>> page(@RequestBody AccessRecordPageParam param) {
        param.setPersonType("1");
        return CudResult.success(accessRecordService.page(param));
    }

    /**
     * 获取所有门禁通行记录列表(分页).
     */
    @ApiOperation(value = "获取所有门禁通行记录列表(分页)")
    @PostMapping(value = "/door/page")
    public CudResult<IPage<AccessRecordModel>> doorPage(@RequestBody AccessRecordPageParam param) {
        return CudResult.success(accessRecordService.page(param));
    }

    /**
     * 获取通行记录详情.
     */
    @ApiOperation(value = "获取通行记录详情")
    @GetMapping(value = "/detail/{id}")
    public CudResult<AccessRecordModel> detail(@PathVariable @NotNull(message = "通行记录标识") Long id) {
        return  CudResult.success(accessRecordService.detail(id));
    }

    /***
     *访客通行记录导出
     */
    @ApiOperation(value = "访客通行记录导出")
    @GetMapping(value = "/export")
    public void easyExport(HttpServletResponse response, @ModelAttribute AccessRecordPageParam param) {
        param.setPersonType("1");
        accessRecordService.export(response, param);
    }

    /***
     *门禁通行记录导出
     */
    @ApiOperation(value = "门禁通行记录导出")
    @GetMapping(value = "/door/export")
    public void doorExport(HttpServletResponse response, @ModelAttribute AccessRecordPageParam param) {
        accessRecordService.doorExport(response, param);
    }
}
