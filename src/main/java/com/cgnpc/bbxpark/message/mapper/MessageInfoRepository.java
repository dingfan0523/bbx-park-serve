package com.cgnpc.bbxpark.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.message.domain.MessageInfo;
import com.cgnpc.bbxpark.message.dto.req.MessageUserPageParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageUserModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageInfoRepository extends BaseMapper<MessageInfo> {
    List<MessageUserModel> findByTypeMessage( @Param("condition") MessageUserPageParam condition);
}
