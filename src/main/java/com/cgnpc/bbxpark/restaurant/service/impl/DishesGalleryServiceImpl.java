
package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.restaurant.domain.DishesGallery;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesGalleryModel;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesGalleryListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesGalleryPageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesGalleryParam;
import com.cgnpc.bbxpark.restaurant.mapper.DishesGalleryRepository;
import com.cgnpc.bbxpark.restaurant.service.IDishesGalleryService;
import com.cgnpc.bbxpark.config.minio.configure.service.FileCenterService;
import com.cgnpc.bbxpark.config.minio.model.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DishesGalleryServiceImpl extends ServiceImpl<DishesGalleryRepository, DishesGallery> implements IDishesGalleryService {

    @Autowired
    private FileCenterService fileCenterService;


    @Override
    public boolean importDishes(List<FileModel> fileModels) {
        addOrUpdateDishImages(fileModels);
        return true;
    }

    @Override
    public Boolean add(DishesGalleryParam param) {
        validate(param);
        DishesGallery dishesGallery = BeanUtils.convertTo(param, DishesGallery::new);
        return saveOrUpdate(dishesGallery);
    }

    @Override
    public IPage<DishesGalleryModel> pageDishesGalleryModel(DishesGalleryPageParam param) {
        Long headerTenantId = WebFrameworkUtils.getHeaderTenantId();
        param.setTenantId(headerTenantId);
        IPage<DishesGallery> result = page(new Page<>(param.getCurrent(), param.getSize()), queryWrapper(param));
        return ConvertUtil.pageConvert(result,BeanUtils.convertListTo(result.getRecords(), DishesGalleryModel::new));
    }

    @Override
    public List<DishesGalleryModel> dishesGalleryModel(DishesGalleryListParam param) {
        DishesGalleryPageParam dishesGalleryPageParam = BeanUtils.convertTo(param, DishesGalleryPageParam::new);
        dishesGalleryPageParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<DishesGallery> galleries = list(queryWrapper(dishesGalleryPageParam));
        return BeanUtils.convertListTo(galleries, DishesGalleryModel::new);
    }

    @Override
    public Boolean removeDishesGallery(Long id) {
        return removeById(id);
    }

    private LambdaQueryWrapper<DishesGallery> queryWrapper(DishesGalleryPageParam param) {
        LambdaQueryWrapper<DishesGallery> queryWrapper = Wrappers.lambdaQuery();
        Long tenantId = param.getTenantId();
        queryWrapper.eq(ObjectUtil.isNotEmpty(tenantId),DishesGallery::getTenantId, tenantId);
        String name = param.getName();
        queryWrapper.like(ObjectUtil.isNotEmpty(name),DishesGallery::getName, name);
        queryWrapper.orderByDesc(DishesGallery::getCreateTime);
        return queryWrapper;
    }

    /**
     * 校验图片是否已存在
     * @param param 菜品库参数
     */
    private void validate(DishesGalleryParam param){
        List<DishesGallery> list = list(new LambdaQueryWrapper<DishesGallery>().eq(DishesGallery::getName, param.getName()));
        AssertUtils.isTrue(list.isEmpty(), "该菜品图片已存在，请重试");
    }

    /**
     * 新增或更新菜品图片
     * @param fileModels 菜品图片文件列表
     */
    private void addOrUpdateDishImages(List<FileModel> fileModels) {
        List<String> fileNames = fileModels.stream().map(FileModel::getFullPath).collect(Collectors.toList());
        Long headerTenantId = WebFrameworkUtils.getHeaderTenantId();
        List<DishesGallery> dishesGalleries = baseMapper.selectList(
                new LambdaQueryWrapper<DishesGallery>().in(DishesGallery::getName, fileNames)
                        .eq(ObjectUtil.isNotEmpty(headerTenantId),DishesGallery::getTenantId,headerTenantId)
        );
        // 新增菜品图片，fileModels中FileModel.getFullPath如果等于DishesGallery.getName，则更新DishesGallery.picUrl否则就是新增
        fileModels.forEach(fileModel -> {
            String fullPath = fileModel.getFullPath();
            Optional<DishesGallery> optionalDishesGallery = dishesGalleries.stream()
                    .filter(dishesGallery -> dishesGallery.getName().equals(fullPath))
                    .findFirst();
            if (optionalDishesGallery.isPresent()) {
                DishesGallery dishesGallery = optionalDishesGallery.get();
                dishesGallery.setImageUrl(fileModel.getUrl());
                baseMapper.updateById(dishesGallery);
            } else {
                DishesGallery newDishesGallery = new DishesGallery();
                newDishesGallery.setName(fullPath);
                newDishesGallery.setImageUrl(fileModel.getUrl());
                baseMapper.insert(newDishesGallery);
            }
        });
    }
}
