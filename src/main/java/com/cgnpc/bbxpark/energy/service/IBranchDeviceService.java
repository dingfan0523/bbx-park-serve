package com.cgnpc.bbxpark.energy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.energy.domain.BranchDevice;
import com.cgnpc.bbxpark.energy.dto.model.BranchDeviceModel;
import com.cgnpc.bbxpark.energy.dto.param.BranchDeviceInsertParam;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 支路设备服务类
 */
public interface IBranchDeviceService extends IService<BranchDevice> {
    /**
     * 新增支路设备
     *
     * @param param
     * @return
     */
    Boolean insert(BranchDeviceInsertParam param);

    /**
     * 根据支路ID查询支路设备
     *
     * @param id
     * @return
     */
    List<BranchDeviceModel> getBranchDevice(Long id);

    /**
     * 根据支路ID集合查询支路设备
     *
     * @param idList
     * @return
     */
    List<BranchDeviceModel> getBranchDevice(List<Long> idList);

    /**
     * 修改支路设备
     *
     * @param param
     * @return
     */
    Boolean updateBranchDevice(BranchDeviceInsertParam param);

    /**
     * 查询支路设备列表
     *
     * @param param
     * @return
     */
    IPage<IocDeviceModel> getDevices(IocDevicePageParam param);
}
