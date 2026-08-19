package com.cgnpc.qrtz.service;

import com.cgnpc.qrtz.model.IpmAssAll;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 超时节点视图 服务类
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-20
 */
public interface IIpmAssAllService extends IService<IpmAssAll> {
    /**
     * @title: 提醒节点处理人处理节点任务
     * @author: P636016 XIAOJINHUI
     * @date: 2023/9/20 14:12
     * @description:
     * @param:
     * @return
     */
    public void notifyTodo();

    /**
      * @title: 释放Redis锁
      * @author: P636016 XIAOJINHUI
      * @date: 2023/10/8 9:34
      * @description:
      * @param:
      * @return
      */
    public void releaseLock();
}
