
package com.cgnpc.bbxpark.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.domain.SpaceImportBatch;
import com.cgnpc.bbxpark.space.dto.param.SpaceImportBatchParam;
import com.cgnpc.bbxpark.space.mapper.SpaceImportBatchRepository;
import com.cgnpc.bbxpark.space.service.ISpaceImportBatchService;
import org.springframework.stereotype.Service;

@Service("spaceImportBatchService")
public class SpaceImportBatchServiceImpl extends ServiceImpl<SpaceImportBatchRepository, SpaceImportBatch> implements ISpaceImportBatchService {

    @Override
    public Boolean add(SpaceImportBatchParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        SpaceImportBatch spaceImportBatch = BeanUtils.convertTo(param, SpaceImportBatch::new);
        spaceImportBatch.setId(null);
        spaceImportBatch.setTenantId(tenantId);
        return save(spaceImportBatch);
    }
}
