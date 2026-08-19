
package com.cgnpc.bbxpark.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.DeptOfficeSpaceAreaModel;
import com.cgnpc.bbxpark.ioc.dto.model.DeptOfficeSpaceStationModel;
import com.cgnpc.bbxpark.space.domain.SpaceBasicInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpaceBasicInfoRepository extends BaseMapper<SpaceBasicInfo> {
    /**
     * 部门办公空间分析-面积维度
     */
    List<DeptOfficeSpaceAreaModel> getDeptOfficeSpaceArea(@Param("tenantId")Long tenantId);
    /**
     * 部门办公空间分析-面积维度
     */
    List<DeptOfficeSpaceStationModel> getDeptOfficeSpaceStation(@Param("tenantId")Long tenantId);
}
