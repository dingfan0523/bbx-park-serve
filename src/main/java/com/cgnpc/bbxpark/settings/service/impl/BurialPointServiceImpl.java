
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.settings.domain.BurialPoint;
import com.cgnpc.bbxpark.settings.dto.param.BurialPointParam;
import com.cgnpc.bbxpark.settings.mapper.BurialPointRepository;
import com.cgnpc.bbxpark.settings.service.IBurialPointService;
import com.cgnpc.bbxpark.common.enums.EventTypeEnum;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BurialPointServiceImpl extends ServiceImpl<BurialPointRepository, BurialPoint> implements IBurialPointService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean add(BurialPointParam param) {
        BurialPoint burialPoint = BeanUtils.convertTo(param, BurialPoint::new);
        burialPoint.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        burialPoint.setId(null);
        burialPoint.setTriggerTime(DateUtil.date());
        burialPoint.setEventType(EventTypeEnum.CLICK.getCode());
        return save(burialPoint);
    }

}
