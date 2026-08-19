package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.ioc.dto.model.FloorDeviceCountModel;
import com.cgnpc.bbxpark.ioc.dto.model.ScreenMessageModel;
import com.cgnpc.bbxpark.ioc.dto.model.StrategicMetricsModel;
import com.cgnpc.bbxpark.ioc.dto.param.MessagePageParam;
import com.cgnpc.bbxpark.ioc.service.IIocCommonService;
import com.cgnpc.bbxpark.ioc.service.IScreenSpaceService;
import com.cgnpc.bbxpark.message.dto.req.MessageUserPageParam;
import com.cgnpc.bbxpark.message.dto.req.UnreadParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageUserModel;
import com.cgnpc.bbxpark.message.dto.resp.UnreadModel;
import com.cgnpc.bbxpark.message.service.IMessageUserService;
import com.cgnpc.bbxpark.settings.dto.model.DictItemModel;
import com.cgnpc.bbxpark.space.dto.model.SpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceBasicInfoListParam;
import com.cgnpc.bbxpark.space.service.ISpaceBasicInfoService;
import com.cgnpc.bbxpark.settings.service.impl.DictServiceImpl;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberListParam;
import com.cgnpc.bbxpark.space.service.ITenantMemberService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 大屏材料统计
 */
@RestController
@RequestMapping("/api/dtwin/common/")
@Api(tags= "大屏-总览")
public class ApiIocController {
    @Autowired
    private IMessageUserService messageUserService;
    @Autowired
    private ISpaceBasicInfoService spaceBasicInfoService;
    @Autowired
    private DictServiceImpl dictService;
    @Autowired
    private ITenantMemberService tenantMemberService;
    @Autowired
    private IIocCommonService iocCommonService;

    @Autowired
    private IScreenSpaceService spaceService;

    @GetMapping("/getStrategicMetrics")
    @ApiOperation("战略指标总览")
    public CudResult<StrategicMetricsModel> getStrategicMetrics(
            @ApiParam(value = "空间模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(iocCommonService.getStrategicMetrics(sslcCode));
    }


    @ApiOperation(value = "通过字典编码获取字典项列表")
    @PostMapping(value = "/findItemsByDictType")
    @RequiredToken
    public CudResult<List<DictItemModel>> findItemsByDictType(String dictType) {
         return CudResult.success(dictService.findItemsByDictType(dictType));
    }

    @GetMapping("/getTenantId")
    @ApiOperation("获取租户")
    public CudResult<Long> getTenantId(@ApiParam(value = "员工编号") @RequestParam  String userNo) {
        TenantMemberListParam param = new TenantMemberListParam();
        param.setUserId(userNo);
        List<Long> tenantIds = tenantMemberService.list(param).stream().map(TenantMemberDomain::getTenantId).collect(Collectors.toList());
        Long tenantId = CollectionUtils.isEmpty(tenantIds) ? null : tenantIds.get(0);
        return CudResult.success(tenantId);
    }

    @ApiOperation(value = "获取用户消息列表(分页)")
    @PostMapping(value = "/message/page")
    @RequiredToken
    public CudResult<IPage<ScreenMessageModel>> page(@RequestBody MessagePageParam param) {
        IPage<MessageUserModel> page = messageUserService.pageZy(BeanUtils.convertTo(param, MessageUserPageParam::new));
        List<ScreenMessageModel> list = page.getRecords().stream().map(m -> {
            ScreenMessageModel message = new ScreenMessageModel();
            message.setId(m.getId());
            message.setType(m.getType() + "");
            message.setTitle(m.getTitle());
            message.setContent(m.getContent());
            message.setDate(m.getCreateTime());
            message.setReadStatus(m.getReadStatus());
            return message;
        }).collect(Collectors.toList());
        return CudResult.success(ConvertUtil.pageConvert(page, list));
    }

    /**
     * 获取空间树形结构列表
     */
    @ApiOperation(value = "获取空间树形结构列表")
    @PostMapping(value = "/space/tree")
    public CudResult<List<SpaceTreeModel>> tree(@RequestBody SpaceBasicInfoListParam param) {
        return CudResult.success(spaceBasicInfoService.tree(param));
    }

    @ApiOperation(value = "获取未读数量")
    @PostMapping(value = "/message/unread")
    @RequiredToken
    public CudResult<List<UnreadModel>> unread() {
        UnreadParam param = new UnreadParam();
        param.setUserId(WebFrameworkUtils.getHeaderUserId());
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(messageUserService.appUnread(param));
    }

    @ApiOperation(value = "批量已读用户消息")
    @PostMapping(value = "/message/batchRead")
    @RequiredToken
    public CudResult<Boolean> batchRead(@RequestBody List<Long> ids) {
        return CudResult.success(messageUserService.batchRead(ids));
    }

    @GetMapping("/getFloorDeviceCount")
    @ApiOperation("楼层设备数量")
    public CudResult<FloorDeviceCountModel> getFloorDeviceCount(@ApiParam(value = "空间模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(spaceService.getFloorDeviceCount(sslcCode));
    }
}
