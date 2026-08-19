package com.cgnpc.bbxpark.problemReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.problemReport.domain.ProblemReport;
import org.springframework.stereotype.Repository;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc 报事报修数据控制层
 */
@Repository
public interface ProblemReportRepository extends BaseMapper<ProblemReport> {
}
