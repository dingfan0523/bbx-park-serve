package com.cgnpc.bbxpark.restaurant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FilePageParam;
import com.cgnpc.bbxpark.settings.dto.param.FileParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 餐厅文件
 */
public interface IRestaurantFileService{

    /**
     * 上传产品设备文件
     * @param file 产品文件数据
     * @return
     */
    ImportReturnModel importFile(MultipartFile file, Integer type);


    /**
     * 分页查询
     * @param param
     * @return
     */
    IPage<FileModel> pageFile(FilePageParam param);

    /**
     * 删除文件
     * @param id 文件id
     * @return
     */
    Boolean removeFile(Long id);

}
