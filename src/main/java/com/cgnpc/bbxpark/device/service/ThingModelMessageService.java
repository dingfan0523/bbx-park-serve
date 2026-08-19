package com.cgnpc.bbxpark.device.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.device.domain.ThingModelMessage;
import com.cgnpc.bbxpark.device.dto.model.ThingModelMessageModel;
import com.cgnpc.bbxpark.device.dto.param.ThingModelMessageParam;

import java.util.List;


public interface ThingModelMessageService {

    IPage<ThingModelMessage> selectPage(ThingModelMessageParam dto);


    ThingModelMessageModel findById(String id);


    int save();

    List<ThingModelMessageModel> search();
}
