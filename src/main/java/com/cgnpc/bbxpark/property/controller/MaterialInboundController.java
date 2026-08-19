package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.MaterialInboundModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialInboundListParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialInboundPageParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialInboundParam;
import com.cgnpc.bbxpark.property.service.IMaterialInboundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Description 材料入库服务控制类
 * @author huangyongtao
 * @date 2025/9/23 11:38
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/material/inbound")
@Api(tags = "材料入库")
public class MaterialInboundController {

    /**
     * 材料入库服务接口.
     */
    @Autowired
    private IMaterialInboundService materialInboundService;

    /**
     * 获取材料入库列表(分页).
     */
    @ApiOperation(value = "获取材料入库列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MaterialInboundModel>> page(@RequestBody MaterialInboundPageParam param) {
        return CudResult.success(materialInboundService.page(param));
    }

    /**
     * 获取材料入库列表.
     */
    @ApiOperation(value = "获取材料入库列表")
    @PostMapping(value = "/list")
    public CudResult<List<MaterialInboundModel>> list(@RequestBody MaterialInboundListParam param) {
        return CudResult.success(materialInboundService.list(param));
    }


    /**
     * 批量新增材料入库.
     */
    @ApiOperation(value = "批量新增材料入库")
    @PostMapping(value = "/add/batch")
    public CudResult<Boolean> addBatch(@Validated @RequestBody List<MaterialInboundParam> params) {
        return CudResult.success(materialInboundService.addBatch(params));
    }

    /**
     * 获取材料入库单号信息.
     */
    @ApiOperation(value = "获取材料入库单号信息")
    @PostMapping(value = "/getInboundNo")
    public CudResult<MaterialInboundModel> getInboundNo() {
        return CudResult.success(materialInboundService.getInboundNo());
    }
}
