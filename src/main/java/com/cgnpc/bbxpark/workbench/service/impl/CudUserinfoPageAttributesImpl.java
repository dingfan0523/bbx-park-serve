package com.cgnpc.bbxpark.workbench.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.workbench.domain.CudUserinfoPageAttributes;
import com.cgnpc.bbxpark.workbench.mapper.CudUserinfoPageAttributesMapper;
import com.cgnpc.bbxpark.workbench.service.CudUserinfoPageAttributesService;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用途说明: 保存用户个性化设置-自定义字段
 * 作者姓名: P633860
 * 创建时间: 2024/7/8
 */

@Service
public class CudUserinfoPageAttributesImpl extends ServiceImpl<CudUserinfoPageAttributesMapper, CudUserinfoPageAttributes> implements CudUserinfoPageAttributesService {

    @Override
    public boolean setAttr(CudUserinfoPageAttributes attr) {
        LambdaQueryWrapper<CudUserinfoPageAttributes> query = new LambdaQueryWrapper<>();
        query.eq(CudUserinfoPageAttributes::getUserId, attr.getUserId());
        query.eq(CudUserinfoPageAttributes::getQueryId, attr.getQueryId());
        if (CollUtil.isNotEmpty(list(query))){
            remove(query);
            return setAttr(attr);
        } else {
            attr.setCreateDate(new Date());
            List<CudUserinfoPageAttributes> listQueryFields = Lists.newArrayList();
            if (attr.getQueryFields() != null && CollUtil.isNotEmpty(attr.getQueryFields())){
                for (String str : attr.getQueryFields()) {
                    CudUserinfoPageAttributes attributes = new CudUserinfoPageAttributes();
                    BeanUtils.copyProperties(attr,attributes);
                    attributes.setJsonFields(str);
                    listQueryFields.add(attributes);
                }
            }
            return saveBatch(listQueryFields);
        }
    }

    @Override
    public CudUserinfoPageAttributes getAttr(String userId, String queryId) {
        LambdaQueryWrapper<CudUserinfoPageAttributes> query = new LambdaQueryWrapper<>();
        query.eq(CudUserinfoPageAttributes::getUserId, userId);
        if (StrUtil.isNotBlank(queryId)){
            query.eq(CudUserinfoPageAttributes::getQueryId, queryId);
        }
        List<CudUserinfoPageAttributes> list = list(query);
        CudUserinfoPageAttributes attributes = new CudUserinfoPageAttributes();
        if (CollUtil.isNotEmpty(list)){
            List<String> queryFields = Lists.newArrayList();
            for (CudUserinfoPageAttributes attr: list) {
                queryFields.add(attr.getJsonFields());
            }
            attributes.setIsFixed(list.get(0).getIsFixed());
            attributes.setQueryId(list.get(0).getQueryId());
            attributes.setQueryFields(queryFields);
        }
        return attributes;
    }


    @Override
    public List<CudUserinfoPageAttributes> getAttrList(String userId) {
        List<CudUserinfoPageAttributes> result = CollUtil.newArrayList();
        LambdaQueryWrapper<CudUserinfoPageAttributes> query = Wrappers.lambdaQuery();
        query.eq(CudUserinfoPageAttributes::getUserId, userId);
        List<CudUserinfoPageAttributes> list = list(query);
        if (CollUtil.isNotEmpty(list)){
            Map<String, List<String>> map = Maps.newHashMap();
            Map<String, CudUserinfoPageAttributes> objMap = Maps.newHashMap();
            for (CudUserinfoPageAttributes attr : list) {
                String queryId = attr.getQueryId();
                List<String> queryFields = map.get(queryId);
                if (queryFields != null) {
                    queryFields.add(attr.getJsonFields());
                    map.put(queryId, queryFields);
                } else {
                    map.put(queryId, new ArrayList<>(Arrays.asList(attr.getJsonFields())));
                    CudUserinfoPageAttributes attributes = new CudUserinfoPageAttributes();
                    BeanUtils.copyProperties(attr,attributes);
                    attributes.setJsonFields(null);
                    objMap.put(queryId,attributes);
                }
            }
            objMap.forEach((key, attributes) -> {
                attributes.setQueryFields(map.get(key));
                result.add(attributes);
            });
        }
        return result;
    }
}
