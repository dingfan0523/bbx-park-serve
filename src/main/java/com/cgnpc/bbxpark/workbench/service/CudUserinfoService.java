package com.cgnpc.bbxpark.workbench.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.workbench.domain.CudUserinfo;
import com.cgnpc.bbxpark.workbench.dto.CudUserinfoDto;
import com.cgnpc.pro.model.respvo.CudUserInfoVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CudUserinfoService extends IService<CudUserinfo> {

    boolean setTheme(CudUserinfoDto userinfo);

    CudUserinfoDto getTheme(String userId);

    List<Map<String, Object>> getOnlineUsers();

    HashMap<String, Object> getDepartmentInfo(HashMap<String, Object> result, CudUserInfoVO cudUserInfo);

}
