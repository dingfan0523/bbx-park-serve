package com.cgnpc.bbxpark.settings.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FilePageParam;
import com.cgnpc.bbxpark.settings.mapper.FileRepository;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class FileServiceImpl extends BaseServiceImpl<FileRepository, File> implements IFileService {

    @Override
    public IPage<FileModel> findByTypeIn(FilePageParam param) {
        IPage<File> page = page(new Page<>(param.getCurrent(),param.getSize()),Wrappers.<File>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(param.getTypes()),File::getType, param.getTypes())
                .eq(param.getType() != null,File::getType,param.getType())
                .eq(File::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(File::getDeleted, Delete.NORMAL.getKey()).orderByDesc(File::getCreateTime));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        return ConvertUtil.pageConvert(page,BeanUtils.convertListTo(page.getRecords(), FileModel::new));
    }

    @Override
    public List<FileModel> findByTypeAndRelatedId(Integer type, Long relatedId) {
        List<File> list = list(Wrappers.<File>lambdaQuery().eq(File::getType, type).eq(File::getRelatedId, relatedId).eq(File::getDeleted, Delete.NORMAL.getKey()));
        return BeanUtils.convertListTo(list, FileModel::new);
    }

    @Override
    public List<FileModel> findByTypeAndRelatedIds(Integer type, List<Long> relatedIds) {
        List<File> list = list(Wrappers.<File>lambdaQuery().eq(File::getType, type).in(File::getRelatedId, relatedIds).eq(File::getDeleted, Delete.NORMAL.getKey()));
        return BeanUtils.convertListTo(list, FileModel::new);
    }

    @Override
    public IPage<FileModel> pageBy(Integer type, Long relatedId, Integer page, Integer limit) {
        IPage<File> filePage = page(new Page<>(page, limit), Wrappers.<File>lambdaQuery().eq(File::getType, type).eq(File::getRelatedId, relatedId).eq(File::getDeleted, Delete.NORMAL.getKey()));
        return ConvertUtil.pageConvert(filePage,BeanUtils.convertListTo(filePage.getRecords(),FileModel::new));
    }

    @Override
    public Boolean add(File file) {
        return save(file);
    }

    @Override
    public Boolean remove(Long id) {
        File file = getById(id);
        AssertUtils.notNull(file, SystemResultCode.RESULT_DATA_NONE.message());
        file.setDeleted(Delete.DELETED.getKey());
        return updateById(file);
    }

    @Override
    public Boolean removeByRelatedId(Integer type, Long relatedId) {
        return this.remove(Wrappers.<File>lambdaQuery().eq(File::getRelatedId,relatedId).eq(File::getType,type));
    }

    @Override
    public Boolean removeByRelatedIds(Integer type, List<Long> relatedIds) {
        return this.remove(Wrappers.<File>lambdaQuery().in(File::getRelatedId,relatedIds).eq(File::getType,type));
    }

    @Override
    public Boolean addBatch(List<File> files) {
        return saveBatch(files);
    }

    @Override
    public Boolean exist(String name, Integer type) {
        return this.count(Wrappers.<File>lambdaQuery().eq(type != null,File::getType,type)
                .eq(StringUtils.isNotEmpty(name),File::getName,name)
                .eq(File::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(File::getDeleted, Delete.NORMAL.getKey())) > 0;
    }
}
