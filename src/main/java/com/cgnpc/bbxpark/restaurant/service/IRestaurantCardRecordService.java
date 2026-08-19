package com.cgnpc.bbxpark.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.ThirdMeetingRecord;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantCardRecord;
import com.cgnpc.bbxpark.settings.domain.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 餐厅一卡通
 */
public interface IRestaurantCardRecordService extends IService<RestaurantCardRecord> {


    ImportReturnModel analysisAndCheckExcelData(MultipartFile file, File id);

    /**
     * 删除记录
     * @param fileId id
     * @return 结果
     */
    Boolean remove(Long fileId);
}
