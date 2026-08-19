package com.cgnpc.bbxpark.settings.service.impl;

import com.cgnpc.bbxpark.settings.domain.FileLog;
import com.cgnpc.bbxpark.settings.dto.param.FileLogParam;
import com.cgnpc.bbxpark.settings.mapper.FileLogRepository;
import com.cgnpc.bbxpark.settings.service.IFileLogService;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 文件日志服务实现
 */
@Service
public class FileLogServiceImpl extends BaseServiceImpl<FileLogRepository, FileLog> implements IFileLogService {
    @Override
    public Boolean add(FileLogParam param) {
        FileLog fileLog = BeanUtils.convertTo(param,FileLog::new);
        return save(fileLog);
    }
}
