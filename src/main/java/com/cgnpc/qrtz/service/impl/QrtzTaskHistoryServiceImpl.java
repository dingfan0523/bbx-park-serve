package com.cgnpc.qrtz.service.impl;

import com.cgnpc.qrtz.model.QrtzTaskHistory;
import com.cgnpc.qrtz.mapper.QrtzTaskHistoryMapper;
import com.cgnpc.qrtz.service.IQrtzTaskHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务执行日志 服务实现类
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-20
 */
@Service
public class QrtzTaskHistoryServiceImpl extends ServiceImpl<QrtzTaskHistoryMapper, QrtzTaskHistory> implements IQrtzTaskHistoryService {

}
