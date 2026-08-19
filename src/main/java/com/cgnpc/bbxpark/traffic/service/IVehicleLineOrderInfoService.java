package com.cgnpc.bbxpark.traffic.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.traffic.domain.VehicleApply;
import com.cgnpc.bbxpark.traffic.domain.VehicleLineOrderInfo;
import org.springframework.web.multipart.MultipartFile;


public interface IVehicleLineOrderInfoService extends IService<VehicleLineOrderInfo> {
    ImportReturnModel analysisAndCheckExcelData(MultipartFile file, File fi);

    /**
     * 删除记录
     * @param fileId id
     * @return 结果
     */
    Boolean remove(Long fileId);

    Boolean importData();
}
