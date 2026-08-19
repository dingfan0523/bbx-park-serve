package com.cgnpc.bbxpark.space.api;


import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberListParam;
import com.cgnpc.bbxpark.space.service.ITenantMemberService;
import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户成员
 */
@RestController
@RequestMapping("/api/tenant/member")
@Api(tags = "BBX-移动端-租户成员API")
public class ApiTenantMemberController extends BaseController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ApiTenantMemberController.class);

    @Autowired
    private ITenantMemberService tenantMemberService;

    @ApiOperation(value = "获取租户成员列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<TenantMemberDomain>> list(@RequestBody TenantMemberListParam param) {
        return CudResult.success(tenantMemberService.list(param));
    }

    /**********************************
     * 用途说明:
     * 参数说明: @param binder
     * 返回值说明: @return void
     * 异常说明: @throws
     * 作者姓名: @author
     * 创建时间: @date 2021/11/26 14:17
     ***********************************/
    @Override
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{""});
    }
}
