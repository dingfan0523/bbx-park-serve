package com.cgnpc.bbxpark.message.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.message.domain.MessageNotice;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticeListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticePageParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageNoticeModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageNoticeRepository extends BaseMapper<MessageNotice> {
    IPage<MessageNoticeModel> findPage(IPage<MessageNoticePageParam> page, @Param("condition") MessageNoticePageParam condition);

    List<MessageNoticeModel> findList(@Param("condition") MessageNoticeListParam condition);
}
