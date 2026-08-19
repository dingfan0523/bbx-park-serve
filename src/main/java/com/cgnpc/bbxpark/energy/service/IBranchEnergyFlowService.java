package com.cgnpc.bbxpark.energy.service;

import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.energy.dto.param.BranchEnergyFlowQueryParam;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/4/24
 * @desc 支路能耗流向图服务接口
 */
public interface IBranchEnergyFlowService {

    List<EnergyBranchModel> queryData(BranchEnergyFlowQueryParam param);

    List<EnergyBranchModel> queryByBranchId(Long branchId,BranchEnergyFlowQueryParam param);

    Boolean executeAutoReadingRemind(String type);
}
