package com.cgnpc.bbxpark.settings.service;

import com.cgnpc.bbxpark.settings.domain.FileLog;
import com.cgnpc.bbxpark.settings.dto.param.FileLogParam;
import com.cgnpc.cud.core.service.IBaseService;


/**
 * 文件日志服务接口
 */
public interface IFileLogService extends IBaseService<FileLog> {
    Boolean add(FileLogParam param);
}
