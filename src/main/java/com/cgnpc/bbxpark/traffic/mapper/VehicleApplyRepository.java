
package com.cgnpc.bbxpark.traffic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.CallTaxiDeptStatModel;
import com.cgnpc.bbxpark.ioc.dto.model.CallTaxiReasonStatModel;
import com.cgnpc.bbxpark.traffic.domain.VehicleApply;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

public interface VehicleApplyRepository extends BaseMapper<VehicleApply> {
    List<CallTaxiReasonStatModel> getCallTaxiReasonAnalysis(@Param("start") Date start,@Param("end") Date end);

    List<CallTaxiDeptStatModel> getCallTaxiDeptAnalysis(@Param("start") Date start,@Param("end") Date end);
}
