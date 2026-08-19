package com.cgnpc.bbxpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.ThirdMeetingRecord;
import com.cgnpc.bbxpark.meeting.mapper.ThirdMeetingRecordRepository;
import com.cgnpc.bbxpark.meeting.service.IThirdMeetingRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class ThirdMeetingRecordServiceImpl extends ServiceImpl<ThirdMeetingRecordRepository, ThirdMeetingRecord> implements IThirdMeetingRecordService {

    @Override
    public Boolean removeByFileId(Long fileId) {
        List<ThirdMeetingRecord> list = list(Wrappers.<ThirdMeetingRecord>lambdaQuery().eq(ThirdMeetingRecord::getFileId,fileId)
                .eq(ThirdMeetingRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId()).eq(ThirdMeetingRecord::getDeleted, Delete.NORMAL.getKey()));
        list.forEach(r->r.setDeleted(Delete.DELETED.getKey()));
        return updateBatchById(list);
    }
}
