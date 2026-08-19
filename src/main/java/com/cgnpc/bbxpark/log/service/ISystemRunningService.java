package com.cgnpc.bbxpark.log.service;


import com.cgnpc.bbxpark.settings.dto.param.SystemRunningParam;

import java.util.Map;


/**
 * @author EDZ
 */
public interface ISystemRunningService {


    /**
     * 系统运行日志.
     *
     * @param param 查询条件
     * @return 组织列表
     */
    Map<String,Object> selectSystemRunningLog(SystemRunningParam param);

}
