package com.cgnpc.bbxpark.settings.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.settings.domain.File;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface FileRepository extends BaseMapper<File> {
}
