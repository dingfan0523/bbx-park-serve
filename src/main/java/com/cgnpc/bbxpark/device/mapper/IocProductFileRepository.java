package com.cgnpc.bbxpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.device.domain.IocProductFile;
import com.cgnpc.bbxpark.device.dto.param.IocProductFileUpdate;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/25
 * @desc
 */
public interface IocProductFileRepository extends BaseMapper<IocProductFile> {
    /**
     * 根据id批量更新
     * @param iocProductFiles
     */
    void updateBatchById(List<IocProductFileUpdate> iocProductFiles);
}
