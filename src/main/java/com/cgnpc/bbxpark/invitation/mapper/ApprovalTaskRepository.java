
package com.cgnpc.bbxpark.invitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.invitation.domain.ApprovalTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalTaskRepository extends BaseMapper<ApprovalTask> {

    IPage<Long> pendingApprovalPage(IPage<Long> page, @Param("tenantId")Long tenantId, @Param("userId") String userId);
}
