package com.cgnpc.bbxpark.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collections;
import java.util.List;

public class ConvertUtil {
    /**
     * 私有构造函数.
     */
    private ConvertUtil() {

    }

    public static <T> IPage<T> pageEmptyConvert(long page, long size){
        IPage<T> result = new Page<>();
        result.setCurrent((int)page);
        result.setTotal(0);
        result.setSize((int)size);
        result.setRecords(Collections.emptyList());
        return result;
    }

    /**
     * 分页实体数据转换
     * mybatis-plus分页数据转换为亚信的分页实体
     * @param page mybatis-plus分页数据
     * @param <T> 实体类
     * @return 亚信分页数据
     */
    public static <T,S> IPage<T> pageConvert(IPage<S> page,List<T> records){
        IPage<T> result = new Page<>();
        result.setCurrent((int)page.getCurrent());
        result.setTotal((int)page.getTotal());
        result.setSize((int)page.getSize());
        result.setRecords(records);
        return result;
    }



    /**
     * 分页实体数据转换
     * mybatis-plus分页数据转换为的分页实体
     * @param page 页码
     * @param total 总条数
     * @param size 每页条数
     * @param list 数据集合
     * @param <T> 实体类
     * @return 分页数据
     */
    public static <T> IPage<T> pageConvert(long page, long total, long size, List<T> list){
        IPage<T> result = new Page<>();
        result.setCurrent((int)page);
        result.setTotal((int)total);
        result.setSize((int)size);
        result.setRecords(list);
        return result;
    }
}
