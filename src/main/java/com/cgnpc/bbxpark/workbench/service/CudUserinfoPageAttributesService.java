package com.cgnpc.bbxpark.workbench.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.workbench.domain.CudUserinfoPageAttributes;

import java.util.List;

public interface CudUserinfoPageAttributesService extends IService<CudUserinfoPageAttributes> {

    boolean setAttr(CudUserinfoPageAttributes attr);

    CudUserinfoPageAttributes getAttr(String userId, String queryId);

    List<CudUserinfoPageAttributes> getAttrList(String userId);

}
