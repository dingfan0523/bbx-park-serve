package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.ThirdMeetingRecord;

public interface IThirdMeetingRecordService extends IService<ThirdMeetingRecord> {
    Boolean removeByFileId(Long fileId);
}
