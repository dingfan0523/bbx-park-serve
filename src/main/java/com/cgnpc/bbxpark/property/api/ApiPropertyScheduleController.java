
package com.cgnpc.bbxpark.property.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleListModel;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleUserModel;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleListParam;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物业排班服务控制类
 */
@RestController
@RequestMapping("/api/property/schedule")
@Api(tags = "物业管理-PC端-物业分组排班")
@Slf4j
public class ApiPropertyScheduleController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ApiPropertyScheduleController.class);

    /**
     * 物业排班服务接口.
     */
    @Autowired
    private IPropertyScheduleService propertyScheduleService;

    /**
     * 获取物业排班列表
     */
    @ApiOperation(value = "获取物业排班列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<PropertyScheduleListModel>> list(@RequestBody PropertyScheduleListParam param) {
        return CudResult.success(propertyScheduleService.list(param));
    }

    /**
     * 根据id获取物业排班分组下的人员列表.
     */
    @ApiOperation(value = "根据id获取物业排班分组下的人员列表")
    @GetMapping(value = "/findUserList")
    @RequiredToken
    public CudResult<List<PropertyScheduleUserModel>> findUserList(@RequestParam Long id) {
        return CudResult.success(propertyScheduleService.findUserList(id));
    }
}
