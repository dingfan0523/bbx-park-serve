package com.cgnpc.bbxpark.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantInboundRecord;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantInventoryRecord;
import com.cgnpc.bbxpark.settings.domain.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * 餐厅物料库存
 */
public interface IRestaurantInventoryRecordService extends IService<RestaurantInventoryRecord> {

    ImportReturnModel analysisAndCheckExcelData(MultipartFile file, File fi);


    /**
     * 删除记录
     * @param fileId id
     * @return 结果
     */
    Boolean remove(Long fileId);
}
