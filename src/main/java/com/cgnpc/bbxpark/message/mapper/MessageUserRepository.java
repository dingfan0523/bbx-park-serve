package com.cgnpc.bbxpark.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.message.domain.MessageUser;
import com.cgnpc.bbxpark.message.dto.req.MessageUserListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageUserPageParam;
import com.cgnpc.bbxpark.message.dto.req.UnreadParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageUserModel;
import com.cgnpc.bbxpark.message.dto.resp.UnreadModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageUserRepository extends BaseMapper<MessageUser> {
    IPage<MessageUserModel> pageZy(IPage<MessageUserPageParam> page, @Param("condition") MessageUserPageParam condition);

    IPage<MessageUserModel> findPage(IPage<MessageUserPageParam> page, @Param("condition") MessageUserPageParam condition);

    List<MessageUserModel> findList(@Param("condition") MessageUserListParam condition);

    List<UnreadModel> appUnread(@Param("condition") UnreadParam condition);
}
