
package com.cgnpc.bbxpark.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.dto.model.LoginLogModel;
import com.cgnpc.bbxpark.settings.dto.param.LoginLogPageParam;
import com.cgnpc.bbxpark.settings.service.ILoginLogService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 登入日志
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/log/login")
@Api(tags = "BBX-登录日志")
public class LoginLogController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(LoginLogController.class);

    @Autowired
    private ILoginLogService loginLogService;

    @ApiOperation(value = "获取登录日志信息")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<LoginLogModel> detail(@RequestParam Long id) {
        return CudResult.success(loginLogService.detail(id));
    }

    @ApiOperation(value = "获取登录日志列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<LoginLogModel>> page(@RequestBody LoginLogPageParam param) {
        return CudResult.success(loginLogService.page(param));

    }

    @ApiOperation(value = "删除登录日志")
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(loginLogService.removeBatch(Collections.singletonList(id)));
    }

    @ApiOperation(value = "批量删除登录日志")
    @PostMapping(value = "/remove/batch", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> removeBatch(@RequestBody List<Long> ids) {
        return CudResult.success(loginLogService.removeBatch(ids));
    }

    @ApiOperation(value = "清空登录日志")
    @PostMapping(value = "/clean", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> clean() {
        return CudResult.success(loginLogService.clean());
    }

}
