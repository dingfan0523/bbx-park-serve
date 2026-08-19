
package com.cgnpc.bbxpark.device.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.device.domain.ThingModel;
import com.cgnpc.bbxpark.device.mapper.ThingModelRepository;
import com.cgnpc.bbxpark.device.service.IThingModelService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author 54766
 */
@Service
public class ThingModelServiceImpl extends BaseServiceImpl<ThingModelRepository, ThingModel> implements IThingModelService {

    @Override
    public List<ThingModel> listByProductKey(String productKey) {
        return list(Wrappers.<ThingModel>lambdaQuery().eq(ThingModel::getProductKey, productKey));
    }
}
