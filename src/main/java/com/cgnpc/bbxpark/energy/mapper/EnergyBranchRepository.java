package com.cgnpc.bbxpark.energy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.energy.domain.EnergyBranch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 能源支路持久层
 */
@Repository
public interface EnergyBranchRepository extends BaseMapper<EnergyBranch> {
    List<EnergyBranch> listWithChildren(@Param("branchId")Long branchId,@Param("tenantId")Long tenantId);
}
