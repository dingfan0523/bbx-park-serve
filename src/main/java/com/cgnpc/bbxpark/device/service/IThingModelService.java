
package com.cgnpc.bbxpark.device.service;


import com.cgnpc.bbxpark.device.domain.ThingModel;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

public interface IThingModelService extends IBaseService<ThingModel> {
    List<ThingModel> listByProductKey(String productKey);
}
