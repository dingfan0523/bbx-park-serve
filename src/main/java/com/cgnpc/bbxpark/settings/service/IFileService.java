package com.cgnpc.bbxpark.settings.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FilePageParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;



public interface IFileService extends IBaseService<File> {
    IPage<FileModel> findByTypeIn(FilePageParam param);

    /**
     * 根据类型和关联id查询
     */
    List<FileModel> findByTypeAndRelatedId(Integer type, Long relatedId);

    List<FileModel> findByTypeAndRelatedIds(Integer type, List<Long> relatedIds);

    IPage<FileModel> pageBy(Integer type, Long relatedId, Integer page, Integer limit);

    /**
     * 新增文件.
     */
    Boolean add(File file);

    /**
     * 删除附件
     * @param id id
     * @return 结果
     */
    Boolean remove(Long id);

    /**
     * 批量删除附件
     * @param relatedId
     * @return 结果
     */
    Boolean removeByRelatedId(Integer type, Long relatedId);

    /**
     * 批量删除附件
     * @param relatedIds
     * @return 结果
     */
    Boolean removeByRelatedIds(Integer type, List<Long> relatedIds);

    /***
     * @Description 批量新增文件
     * @author huangyongtao
     * @date 2024/12/24 15:23
     * @param files
     */
    Boolean addBatch(List<File> files);

    /**
     * 校验文件名是否重复
     * @param name 名称
     * @param type 类型
     * @return true->已存在;false->不存在
     */
    Boolean exist(String name,Integer type);
}
