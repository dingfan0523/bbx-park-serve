package com.cgnpc.bbxpark.settings.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.settings.domain.Oss;
import com.cgnpc.bbxpark.settings.dto.model.OssModel;
import com.cgnpc.bbxpark.settings.dto.param.OssListParam;
import com.cgnpc.bbxpark.settings.dto.param.OssPageParam;
import com.cgnpc.bbxpark.settings.dto.param.OssParam;
import com.cgnpc.bbxpark.settings.mapper.OssRepository;
import com.cgnpc.bbxpark.settings.service.IOssService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 摘    要： []
 *
 */
@Service
public class OssServiceImpl extends BaseServiceImpl<OssRepository, Oss> implements IOssService {
    /**
     * 注入repository.
     */
    @Autowired
    private OssRepository ossRepository;


    /**
     * 根据OSS对象存储标识获得OSS对象存储详情信息.
     * @Param [ossId] OSS对象存储标识
     * @Return OSS对象存储详情信息
     */
    @Override
    public OssModel detail(Long ossId) {
        Oss oss = this.getById(ossId);
        AssertUtils.notNull(oss, SystemResultCode.RESULT_DATA_NONE.message());
        return BeanUtils.convertTo(oss, OssModel::new);
    }

    /**
     * 获取OSS对象存储列表(分页).
     * @Param param OSS对象存储查询条件
     * @Return OSS对象存储信息列表（分页）
     */
    @Override
    public IPage<OssModel> page(OssPageParam param) {
        IPage<Oss> result = this.page(new Page<>(param.getCurrent(),param.getSize()), Wrappers.<Oss>lambdaQuery()
                .eq(param.getOssId() != null,Oss::getId,param.getOssId())
                .like(StringUtils.isNotEmpty(param.getFileName()),Oss::getFileName,param.getFileName())
                .like(StringUtils.isNotEmpty(param.getOriginalName()),Oss::getOriginalName,param.getOriginalName())
                .eq(param.getStatus()!=null,Oss::getStatus,param.getStatus())
                .orderByDesc(Oss::getCreateTime));
        if(CollectionUtils.isEmpty(result.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        return ConvertUtil.pageConvert(result,BeanUtils.convertListTo(result.getRecords(),OssModel::new));
    }

    /**
     * 获取OSS对象存储列表.
     * @Param param OSS对象存储查询条件
     * @Return OSS对象存储信息列表
     */
    @Override
    @SneakyThrows
    public List<OssModel> list(OssListParam param) {
       List<Oss> list = this.list(Wrappers.<Oss>lambdaQuery().eq(param.getOssId() != null,Oss::getId,param.getOssId())
                .like(StringUtils.isNotEmpty(param.getFileName()),Oss::getFileName,param.getFileName())
                .like(StringUtils.isNotEmpty(param.getOriginalName()),Oss::getOriginalName,param.getOriginalName())
                .eq(param.getStatus()!=null,Oss::getStatus,param.getStatus())
                .orderByDesc(Oss::getCreateTime));
       return BeanUtils.convertListTo(list,OssModel::new);
    }

    /**
     * 新增OSS对象存储.
     * @Param param OSS对象存储信息
     * @Return 新增OSS对象存储是否成功
     */
    @Override
    public Boolean add(OssParam param) {
        Oss oss = BeanUtils.convertTo(param, Oss::new);
        oss.setId(param.getOssId());
        return save(oss);
    }

    /**
     * 批量新增OSS对象存储.
     * @Param params OSS对象存储信息列表
     * @Return 批量新增OSS对象存储是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addBatch(List<OssParam> params) {
        List<Oss> osss = BeanUtils.convertListTo(params, Oss::new);
        return this.saveBatch(osss);
    }

    /**
     * 删除OSS对象存储.
     * @Param ossId OSS对象存储标识
     * @Return 删除OSS对象存储是否成功
     */
    @Override
    public Boolean remove(Long ossId) {
        Oss oss = this.getById(ossId);
        AssertUtils.notNull(oss, SystemResultCode.RESULT_DATA_NONE.message());
        oss.setDeleted(Delete.DELETED.getKey());
        return updateById(oss);
    }

    /**
     * 批量删除OSS对象存储.
     * @Param ossIds OSS对象存储标识列表
     * @Return 批量删除OSS对象存储是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeBatch(List<Long> ossIds) {
        List<Oss> list = (List<Oss>) this.listByIds(ossIds);
        list.forEach(l-> l.setDeleted(Delete.DELETED.getKey()));
        return updateBatchById(list);
    }

    /**
     * 编辑OSS对象存储信息.
     * @Param param OSS对象存储信息
     * @Return 编辑OSS对象存储是否成功
     */
    @Override
    public Boolean edit(Long ossId, OssParam param) {
        Oss oss = this.getById(ossId);
        AssertUtils.notNull(oss, SystemResultCode.RESULT_DATA_NONE.message());
        Oss editParam = BeanUtils.convertTo(param, Oss::new);
        /**
         * 保护不可编辑字段
         */
        return updateById(editParam);
    }

    /**
     * 批量编辑OSS对象存储信息.
     * @Param params OSS对象存储信息列表
     * @Return 批量编辑OSS对象存储是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editBatch(List<OssParam> params) {
        List<Oss> osss = BeanUtils.convertListTo(params, Oss::new);
        return updateBatchById(osss);
    }

    /**
     * 启用OSS对象存储.
     * @Param ossId OSS对象存储标识
     * @Return 启用OSS对象存储是否成功
     */
    @Override
    public Boolean enable(Long ossId) {
        Oss oss = new Oss();
        oss.setId(ossId);
        oss.setStatus(Status.enabled.getKey());
        return updateById(oss);
    }

    /**
     * 批量启用OSS对象存储信息.
     * @Param ossIds OSS对象存储标识列表
     * @Return 批量启用OSS对象存储是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean enableBatch(List<Long> ossIds) {
        List<Oss> osss = ossIds.stream().map(ossId -> {
            Oss oss = new Oss();
            oss.setId(ossId);
            oss.setStatus(Status.enabled.getKey());
            return oss;
        }).collect(Collectors.toList());
        return updateBatchById(osss);
    }

    /**
     * 禁用OSS对象存储.
     * @Param ossId OSS对象存储标识
     * @Return 禁用OSS对象存储是否成功
     */
    @Override
    public Boolean disable(Long ossId) {
        Oss oss = new Oss();
        oss.setId(ossId);
        oss.setStatus(Status.disabled.getKey());
        return updateById(oss);
    }

    /**
     * 批量禁用OSS对象存储信息.
     * @Param ossIds OSS对象存储标识列表
     * @Return 批量禁用OSS对象存储是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean disableBatch(List<Long> ossIds) {
        List<Oss> osss = ossIds.stream().map(ossId -> {
            Oss oss = new Oss();
            oss.setId(ossId);
            oss.setStatus(Status.disabled.getKey());
            return oss;
        }).collect(Collectors.toList());
        return updateBatchById(osss);
    }
}
