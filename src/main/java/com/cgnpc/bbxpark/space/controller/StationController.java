
package com.cgnpc.bbxpark.space.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.*;
import com.cgnpc.bbxpark.space.dto.param.DepartmentMemberExParam;
import com.cgnpc.bbxpark.space.dto.param.StationListParam;
import com.cgnpc.bbxpark.space.dto.param.StationPageParam;
import com.cgnpc.bbxpark.space.dto.param.StationParam;
import com.cgnpc.bbxpark.space.service.IStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(Constant.BASE_PATH + "/space/station")
@Api(tags = "智慧空间-空间工位")
@Slf4j
public class StationController {

    /**
     * 空间工位服务接口.
     */
    @Autowired
    private IStationService stationService;

    /**
     * 获取空间工位列表(分页).
     */
    @ApiOperation(value = "获取空间工位列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<StationModel>> page(@RequestBody StationPageParam param) {
        return CudResult.success(stationService.page(param));
    }

    /**
     * 获取空间工位列表.
     */
    @ApiOperation(value = "获取空间工位列表")
    @PostMapping(value = "/list")
    public CudResult<List<StationModel>> list(@RequestBody StationListParam param) {
        return CudResult.success(stationService.list(param));
    }

    /**
     * 分配空间工位.
     */
    @ApiOperation(value = "分配空间工位")
    @PostMapping(value = "/allot")
    public CudResult<Boolean> allot(@Validated @RequestBody StationParam param) {
        return CudResult.success(stationService.allot(param));
    }


    /**
     * 新增空间工位.
     */
    @ApiOperation(value = "分配空间工位校验")
    @PostMapping(value = "/check")
    public CudResult<Boolean> check(@Validated @RequestBody StationParam param) {
        return CudResult.success(stationService.check(param));
    }

    /**
     * 空间工位统计.
     */
    @ApiOperation(value = "空间工位统计")
    @GetMapping(value = "/statistics")
    public CudResult<StationStatisticsModel> statistics(@RequestParam Long spaceId) {
        return CudResult.success(stationService.statistics(spaceId));
    }

    /**
     * 删除空间工位.
     */
    @ApiOperation(value = "删除空间工位")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(stationService.remove(id));
    }

    /**
     * 获取空间工位列表(分页).
     */
    @ApiOperation(value = "获取组织部门树")
    @PostMapping(value = "/department/list/organization/{organizationId}")
    public CudResult<List<OrgDeptExTreeNode>> departmentTree(@PathVariable Long organizationId) {
        return CudResult.success(stationService.listOrgTree(organizationId));
    }

    /**
     * 获取空间工位列表(分页).
     */
    @ApiOperation(value = "获取部门人员列表(分页)")
    @PostMapping(value = "/department/member/page")
    public CudResult<IPage<DepartmentMemberExModel>> memberPage(@RequestBody DepartmentMemberExParam param) {
        return CudResult.success(stationService.departmentMemberPage(param));
    }

    /***
     * @Description 获取用户办公地点
     * @author huangyongtao
     * @date 2025/9/25 15:22
     */
    @ApiOperation(value = "获取用户办公地点")
    @GetMapping(value = "/use")
    public CudResult<ParkSpaceFullModel> getUserStationSpace() {
        return CudResult.success(stationService.getUserStationSpace());
    }
}
