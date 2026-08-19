package com.cgnpc.bbxpark.device.service;

import com.cgnpc.bbxpark.device.domain.IocProductFile;
import com.cgnpc.bbxpark.device.dto.model.IocProductFileModel;
import com.cgnpc.bbxpark.device.dto.param.IocProductFileUpdate;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/25
 * @desc ioc产品设备文件上传接口
 */
public interface IIocProductFileService extends IBaseService<IocProductFile> {
    /**
     * 上传产品设备文件
     * @param iocProductFile 产品文件数据
     * @return
     */
    Boolean uploadProductFile(IocProductFile iocProductFile);

    /**
     * 修改产品设备文件
     * @param iocProductFiles 产品文件数据
     * @return
     */
    Boolean updateFileList(List<IocProductFileUpdate> iocProductFiles);

    /**
     * 删除文件
     * @param id 文件id
     * @return
     */
    Boolean deleteFile(Long id);

    /**
     * 根据产品id查询上传文件
     * @param id 产品id
     * @param type 文件业务类型
     * @return
     */
    List<IocProductFileModel> queryByBusinessId(Long id , Integer type);

}
