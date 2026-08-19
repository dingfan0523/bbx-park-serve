package com.cgnpc.qrtz.service;

import com.cgnpc.qrtz.model.QrtzTaskHistory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 定时任务执行日志 服务类
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-20
 */
public interface IQrtzTaskHistoryService extends IService<QrtzTaskHistory> {
    /** 执行状态：成功*/
    public static final String STATUS_SUCCESS="1";

    /** 执行状态：失败*/
    public static final String STATUS_FAIL="0";
}
