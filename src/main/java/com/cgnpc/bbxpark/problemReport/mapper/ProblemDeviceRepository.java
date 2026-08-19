package com.cgnpc.bbxpark.problemReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.problemReport.domain.ProblemDevice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc
 */
@Repository
public interface ProblemDeviceRepository extends BaseMapper<ProblemDevice> {
    void insertBatch(List<ProblemDevice> problemDeviceList);
}
