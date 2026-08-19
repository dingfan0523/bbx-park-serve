
package com.cgnpc.bbxpark.invitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.invitation.domain.ApprovalRecord;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalRecordPageParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRecordRepository extends BaseMapper<ApprovalRecord> {
    IPage<Long> approvedPage(IPage<ApprovalRecordPageParam> page, @Param("tenantId")Long tenantId, @Param("userId") String userId);
}
