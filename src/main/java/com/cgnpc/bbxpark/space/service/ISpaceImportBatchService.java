
package com.cgnpc.bbxpark.space.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.space.domain.SpaceImportBatch;
import com.cgnpc.bbxpark.space.dto.param.SpaceImportBatchParam;

public interface ISpaceImportBatchService extends IService<SpaceImportBatch> {

    /**
     * 新增空间信息导入批次表.
     * @Param param 批次信息
     * @Return 新增批次是否成功
     */
    Boolean add(SpaceImportBatchParam param);
}
