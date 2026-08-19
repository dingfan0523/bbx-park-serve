package com.cgnpc.bbxpark.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.device.domain.IocProductFile;
import com.cgnpc.bbxpark.device.dto.model.IocProductFileModel;
import com.cgnpc.bbxpark.device.dto.param.IocProductFileUpdate;
import com.cgnpc.bbxpark.device.mapper.IocProductFileRepository;
import com.cgnpc.bbxpark.device.service.IIocProductFileService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @create zhaoshuo
 * @time 2025/2/25
 * @desc ioc产品设备文件上传接口实现
 */
@Service
public class IocProductFileServiceImpl extends BaseServiceImpl<IocProductFileRepository, IocProductFile> implements IIocProductFileService {
    @Autowired
    private IocProductFileRepository iocProductFileRepository;
    @Autowired
    private IUserApiService userApiService;

    /**
     * 上传产品文件
     * @param iocProductFile 产品文件数据
     * @return
     */
    @Override
    public Boolean uploadProductFile(IocProductFile iocProductFile) {
        try {
            if (!StringUtils.hasLength(iocProductFile.getName())
                    || !StringUtils.hasLength(iocProductFile.getUrl())
                    || iocProductFile.getBusinessId() == null){
                throw GenericException.fail("入参缺失，请检查数据后重新请求。");
            }
            iocProductFile.setCreateBy(userApiService.getCurrentStaffName());
            return save(iocProductFile);
        } catch (Exception e) {
            log.error("新增失败！", e);
            throw GenericException.fail("新增失败！！");
        }
    }

    /**
     * 修改产品设备文件
     * @param iocProductFiles 产品文件数据
     * @return
     */
    @Override
    public Boolean updateFileList(List<IocProductFileUpdate> iocProductFiles) {
        if (CollectionUtil.isNotEmpty(iocProductFiles)){
            iocProductFileRepository.updateBatchById(iocProductFiles);
        }
        return true;
    }

    /**
     * 删除文件
     * @param id 文件id
     * @return
     */
    @Override
    public Boolean deleteFile(Long id) {
        if (id == null ){
            throw GenericException.fail("文件id不能为空！！");
        }
        IocProductFile productFile = new IocProductFile();
        productFile.setId(id);
        productFile.setDeleted(Delete.DELETED.getKey());
        if (updateById(productFile)){
            return true;
        }else{
            throw GenericException.fail("删除失败，根据文件id未查询到数据！！");
        }
    }

    /**
     * 根据产品id查询上传文件
     * @param id 产品id
     * @return
     */
    @Override
    public List<IocProductFileModel> queryByBusinessId(Long id , Integer type) {
        if (id == null || type == null){
            throw GenericException.fail("产品id不能为空！！");
        }
        LambdaUpdateWrapper<IocProductFile> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(IocProductFile::getBusinessId,id);
        queryWrapper.eq(IocProductFile::getType , type);
        queryWrapper.eq(IocProductFile::getDeleted , Delete.NORMAL.getKey());
        queryWrapper.orderByDesc(IocProductFile::getCreateTime);
        List<IocProductFile> iocProductFiles = iocProductFileRepository.selectList(queryWrapper);
        List<IocProductFileModel> iocProductModels = BeanUtils.convertListTo(iocProductFiles, IocProductFileModel::new);
        //获取当前登录人员工号
        String staffNo = userApiService.getCurrentStaffNo();
        for (IocProductFileModel iocProductModel : iocProductModels) {
            Optional.ofNullable(staffNo).ifPresent(s->iocProductModel.setIsCreator(staffNo.equals(iocProductModel.getCreatorId())));
        }
        return iocProductModels;
    }
}
