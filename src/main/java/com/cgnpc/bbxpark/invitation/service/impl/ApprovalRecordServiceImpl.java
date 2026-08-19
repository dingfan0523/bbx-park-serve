
package com.cgnpc.bbxpark.invitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.invitation.domain.ApprovalRecord;
import com.cgnpc.bbxpark.invitation.dto.model.ApprovalRecordModel;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalRecordListParam;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalRecordPageParam;
import com.cgnpc.bbxpark.invitation.mapper.ApprovalRecordRepository;
import com.cgnpc.bbxpark.invitation.service.IApprovalRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


@Service("approvalRecordService")
public class ApprovalRecordServiceImpl extends ServiceImpl<ApprovalRecordRepository,ApprovalRecord> implements IApprovalRecordService {

    @Resource
    private ApprovalRecordRepository approvalRecordRepository;

	@Override
	public IPage<Long> page(ApprovalRecordPageParam param) {
        String userId = WebFrameworkUtils.getHeaderUserId();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        // 分页查询
        IPage<Long> page = approvalRecordRepository.approvedPage(new Page<>(param.getCurrent(), param.getSize()),tenantId,userId);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return ConvertUtil.pageEmptyConvert(page.getCurrent(),page.getSize());
        }
        return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(), page.getRecords());
	}

    @Override
    public List<ApprovalRecordModel> list(ApprovalRecordListParam param) {
        List<ApprovalRecord> list = list(Wrappers.<ApprovalRecord>lambdaQuery().eq(ApprovalRecord::getBusinessId,param.getBusinessId()).eq(ApprovalRecord::getDeleted, Status.enabled.getKey()).orderByDesc(ApprovalRecord::getCreateTime));
        return BeanUtils.convertListTo(list, ApprovalRecordModel::new);
    }
}
