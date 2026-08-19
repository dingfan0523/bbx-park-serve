package com.cgnpc.bbxpark.ioc.service;

import com.cgnpc.bbxpark.ioc.dto.model.StrategicMetricsModel;

public interface IIocCommonService {
    StrategicMetricsModel getStrategicMetrics(String sslcCode);
}
