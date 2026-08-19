
package com.cgnpc.bbxpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.meeting.domain.MeetingService;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingServiceModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingServiceListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingServicePageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingServiceParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingServiceRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingServiceService;
import com.cgnpc.bbxpark.config.eventbus.MeetingServeDelEvent;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/***
 * @Description 会服服务实现
 * @author huangyongtao
 * @date 2024/8/23 15:45
 */
@Service
public class MeetingServiceServiceImpl extends ServiceImpl<MeetingServiceRepository, MeetingService> implements IMeetingServiceService {
    @Resource
    private AsyncEventBus asyncEventBus;

    @Override
    public IPage<MeetingServiceModel> page(MeetingServicePageParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        IPage<MeetingService> page = page(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<MeetingService>lambdaQuery().eq(tenantId != null, MeetingService::getTenantId, tenantId)
                .like(StringUtils.isNotEmpty(param.getName()), MeetingService::getName, param.getName())
                .eq(MeetingService::getDeleted, Delete.NORMAL.getKey()).orderByDesc(MeetingService::getCreateTime));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return ConvertUtil.pageEmptyConvert(page.getCurrent(), page.getSize());
        }
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), BeanUtils.convertListTo(page.getRecords(), MeetingServiceModel::new));
    }

    @Override
    public List<MeetingServiceModel> list(MeetingServiceListParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<MeetingService> list = list(Wrappers.<MeetingService>lambdaQuery().eq(tenantId != null, MeetingService::getTenantId, tenantId)
                .like(StringUtils.isNotEmpty(param.getName()), MeetingService::getName, param.getName())
                .eq(MeetingService::getDeleted, Delete.NORMAL.getKey()).orderByDesc(MeetingService::getCreateTime));
        return BeanUtils.convertListTo(list, MeetingServiceModel::new);
    }

    @Override
    public MeetingServiceModel detail(Long id) {
        MeetingService service = getById(id);
        AssertUtils.notNull(service, SystemResultCode.RESULT_DATA_NONE.message());
        return BeanUtils.convertTo(service, MeetingServiceModel::new);
    }

    @Override
    public MeetingService getByCommon() {
        return getOne(Wrappers.<MeetingService>lambdaQuery().eq(MeetingService::getDeleted, Delete.NORMAL.getKey()).eq(MeetingService::getCommon, Status.enabled.getKey()).eq(MeetingService::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
    }

    @Override
    public Boolean add(MeetingServiceParam param) {
        //会服名称唯一性校验
        //AssertUtils.isFalse(verifyName(param.getId(), param.getName()), "会服名称重复");
        MeetingService service = BeanUtils.convertTo(param, MeetingService::new);
        //默认字段
        service.setId(null);
        service.setDeleted(Delete.NORMAL.getKey());
        return save(service);
    }

    @Override
    public Boolean edit(MeetingServiceParam param) {
        //会服名称唯一性校验
        //AssertUtils.isFalse(verifyName(param.getId(), param.getName()), "会服名称重复");
        MeetingService service = getById(param.getId());
        AssertUtils.notNull(service, SystemResultCode.RESULT_DATA_NONE.message());
        param.setId(service.getId());
        BeanUtils.copyProperties(param, service);
        return updateById(service);
    }

    @Override
    public Boolean remove(Long id) {
        MeetingService service = getById(id);
        AssertUtils.notNull(service, SystemResultCode.RESULT_DATA_NONE.message());
        service.setDeleted(Delete.DELETED.getKey());
        if (updateById(service)) {
            MeetingServeDelEvent event = new MeetingServeDelEvent();
            event.setServiceId(id);
            asyncEventBus.post(event);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean verifyName(Long id, String name) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        return count(Wrappers.<MeetingService>lambdaQuery().eq(MeetingService::getName, name)
                .eq(MeetingService::getDeleted, Delete.NORMAL.getKey())
                .eq(tenantId != null, MeetingService::getTenantId, tenantId)
                //如果id不为空,那么说明是编辑,需排除自身
                .ne(id != null, MeetingService::getId, id)) > 0;
    }
}
