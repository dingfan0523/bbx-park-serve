package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.MaterialModel;
import com.cgnpc.bbxpark.property.dto.model.MaterialRecordModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialPageParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialRecordPageParam;
import com.cgnpc.bbxpark.property.service.IMaterialRecordService;
import com.cgnpc.bbxpark.property.service.IMaterialService;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.service.IMaterialScreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 大屏材料统计
 */
@RestController
@RequestMapping("/api/dtwin/material")
@Api(tags= "大屏-材料统计")
public class ApiMaterialScreenController {
    @Autowired
    private IMaterialScreenService materialScreenService;

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private IMaterialRecordService materialRecordService;

    @GetMapping(value = "/getInventoryAnalysis")
    @ApiOperation(value = "库存分析")
    public CudResult<MaterialInventoryAnalysisModel> getMaterialInventory(@ApiParam(value = "材料类型") @RequestParam(required = false) Long type) {
        return CudResult.success(materialScreenService.getMaterialInventory(type));
    }

    @GetMapping(value = "/getInventoryHealth")
    @ApiOperation(value = "库存健康度")
    public CudResult<List<MaterialInventoryHealthModel>> getMaterialInventoryHealth() {
        return CudResult.success(materialScreenService.getMaterialInventoryHealth());
    }

    @GetMapping(value = "/getTurnover")
    @ApiOperation(value = "库存周转")
    public CudResult<List<MaterialTurnoverModel>> getMaterialTurnover() {
        return CudResult.success(materialScreenService.getMaterialTurnover());
    }

    @GetMapping(value = "/getWorkUse")
    @ApiOperation(value = "sku关联分析")
    public CudResult<List<MaterialWorkUseModel>> getMaterialWorkUse() {
        return CudResult.success(materialScreenService.getMaterialWorkUse());
    }

    @GetMapping(value = "/getSmartSuggest")
    @ApiOperation(value = "智能分析建议")
    public CudResult<List<MaterialSmartSuggestModel>> getMaterialSmartSuggest() {
        return CudResult.success(materialScreenService.getMaterialSmartSuggest());
    }

    @GetMapping(value = "/getTurnoverRank")
    @ApiOperation(value = "耗材周转排行榜")
    public CudResult<List<MaterialTurnoverRankModel>> getMaterialTurnoverRank(@ApiParam(value = "排行类型（1：高； 2：低）") @RequestParam(required = true) Long type) {
        return CudResult.success(materialScreenService.getMaterialTurnoverRank(type));
    }

    @GetMapping(value = "/getUsageRank")
    @ApiOperation(value = "器材使用排行榜")
    public CudResult<List<MaterialUsageRankModel>> getMaterialUsageRank(@ApiParam(value = "排行类型（1：高； 2：低）") @RequestParam(required = true) Long type) {
        return CudResult.success(materialScreenService.getMaterialUsageRank(type));
    }

    /**
     * 获取材料列表(分页).
     */
    @ApiOperation(value = "获取材料列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<MaterialModel>> materialPage(@RequestBody MaterialPageParam param) {
        return CudResult.success(materialService.page(param));
    }

    /**
     * 获取材料出入库记录列表(分页).
     */
    @ApiOperation(value = "获取材料出入库记录列表(分页)")
    @PostMapping(value = "/record/page")
    public CudResult<IPage<MaterialRecordModel>> recodePage(@RequestBody MaterialRecordPageParam param) {
        return CudResult.success(materialRecordService.page(param));
    }

    @GetMapping(value = "/getSpaceCount")
    @ApiOperation(value = "材料空间统计")
    public CudResult<List<MaterialSpaceCountModel>> getMaterialSpaceCount(@ApiParam(value = "所属楼层物模型编码") @RequestParam(required = true) String sslcCode) {
        return CudResult.success(materialScreenService.getMaterialSpaceCount(sslcCode));
    }

    @GetMapping(value = "/getSpaceView")
    @ApiOperation(value = "材料空间楼层高亮展示列表")
    public CudResult<List<SpaceViewModel>> getMaterialSpaceView(@ApiParam(value = "所属楼层物模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(materialScreenService.getMaterialSpaceView(sslcCode));
    }

}
