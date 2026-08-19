package com.cgnpc.bbxpark.energy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.service.impl.IocDeviceServiceImpl;
import com.cgnpc.bbxpark.energy.domain.BranchDevice;
import com.cgnpc.bbxpark.energy.dto.model.BranchDeviceModel;
import com.cgnpc.bbxpark.energy.dto.param.BranchDeviceInsertParam;
import com.cgnpc.bbxpark.energy.dto.param.BranchDeviceParam;
import com.cgnpc.bbxpark.energy.mapper.BranchDeviceRepository;
import com.cgnpc.bbxpark.energy.service.IBranchDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 支路设备服务实现类
 */
@Service
public class BranchDeviceServiceImpl extends ServiceImpl<BranchDeviceRepository, BranchDevice> implements IBranchDeviceService {
    @Autowired
    private BranchDeviceRepository branchDeviceRepository;
    @Autowired
    private IocDeviceServiceImpl iocDeviceService;

    @Override
    public Boolean insert(BranchDeviceInsertParam param) {
        if (param.getBranchId() == null)
            throw GenericException.fail("支路ID不能为空！");
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        String userId = WebFrameworkUtils.getHeaderUserId();
        List<BranchDevice> branchDevices = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(param.getBranchDeviceParamList())) {
            for (BranchDeviceParam branchDeviceParam : param.getBranchDeviceParamList()) {
                BranchDevice branchDevice = BeanUtils.convertTo(branchDeviceParam, BranchDevice::new);
                branchDevice.setTenantId(tenantId);
                branchDevice.setCreatorId(userId);
                branchDevice.setCreateTime(new Date());
                branchDevice.setBranchId(param.getBranchId());
                branchDevices.add(branchDevice);
            }
            return this.saveBatch(branchDevices);
        }else {
            throw GenericException.fail("支路设备信息不能为空！");
        }
    }

    /**
     * 根据支路id获取支路设备信息
     * @param id
     * @return
     */
    @Override
    public List<BranchDeviceModel> getBranchDevice(Long id) {
        if (id == null)
            throw GenericException.fail("支路ID不能为空！");
        List<BranchDevice> branchDevices = branchDeviceRepository.selectList(new QueryWrapper<BranchDevice>().eq("branch_id", id).eq("deleted", Status.enabled.getKey()));
        return BeanUtils.convertListTo(branchDevices, BranchDeviceModel::new);
    }

    @Override
    public List<BranchDeviceModel> getBranchDevice(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            return new ArrayList<>();
        }
        List<BranchDevice> branchDevices = this.list(Wrappers.<BranchDevice>lambdaQuery().in(BranchDevice::getBranchId, idList).eq(BranchDevice::getDeleted, Status.enabled.getKey()));
        return BeanUtils.convertListTo(branchDevices, BranchDeviceModel::new);
    }


    /**
     * 修改支路设备信息
     * @param param
     * @return
     */
    @Override
    public Boolean updateBranchDevice(BranchDeviceInsertParam param) {
        if (param.getBranchId() == null)
            throw GenericException.fail("支路ID不能为空！");
        //删除旧数据
        branchDeviceRepository.delete(new QueryWrapper<BranchDevice>().eq("branch_id", param.getBranchId()));
        //保存新数据
        if (CollectionUtils.isNotEmpty(param.getBranchDeviceParamList())) {
            List<BranchDevice> branchDevices = new ArrayList<>();
            for (BranchDeviceParam branchDeviceParam : param.getBranchDeviceParamList()) {
                BranchDevice branchDevice = BeanUtils.convertTo(branchDeviceParam, BranchDevice::new);
                branchDevice.setTenantId(WebFrameworkUtils.getHeaderTenantId());
                branchDevice.setCreatorId(WebFrameworkUtils.getHeaderUserId());
                branchDevice.setCreateTime(new Date());
                branchDevice.setBranchId(param.getBranchId());
                branchDevices.add(branchDevice);
            }
            this.saveBatch(branchDevices);
        }
        return Boolean.TRUE;
    }

    @Override
    public IPage<IocDeviceModel> getDevices(IocDevicePageParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<BranchDevice> branchDevices = branchDeviceRepository.selectList(new LambdaQueryWrapper<BranchDevice>().eq(BranchDevice::getTenantId, tenantId));
        List<Long> deviceIds = branchDevices.stream().map(BranchDevice::getDeviceId).collect(Collectors.toList());
        param.setTenantId(tenantId);
        param.setNoDeviceIdList(deviceIds);
        param.setAuth(false);
        IPage<IocDeviceModel> device = iocDeviceService.pageDevice(param);
        return device;
    }
}
