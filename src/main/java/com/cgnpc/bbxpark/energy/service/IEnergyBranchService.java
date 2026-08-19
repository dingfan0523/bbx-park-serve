package com.cgnpc.bbxpark.energy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.energy.domain.EnergyBranch;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchParam;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchQueryParam;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 能源支路服务接口
 */
public interface IEnergyBranchService extends IService<EnergyBranch> {
    /**
     * 新增支路
     *
     * @param param
     * @return
     */
    Boolean insert(EnergyBranchParam param);

    /**
     * 修改支路
     *
     * @param param
     * @return
     */
    Boolean updateById(EnergyBranchParam param);

    /**
     * 删除支路
     *
     * @param id
     * @return
     */
    Boolean delete(Long id);

    /**
     * 分页查询支路
     *
     * @param param
     * @return
     */
    List<EnergyBranchModel> findTreeList(EnergyBranchQueryParam param);

    /**
     * 分页查询支路
     *
     * @param param
     * @return
     */
    List<EnergyBranchModel> findEnablerTree(EnergyBranchQueryParam param);

    /**
     * 启禁用支路
     *
     * @param param
     * @return
     */
    Boolean updateStatus(EnergyBranchParam param);

     List<EnergyBranchModel> buildQuery(EnergyBranchQueryParam param);
}
