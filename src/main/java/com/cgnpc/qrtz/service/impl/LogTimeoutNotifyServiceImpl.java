package com.cgnpc.qrtz.service.impl;

import com.cgnpc.qrtz.model.LogTimeoutNotify;
import com.cgnpc.qrtz.mapper.LogTimeoutNotifyMapper;
import com.cgnpc.qrtz.service.ILogTimeoutNotifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 超时通知日志 服务实现类
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-21
 */
@Service
public class LogTimeoutNotifyServiceImpl extends ServiceImpl<LogTimeoutNotifyMapper, LogTimeoutNotify> implements ILogTimeoutNotifyService {

    @Resource
    private LogTimeoutNotifyMapper logTimeoutNotifyMapper;

    /**
     * @title: 获取最新的超时通知集合
     * @author: P636016 XIAOJINHUI
     * @date: 2023/9/21 17:27
     * @description:
     * @param: createDate 根据创建时间筛选大于该创建时间的数据
     * @return
     */
    @Override
    public List<LogTimeoutNotify> findLastest(Date createDate) throws RuntimeException {
        //查询半年内的数据
        if(createDate==null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            createDate = calendar.getTime();
        }
        return logTimeoutNotifyMapper.findLastest(createDate);
    }
}
