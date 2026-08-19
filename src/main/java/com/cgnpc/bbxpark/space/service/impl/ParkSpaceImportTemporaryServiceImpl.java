
package com.cgnpc.bbxpark.space.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.domain.ParkSpaceImportTemporary;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceImportTemporaryModel;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceImportTemporaryListParam;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceImportTemporaryParam;
import com.cgnpc.bbxpark.space.mapper.ParkSpaceImportTemporaryRepository;
import com.cgnpc.bbxpark.space.service.IParkSpaceImportTemporaryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("parkSpaceImportTemporaryService")
public class ParkSpaceImportTemporaryServiceImpl extends ServiceImpl<ParkSpaceImportTemporaryRepository, ParkSpaceImportTemporary> implements IParkSpaceImportTemporaryService {

    /**
     * 新增导入临时表.
     * @Param param 临时表信息
     * @Return 新增临时表是否成功
     */
    @Override
    public Boolean add(ParkSpaceImportTemporaryParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        ParkSpaceImportTemporary parkSpaceImportTemporary = BeanUtils.convertTo(param, ParkSpaceImportTemporary::new);
        parkSpaceImportTemporary.setId(null);
        parkSpaceImportTemporary.setImportStatus("2");
        parkSpaceImportTemporary.setTenantId(tenantId);
        return save(parkSpaceImportTemporary);
    }

    /**
     * 批量新增导入临时表.
     * @Param param 临时表信息
     * @Return 新增临时表是否成功
     */
    @Override
    public Boolean batchAdd(List<ParkSpaceImportTemporaryParam> params) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<ParkSpaceImportTemporary> importTemporaries = BeanUtils.convertListTo(params, ParkSpaceImportTemporary::new);
        importTemporaries.forEach(f->f.setTenantId(tenantId));
        return saveBatch(importTemporaries);
    }


    /**
     * 获取园区空间导入临时表
     *
     * @param param 参数
     * @return 临时表
     */
    @Override
    public List<ParkSpaceImportTemporaryModel> findAllList(ParkSpaceImportTemporaryListParam param) {
        //查询导入临时表
        List<ParkSpaceImportTemporary> list = list(Wrappers.<ParkSpaceImportTemporary>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getBatchCode()), ParkSpaceImportTemporary::getBatchCode, param.getBatchCode())
                .eq(ParkSpaceImportTemporary::getTenantId,  WebFrameworkUtils.getHeaderTenantId())
        );
        return BeanUtils.convertListTo(list,ParkSpaceImportTemporaryModel::new);
    }

}
