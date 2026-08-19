package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.settings.domain.ScreenOverview;
import com.cgnpc.bbxpark.settings.dto.model.ApiScreenOverviewModel;
import com.cgnpc.bbxpark.settings.dto.model.ScreenOverviewModel;
import com.cgnpc.bbxpark.settings.dto.param.ScreenOverviewParam;

import java.util.List;

public interface IScreenOverviewService extends IService<ScreenOverview> {
    List<ScreenOverviewModel> listBy();

    ScreenOverviewModel detail(Long id);

    Boolean add(ScreenOverviewParam param);

    Boolean edit(ScreenOverviewParam param);

    ApiScreenOverviewModel get();
}
