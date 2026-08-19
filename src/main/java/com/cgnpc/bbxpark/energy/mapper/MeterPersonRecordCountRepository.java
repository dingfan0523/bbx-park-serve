
package com.cgnpc.bbxpark.energy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.energy.domain.MeterPersonRecordCount;
import com.cgnpc.bbxpark.ioc.dto.model.ElectricityAreaCompareModel;
import com.cgnpc.bbxpark.ioc.dto.model.ElectricityPerCapitaTrendModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/***
 * @Description 抄表人工抄表记录统计数据操作接口
 * @author huangyongtao
 * @date 2025/4/21 9:24
 */
@Repository
public interface MeterPersonRecordCountRepository extends BaseMapper<MeterPersonRecordCount> {
    List<ElectricityAreaCompareModel> findAreaCompare(@Param("branchId")Long branchId, @Param("branchType")String branchType,
                                                     @Param("startTime") Date startTime, @Param("endTime")Date endTime,@Param("tenantId")Long tenantId);

    List<ElectricityPerCapitaTrendModel> getMonthlyPerCapitaTrend(@Param("branchType")String branchType,@Param("year")Integer year, @Param("tenantId")Long tenantId);

    List<ElectricityPerCapitaTrendModel> getDailyPerCapitaTrend(@Param("branchType")String branchType,@Param("year")Integer year,
                                                                  @Param("month")Integer month,@Param("tenantId")Long tenantId);
}
