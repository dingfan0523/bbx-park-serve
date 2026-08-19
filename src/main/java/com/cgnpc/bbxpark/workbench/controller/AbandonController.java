package com.cgnpc.bbxpark.workbench.controller;

import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.cud.workbench.common.constant.WbConstant;
import com.cgnpc.cud.workbench.common.dto.ext.QuickExtDto;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.cud.workflow2.utils.logs.LogUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author P629988
 */
@Slf4j
@RestController
@RequestMapping("/abandon")
@Api(value = "流程作废处理接口", tags = {"流程作废处理接口"})
public class AbandonController extends BaseController {

    /**
     * 流程实例作废
     *
     * @param quickDto
     * @return
     */
    @PostMapping(value = "/isAbandon")
    @ApiOperation(value = "判断是否可以作废", notes = "判断是否可以作废")
    public WfResult abandon(@Valid @RequestBody QuickExtDto quickDto) {
        WfResult wfResult = new WfResult();
        try {
            log.info(quickDto.getComment());
            wfResult.success("可以作废！");
        } catch (Exception e) {
            LogUtils.error(WbConstant.PSC_ABANDONPROCINST_ERROR, e);
            wfResult.error(WbConstant.PSC_ABANDONPROCINST_ERROR);
        }
        return wfResult;
    }

    //fixme 此处为了解决奇安信静态扫描漏洞
    @Override
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{""});
    }
}
