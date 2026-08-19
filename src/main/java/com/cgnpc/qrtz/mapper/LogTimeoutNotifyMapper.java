package com.cgnpc.qrtz.mapper;

import com.cgnpc.qrtz.model.LogTimeoutNotify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 超时通知日志 Mapper 接口
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-21
 */
public interface LogTimeoutNotifyMapper extends BaseMapper<LogTimeoutNotify> {
    /**
      * @title: 查找最新执行成功的日志列表
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/21 17:11
      * @description:
      * @param: createDate 创建时间
      * @return
      */
    List<LogTimeoutNotify> findLastest(@Param("notifyDate")Date notifyDate);
}
