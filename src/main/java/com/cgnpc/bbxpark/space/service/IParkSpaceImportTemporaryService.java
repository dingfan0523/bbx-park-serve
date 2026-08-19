
package com.cgnpc.bbxpark.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.space.domain.ParkSpaceImportTemporary;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceImportTemporaryModel;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceImportTemporaryListParam;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceImportTemporaryParam;

import java.util.List;




public interface IParkSpaceImportTemporaryService extends IService<ParkSpaceImportTemporary> {

    /**
     * 新增导入临时表.
     * @Param param 临时表信息
     * @Return 新增临时表是否成功
     */
    Boolean add(ParkSpaceImportTemporaryParam param);

    /**
     * 批量新增导入临时表.
     * @Param param 临时表信息
     * @Return 新增临时表是否成功
     */
    Boolean batchAdd(List<ParkSpaceImportTemporaryParam> params);


    /**
     * 获取园区空间导入临时表
     *
     * @param param 参数
     * @return 临时表
     */
    List<ParkSpaceImportTemporaryModel> findAllList(ParkSpaceImportTemporaryListParam param);
}
