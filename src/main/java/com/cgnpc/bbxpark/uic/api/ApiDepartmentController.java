package com.cgnpc.bbxpark.uic.api;

import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.uic.api.dto.param.DepartmentSearchParam;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hrcenter")
@Api(tags = "用户")
public class ApiDepartmentController {
    @Autowired
    private IDepartmentApiService departmentApiService;

    @ApiOperation(value = "获取部门列表")
    @PostMapping(value = "/getOrgTreeForOrgWidget")
    @RequiredToken
    public CudResult<List<OrgDepartmentNode>> getOrgTreeForOrgWidget(@RequestBody DepartmentSearchParam param) {
        return CudResult.success(departmentApiService.getOrgTreeForOrgWidget(param.getOrgId()));
    }

    @ApiOperation(value = "获取苍南二级部门列表")
    @PostMapping(value = "/getSecondOrgList")
    @RequiredToken
    public CudResult<List<OrgDepartmentNode>> getSecondOrgList() {
        return CudResult.success(departmentApiService.getSecondOrgList());
    }
}
