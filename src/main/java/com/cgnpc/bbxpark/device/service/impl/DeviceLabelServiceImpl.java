
package com.cgnpc.bbxpark.device.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.DeviceLabel;
import com.cgnpc.bbxpark.device.domain.DeviceLabelRel;
import com.cgnpc.bbxpark.device.dto.model.DeviceLabelModel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceLabelListParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceLabelPageParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceLabelParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.mapper.DeviceLabelRepository;
import com.cgnpc.bbxpark.device.service.IDeviceLabelRelService;
import com.cgnpc.bbxpark.device.service.IDeviceLabelService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备标签服务实现
 */
@Service
public class DeviceLabelServiceImpl extends BaseServiceImpl<DeviceLabelRepository, DeviceLabel> implements IDeviceLabelService {


    @Autowired
    private IDeviceLabelRelService deviceLabelRelService;

    @Autowired
    private DeviceLabelRepository deviceLabelRepository;

    @Autowired
    private IParkSpaceService parkSpaceService;

    @Autowired
    private IUserApiService userApiService;

    @Override
    public IPage<DeviceLabelModel> pageResult(DeviceLabelPageParam param) {
        // 分页参数
        IPage<DeviceLabel> page = new Page<>(param.getCurrent(), param.getSize());
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        LambdaQueryWrapper<DeviceLabel> wrapper = Wrappers.<DeviceLabel>lambdaQuery()
                .eq(ObjectUtil.isNotNull(tenantId), DeviceLabel::getTenantId, tenantId)
                .like(StrUtil.isNotEmpty(param.getLabelName()), DeviceLabel::getLabelName, param.getLabelName())
                .like(StrUtil.isNotEmpty(param.getLabelCode()), DeviceLabel::getLabelCode, param.getLabelCode())
                .orderByDesc(DeviceLabel::getCreateTime);
        // 分页查询
        IPage<DeviceLabel> iPage = this.page(page, wrapper);
        // Model 转换
        List<DeviceLabelModel> compartmentModels = BeanUtils.convertListTo(iPage.getRecords(), DeviceLabelModel::new);
        List<Long> ids = compartmentModels.stream().map(DeviceLabelModel::getId).collect(Collectors.toList());
        //统计设备数量
        Map<Long, List<DeviceLabelRel>> deviceLabelMap = new HashMap<>(16);
        if (CollUtil.isNotEmpty(ids)) {
            deviceLabelMap = deviceLabelRelService.list(Wrappers.<DeviceLabelRel>lambdaQuery()
                    .in(DeviceLabelRel::getLabelId, ids)).stream().collect(Collectors.groupingBy(DeviceLabelRel::getLabelId));
        }
        Map<Long, List<DeviceLabelRel>> finalDeviceLabelMap = deviceLabelMap;
        compartmentModels.forEach(f -> f.setLabelDeviceNum(ObjectUtil.isNotEmpty(finalDeviceLabelMap) && ObjectUtil.isNotEmpty(finalDeviceLabelMap.get(f.getId())) ? finalDeviceLabelMap.get(f.getId()).size() : 0));
        IPage<DeviceLabelModel> page1 = new Page<>(param.getCurrent(), param.getSize());
        page1.setTotal(page.getTotal());
        page1.setRecords(compartmentModels);
        return page1;

    }

    /**
     * @Param: param
     * @Author lhy
     * @Date 2024/8/13
     * @Description: 新增标签
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean add(DeviceLabelParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        //校验数据
        validate(param);
        DeviceLabel deviceLabel = BeanUtils.convertTo(param, DeviceLabel::new);
        deviceLabel.setId(null);
        deviceLabel.setStaffId(userApiService.getCurrentStaffNo());
        deviceLabel.setCreateBy(userApiService.getCurrentStaffName());
        return this.save(deviceLabel);
    }

    /**
     * @Param: param
     * @Author lhy
     * @Date 2024/8/13
     * @Description: 编辑标签
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean edit(DeviceLabelParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        //校验数据
        validate(param);
        DeviceLabel deviceLabel = BeanUtils.convertTo(param, DeviceLabel::new);
        this.updateById(deviceLabel);
        return Boolean.TRUE;
    }

    /**
     * @Param:
     * @Author lhy
     * @Date 2024/8/13
     * @Description: 校验新增数据
     */
    public void validate(DeviceLabelParam param) {
        int num = this.count(new LambdaQueryWrapper<DeviceLabel>()
                .eq(DeviceLabel::getLabelCode, param.getLabelCode())
                .eq(ObjectUtil.isNotEmpty(param.getTenantId()), DeviceLabel::getTenantId, param.getTenantId())
                .ne(param.getId() != null, DeviceLabel::getId, param.getId()));
        AssertUtils.isTrue(num == 0, "标签编码已存在");
    }

    /**
     * @Param:id
     * @Author lhy
     * @Date 2024/8/13
     * @Description: 删除标签
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean removeId(Long id) {
        //删除关联表
        deviceLabelRelService.remove(new LambdaQueryWrapper<DeviceLabelRel>().eq(DeviceLabelRel::getLabelId, id));
        this.getBaseMapper().deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public DeviceLabelModel get(Long id) {
        DeviceLabel deviceLabel = getById(id);
        AssertUtils.notNull(deviceLabel, SystemResultCode.RESULT_DATA_NONE.message());
        return BeanUtils.convertTo(deviceLabel, DeviceLabelModel::new);
    }


    /**
     * @Param:param
     * @Author lhy
     * @Date 2024/8/13
     * @Description: 新增设备到标签
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addDeviceToLabel(DeviceLabelParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<Long> deviceIds = param.getDeviceIds();
        if (CollUtil.isEmpty(deviceIds)) {
            return Boolean.TRUE;
        }
        //添加标签设备关联表
        List<DeviceLabelRel> compartmentCombos = deviceIds.stream().map(p -> {
            DeviceLabelRel deviceLabelRel = new DeviceLabelRel();
            deviceLabelRel.setId(null);
            deviceLabelRel.setTenantId(tenantId);
            deviceLabelRel.setDeviceId(p);
            deviceLabelRel.setLabelId(param.getId());
            return deviceLabelRel;
        }).collect(Collectors.toList());
        deviceLabelRelService.saveBatch(compartmentCombos);
        return Boolean.TRUE;
    }


    /***
     * @Description 标签设备分页查询
     * @author lhy
     * @date 2024/7/17 11:30
     * @param
     */
    @Override
    public IPage<IocDeviceModel> pageDeviceToLabel(IocDevicePageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        IPage<IocDeviceModel> iPage = new Page<>(param.getCurrent(), param.getSize());
        IPage<IocDeviceModel> page = deviceLabelRepository.pageDeviceToLabel(iPage, param);
        List<IocDeviceModel> records = page.getRecords();

        // 实时查询空间全路径
        List<Long> spaceIdList = records.stream().map(IocDeviceModel::getSpaceId).collect(Collectors.toList());
        Map<Long, ParkSpaceFullModel> spaceFullModelMap = parkSpaceService.findFullSpaceMap(spaceIdList, WebFrameworkUtils.getHeaderTenantId());

        records.forEach(f -> {
            ParkSpaceFullModel fullModel = spaceFullModelMap.get(f.getSpaceId());
            if (fullModel != null) {
                f.setSpaceFullPath(fullModel.getFullPath());
            }
        });
        return  ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), records);
    }


    /**
     * @Param: param
     * @Author lhy
     * @Date 2024/8/13
     * @Description: 移除标签下的设备
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean removeDeviceToLabel(DeviceLabelListParam param) {
        List<Long> deviceIdList = param.getDeviceIdList();
        Long labelId = param.getLabelId();
        return deviceLabelRelService.remove(new LambdaQueryWrapper<DeviceLabelRel>().eq(DeviceLabelRel::getLabelId, labelId)
                .in(DeviceLabelRel::getDeviceId, deviceIdList));
    }


    /***
     * @Description 根据设备id查询设备下的标签信息
     * @author lhy
     * @date 2024/7/17 11:30
     * @param
     */
    @Override
    public List<DeviceLabelModel> list(DeviceLabelListParam param) {
        List<DeviceLabelRel> deviceLabelRels = deviceLabelRelService.list(new LambdaQueryWrapper<DeviceLabelRel>().eq(DeviceLabelRel::getDeviceId, param.getDeviceId()));
        List<Long> labelIds = deviceLabelRels.stream().map(DeviceLabelRel::getLabelId).collect(Collectors.toList());
        if (CollUtil.isEmpty(labelIds)) {
            return CollUtil.newArrayList();
        }
        return BeanUtils.convertListTo(this.list(new LambdaQueryWrapper<DeviceLabel>().in(DeviceLabel::getId, labelIds)), DeviceLabelModel::new);
    }
}
