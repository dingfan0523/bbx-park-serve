package com.cgnpc.bbxpark.device.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.device.domain.IocProduct;
import com.cgnpc.bbxpark.device.dto.model.IocProductModel;
import com.cgnpc.bbxpark.device.dto.param.IocProductAddParam;
import com.cgnpc.bbxpark.device.dto.param.IocProductPageParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/24
 * @desc  ioc产品服务接口
 */
public interface IIocProductService extends IBaseService<IocProduct> {
    /**
     * ioc产品新增
     * @param iocProduct 产品数据
     * @return
     */
    Boolean add(IocProductAddParam iocProduct);

    /**
     * 修改产品信息
     * @param iocProduct 前端入参
     * @return
     */
    Boolean updateProduct(IocProductAddParam iocProduct);

    /**
     * 分页查询产品信息
     * @param iocProductPageParam 分页和模糊查询参数
     * @return
     */
    IPage<IocProductModel> queryPage(IocProductPageParam iocProductPageParam);

    /**
     * 条件查询所有产品
     * @param iocProductPageParam
     * @return
     */
    List<IocProductModel> queryList(IocProductPageParam iocProductPageParam);

    /**
     * 根据id删除产品
     * @param id 产品id
     * @return
     */
    Boolean deleteById(Long id);

    /**
     * 获取文件详情
     * @param id 产品id
     * @return
     */
    IocProductModel getDetail(Long id);
}
