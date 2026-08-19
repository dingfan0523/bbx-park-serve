
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.domain.RegionSpaceRelation;
import com.cgnpc.bbxpark.settings.dto.model.RegionSpaceRelationModel;
import com.cgnpc.bbxpark.settings.dto.param.RegionSpaceRelationParam;
import com.cgnpc.bbxpark.settings.mapper.RegionSpaceRelationRepository;
import com.cgnpc.bbxpark.settings.service.IRegionSpaceRelationService;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @Description 区域管理员空间关联服务实现
 * @author huangyongtao
 * @date 2025/3/11 11:24
 */
@Service("regionSpaceRelationService")
public class RegionSpaceRelationServiceImpl extends BaseServiceImpl<RegionSpaceRelationRepository, RegionSpaceRelation> implements IRegionSpaceRelationService {

    @Autowired
    private RegionSpaceRelationRepository regionSpaceRelationRepository;

    @Autowired
    private IUserApiService userApiService;

    public RegionSpaceRelationServiceImpl() {
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(RegionSpaceRelationParam param) {
        AssertUtils.notNull(param.getRegionId(), "regionId不能为空");
        //删除所有关联的空间
        this.remove(Wrappers.<RegionSpaceRelation>lambdaQuery().eq(RegionSpaceRelation::getRegionId, param.getRegionId()));
        if (CollectionUtil.isNotEmpty(param.getSpaceIds())) {
            // 将输入参数转换为目标对象列表
            List<RegionSpaceRelation> regionSpaceRelations = param.getSpaceIds().stream()
                    .map(spaceId -> {
                        RegionSpaceRelation relation = new RegionSpaceRelation();
                        relation.setSpaceId(spaceId);
                        relation.setRegionId(param.getRegionId());
                        // 设置其他必要的字段
                        return relation;
                    })
                    .collect(Collectors.toList());
            this.saveBatch(regionSpaceRelations);
        }
        return true;
    }

    @Override
    public List<RegionSpaceRelationModel> list(RegionSpaceRelationParam param) {
        if (ObjectUtil.isEmpty(param.getRegionId())) {
            return Collections.emptyList();
        }
        List<RegionSpaceRelation> relations = this.list(Wrappers.<RegionSpaceRelation>lambdaQuery().eq(RegionSpaceRelation::getRegionId, param.getRegionId()));
        return BeanUtils.convertListTo(relations, RegionSpaceRelationModel::new);
    }

    @Override
    public List<Long> queryAllSpaceIdByLoginUser() {
        String regionStaffid = userApiService.getCurrentStaffNo();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<Long> spaceIds = regionSpaceRelationRepository.queryAllSpaceIdByLoginUser(regionStaffid, tenantId);
        if (CollectionUtil.isNotEmpty(spaceIds)){
            return spaceIds;
        }
        return Collections.emptyList();
    }
}
