package com.cgnpc.bbxpark.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantInventoryRecord;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantWasteRecord;
import com.cgnpc.bbxpark.settings.domain.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * 餐厅垃圾
 */
public interface IRestaurantWasteRecordService extends IService<RestaurantWasteRecord> {

    ImportReturnModel analysisAndCheckExcelData(MultipartFile file, File fi);

    /**
     * 删除记录
     * @param fileId id
     * @return 结果
     */
    Boolean remove(Long fileId);

}
