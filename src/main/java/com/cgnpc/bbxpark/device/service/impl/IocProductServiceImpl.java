package com.cgnpc.bbxpark.device.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.common.enums.FileTypeEnum;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.IocProduct;
import com.cgnpc.bbxpark.device.dto.model.IocProductModel;
import com.cgnpc.bbxpark.device.dto.param.IocProductAddParam;
import com.cgnpc.bbxpark.device.dto.param.IocProductPageParam;
import com.cgnpc.bbxpark.device.mapper.IocProductRepository;
import com.cgnpc.bbxpark.device.service.IIocProductService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @create zhaoshuo
 * @time 2025/2/24
 * @desc ioc产品服务实现类
 */
@Slf4j
@Service("iocProductService")
public class IocProductServiceImpl extends BaseServiceImpl<IocProductRepository, IocProduct> implements IIocProductService {
    @Autowired
    private IocProductRepository iocProductRepository;

    @Autowired
    private IFileService iFileService;

    /**
     * ioc产品新增
     *
     * @param iocProductAddParam 产品数据入参
     * @return
     */
    @Override
    public Boolean add(IocProductAddParam iocProductAddParam) {
        try {
            IocProduct iocProduct = BeanUtils.convertTo(iocProductAddParam, IocProduct::new);
            //产品新增成功插入文件表数据
            if (save(iocProduct)) {
                if (CollectionUtils.isNotEmpty(iocProductAddParam.getFileList())) {
                    List<File> fileList = iocProductAddParam.getFileList();
                    for (File file : fileList) {
                        file.setType(FileTypeEnum.PRODUCT.getValue());
                        file.setRelatedId(iocProduct.getId());
                    }
                    iFileService.addBatch(fileList);
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("新增失败！", e);
            throw GenericException.fail("新增失败！！");
        }
    }

    /**
     * 修改产品信息
     *
     * @param iocProductAddParam 前端入参
     * @return
     */
    @Override
    @Transactional
    public Boolean updateProduct(IocProductAddParam iocProductAddParam) {
        try {
            IocProduct iocProduct = BeanUtils.convertTo(iocProductAddParam, IocProduct::new);
            if (updateById(iocProduct)) {
                //先查出来旧文件数据
                List<FileModel> fileModels = iFileService.findByTypeAndRelatedId(FileTypeEnum.PRODUCT.getValue(), iocProductAddParam.getId());
                //如果前端传的没有文件数据后台有数据则删除所有数据
                if (CollectionUtils.isEmpty(iocProductAddParam.getFileList())) {
                    if (CollectionUtils.isNotEmpty(fileModels)){
                        List<Long> fileIds = fileModels.stream().map(FileModel::getId).collect(Collectors.toList());
                        iFileService.removeByIds(fileIds);
                    }
                } else {
                    List<Long> existingIds = fileModels.stream().map(FileModel::getId).collect(Collectors.toList());
                    List<File> fileList = iocProductAddParam.getFileList();
                    for (File file : fileList) {
                        if (file.getId() == null) {
                            file.setType(FileTypeEnum.PRODUCT.getValue());
                            file.setRelatedId(iocProduct.getId());
                        }
                    }
                    // 分离出需要新增、删除的文件
                    List<File> toAdd = fileList.stream()
                            .filter(file -> file.getId() == null)
                            .collect(Collectors.toList());

                    List<Long> toDelete = existingIds.stream()
                            .filter(id -> fileList.stream().noneMatch(file -> id.equals(file.getId())))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(toAdd)) {
                        iFileService.addBatch(toAdd);
                    }
                    if (CollectionUtils.isNotEmpty(toDelete)) {
                        iFileService.removeByIds(toDelete);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error("修改失败！", e);
            throw GenericException.fail("修改失败！！");
        }
    }

    /**
     * 分页查询产品信息
     *
     * @param iocProductPageParam 分页和模糊查询参数
     * @return
     */
    @Override
    public IPage<IocProductModel> queryPage(IocProductPageParam iocProductPageParam) {
        try {
            IPage<IocProduct> page = new Page<>(iocProductPageParam.getCurrent(), iocProductPageParam.getSize());
            LambdaQueryWrapper<IocProduct> queryWrapper = new LambdaQueryWrapper<>();
            if (iocProductPageParam.getPdName() != null && !iocProductPageParam.getPdName().isEmpty()) {
                queryWrapper.like(IocProduct::getPdName, iocProductPageParam.getPdName());
            }
            queryWrapper.eq(IocProduct::getTenantId, WebFrameworkUtils.getHeaderTenantId());
            queryWrapper.eq(IocProduct::getDeleted, 1);
            queryWrapper.orderByDesc(IocProduct::getCreateTime);
            IPage<IocProduct> iocProductIPage = iocProductRepository.selectPage(page, queryWrapper);
            return ConvertUtil.pageConvert(iocProductIPage,BeanUtils.convertListTo(iocProductIPage.getRecords(), IocProductModel::new));
        } catch (Exception e) {
            log.error("查询失败！", e);
            throw GenericException.fail("查询失败！！");
        }
    }

    /**
     * 条件查询所有产品信息
     *
     * @param iocProductPageParam 条件查询所有产品信息
     * @return
     */
    @Override
    public List<IocProductModel> queryList(IocProductPageParam iocProductPageParam) {
        try {
            LambdaQueryWrapper<IocProduct> queryWrapper = new LambdaQueryWrapper<>();
            if (iocProductPageParam.getPdName() != null && !iocProductPageParam.getPdName().isEmpty()) {
                queryWrapper.like(IocProduct::getPdName, iocProductPageParam.getPdName());
            }
            if (iocProductPageParam.getDeleted() != null) {
                queryWrapper.eq(IocProduct::getDeleted, iocProductPageParam.getDeleted());
            }
            queryWrapper.eq(IocProduct::getTenantId, WebFrameworkUtils.getHeaderTenantId());
            queryWrapper.orderByDesc(IocProduct::getCreateTime);
            List<IocProduct> iocProducts = iocProductRepository.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(iocProducts)){
                for (IocProduct iocProduct : iocProducts) {
                    if (iocProduct.getDeleted().equals(0)){
                        iocProduct.setPdName(iocProduct.getPdName() + "（已删除）");
                    }
                }
            }
            return BeanUtils.convertListTo(iocProducts, IocProductModel::new);
        } catch (Exception e) {
            log.error("查询失败！", e);
            throw GenericException.fail("查询失败！！");
        }
    }

    /**
     * 根据id删除产品
     *
     * @param id 产品id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
        if (id == null) {
            throw GenericException.fail("产品id不能为空！！");
        }
        IocProduct product = new IocProduct();
        product.setId(id);
        product.setDeleted(0);
        if (updateById(product)) {
            return true;
        } else {
            throw GenericException.fail("产品不存在(后台未找到该产品)");
        }
    }

    /**
     * 获取文件详情
     *
     * @param id
     * @return
     */
    @Override
    public IocProductModel getDetail(Long id) {
        try {
            if (id == null) {
                throw GenericException.fail("产品id不能为空！！");
            }
            IocProduct iocProduct = iocProductRepository.selectById(id);
            IocProductModel iocProductModel = BeanUtils.convertTo(iocProduct, IocProductModel::new);
            List<FileModel> fileModelList = iFileService.findByTypeAndRelatedId(FileTypeEnum.PRODUCT.getValue(), iocProduct.getId());
            iocProductModel.setFileList(fileModelList);
            return iocProductModel;
        } catch (Exception e) {
            log.error("查询失败", e);
            throw GenericException.fail("查询失败！！");
        }
    }
}
