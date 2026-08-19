package com.cgnpc.bbxpark.energy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.energy.domain.EnergyAbnormalRemind;
import com.cgnpc.bbxpark.energy.dto.model.EnergyAbnormalRemindModel;
import com.cgnpc.bbxpark.energy.dto.param.EnergyAbnormalRemindParam;

/**
 * @create zhaoshuo
 * @time 2025/4/22
 * @desc 能耗异常提醒服务接口
 */
public interface IEnergyAbnormalRemindService extends IService<EnergyAbnormalRemind> {
    /**
     * 快速报单
     * @param id 异常id
     * @return
     */
    Boolean submitProblem(Long id);

    /**
     * 搜索同比能耗异常偏差
     */
    void searchEnergyAbnormal();

    /**
     * 分页查询
     * @param param
     * @return
     */
    IPage<EnergyAbnormalRemindModel> queryPage(EnergyAbnormalRemindParam param);

    /**
     * 搜索休息时段能耗异常偏差
     */
    void searchSleepEnergy();
}
