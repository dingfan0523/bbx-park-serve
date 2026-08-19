package com.cgnpc.bbxpark.traffic.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleMaintain;
import org.springframework.web.multipart.MultipartFile;

/***
 * @Description 车辆保养信息
 * @author huangyongtao
 * @date 2026/5/27 16:34
 */
public interface IDwdVehicleMaintainService extends IService<DwdVehicleMaintain> {

    ImportReturnModel analysisAndCheckExcelData(MultipartFile file, File fi);

    /**
     * 删除记录
     * @param fileId id
     * @return 结果
     */
    Boolean remove(Long fileId);

    Boolean importData();

}
