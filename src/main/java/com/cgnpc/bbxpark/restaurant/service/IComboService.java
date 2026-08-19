package com.cgnpc.bbxpark.restaurant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.Combo;
import com.cgnpc.bbxpark.restaurant.dto.model.ComboModel;
import com.cgnpc.bbxpark.restaurant.dto.param.ComboListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.ComboParam;

import java.util.List;

/**
 * <p>
 * 套餐服务接口
 * </p>
 *
 * @author gujun
 * @time 2024-07-22
 */
public interface IComboService extends IService<Combo> {
    /**
     * 查询套餐
     * @param id
     * @return
     */
    ComboModel getComboModel(Integer id);

    /**
     * 分页查询
     * @param param
     * @return
     */
    IPage<ComboModel> pageResult(ComboListParam param);

    /**
     * 创建
     * @param param
     * @return
     */
    Long saveOrUpdate(ComboParam param);

    List<ComboModel> list(ComboListParam param);

    Boolean remove(Long id);

}
