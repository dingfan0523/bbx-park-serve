package com.cgnpc.qrtz.service;

import com.cgnpc.qrtz.model.LogTimeoutNotify;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 超时通知日志 服务类
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-21
 */
public interface ILogTimeoutNotifyService extends IService<LogTimeoutNotify> {

    /**
      * @title: 获取最新的超时通知集合
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/21 17:27
      * @description:
      * @param: createDate 根据创建时间筛选大于该创建时间的数据
      * @return
      */
    public List<LogTimeoutNotify> findLastest(Date createDate)throws RuntimeException;

}
