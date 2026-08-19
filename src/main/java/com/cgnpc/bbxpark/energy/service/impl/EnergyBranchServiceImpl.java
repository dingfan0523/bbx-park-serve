package com.cgnpc.bbxpark.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.energy.domain.BranchDevice;
import com.cgnpc.bbxpark.energy.domain.EnergyBranch;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchParam;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchQueryParam;
import com.cgnpc.bbxpark.energy.mapper.BranchDeviceRepository;
import com.cgnpc.bbxpark.energy.mapper.EnergyBranchRepository;
import com.cgnpc.bbxpark.energy.service.IEnergyBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 能源支路服务实现类
 */
@Service
public class EnergyBranchServiceImpl extends ServiceImpl<EnergyBranchRepository, EnergyBranch> implements IEnergyBranchService {
    @Autowired
    private  EnergyBranchRepository energyBranchRepository;
    @Autowired
    private BranchDeviceRepository branchDeviceRepository;

    @Override
    public Boolean insert(EnergyBranchParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        String userId = WebFrameworkUtils.getHeaderUserId();
        // 校验支路编码唯一性
        LambdaQueryWrapper<EnergyBranch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnergyBranch::getBranchCode, param.getBranchCode());
        queryWrapper.eq(EnergyBranch::getTenantId, tenantId);
        queryWrapper.eq(EnergyBranch::getDeleted, Status.enabled.getKey());
        AssertUtils.isFalse(this.count(queryWrapper) > 0,"支路编码已存在，请重新输入");

        // 构造实体对象并填充新增人和新增时间
        EnergyBranch energyBranch = BeanUtils.convertTo(param, EnergyBranch::new);
        energyBranch.setCreateTime(new Date());
        energyBranch.setCreatorId(userId);

        // 执行插入操作
        this.save(energyBranch);
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateById(EnergyBranchParam param) {
        AssertUtils.isFalse(param.getId() == null,"id不能为空");
        EnergyBranch oldEnergyBranch = energyBranchRepository.selectById(param.getId());
        AssertUtils.isFalse(oldEnergyBranch == null,"支路不存在");
        AssertUtils.isFalse(Delete.DELETED.getKey().equals(oldEnergyBranch.getDeleted()),"支路已被删除");
        // 校验支路编码唯一性
        LambdaQueryWrapper<EnergyBranch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnergyBranch::getBranchCode, param.getBranchCode());
        //全局唯一
//        queryWrapper.eq(EnergyBranch::getTenantId, WebFrameworkUtils.getHeaderTenantId());
        queryWrapper.ne(EnergyBranch::getId, param.getId());
        queryWrapper.eq(EnergyBranch::getDeleted, Status.enabled.getKey());
        AssertUtils.isFalse(this.count(queryWrapper) > 0,"支路编码已存在，请重新输入");
        EnergyBranch energyBranch = BeanUtils.convertTo(param, EnergyBranch::new);
        energyBranch.setUpdatorId(WebFrameworkUtils.getHeaderUserId());
        energyBranch.setUpdateTime(new Date());
        return this.updateById(energyBranch);
    }

    @Override
    public Boolean delete(Long id) {
        if (id == null)
            throw GenericException.fail("id不能为空");
        // 递归删除所有下级支路
        //删除支路后，删除支路下的设备数据
        deleteBranchAndChildren(id);
        return true;
    }
    public List<EnergyBranchModel> findList(EnergyBranchQueryParam param) {

        LambdaQueryWrapper<EnergyBranch> queryWrapper = new LambdaQueryWrapper<>();
        if (param.getDeleted() == null){
            queryWrapper.eq(EnergyBranch::getDeleted, Status.enabled.getKey());
        }else {
            queryWrapper.eq(EnergyBranch::getDeleted, param.getDeleted());
        }
        if (param.getBranchName() != null && !param.getBranchName().isEmpty()) {
            queryWrapper.like(EnergyBranch::getBranchName, param.getBranchName());
        }
        if (param.getBranchCode() != null && !param.getBranchCode().isEmpty()) {
            queryWrapper.like(EnergyBranch::getBranchCode, param.getBranchCode());
        }
        if (param.getBranchType() != null) {
            queryWrapper.eq(EnergyBranch::getBranchType, param.getBranchType());
        }
        if (param.getStatus() != null) {
            queryWrapper.eq(EnergyBranch::getStatus, param.getStatus());
        }
        queryWrapper.eq(WebFrameworkUtils.getHeaderTenantId()!= null ,EnergyBranch::getTenantId, WebFrameworkUtils.getHeaderTenantId());
        queryWrapper.orderByDesc(EnergyBranch::getCreateTime);
        List< EnergyBranch> energyBranches = this.list( queryWrapper);
        return BeanUtils.convertListTo(energyBranches, EnergyBranchModel::new);
    }


    @Override
    public List<EnergyBranchModel> findTreeList(EnergyBranchQueryParam param) {
        List<EnergyBranchModel> energyBranchModels = buildQuery(param);

        if (StringUtils.isNotEmpty(param.getBranchName()) || StringUtils.isNotEmpty(param.getBranchCode()) || param.getStatus() != null){
            return energyBranchModels;
        }
        // 将查询结果转换为IocProductModel列表并设置到结果对象
        if (CollectionUtils.isNotEmpty(energyBranchModels)){
            return buildTree(energyBranchModels);
        }else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<EnergyBranchModel> findEnablerTree(EnergyBranchQueryParam param) {
        param.setStatus(Status.enabled.getKey());
        List<EnergyBranchModel> energyBranchModels = buildQuery(param);
        // 将查询结果转换为IocProductModel列表并设置到结果对象
        if (CollectionUtils.isNotEmpty(energyBranchModels)){
            return buildTree(energyBranchModels);
        }else {
            return new ArrayList<>();
        }
    }

    @Override
    public  List<EnergyBranchModel> buildQuery(EnergyBranchQueryParam param){
        LambdaQueryWrapper<EnergyBranch> queryWrapper = new LambdaQueryWrapper<>();
        if (param.getDeleted() == null){
            queryWrapper.eq(EnergyBranch::getDeleted, Status.enabled.getKey());
        }else {
            queryWrapper.eq(EnergyBranch::getDeleted, param.getDeleted());
        }
        if (param.getBranchName() != null && !param.getBranchName().isEmpty()) {
            queryWrapper.like(EnergyBranch::getBranchName, param.getBranchName());
        }
        if (param.getBranchCode() != null && !param.getBranchCode().isEmpty()) {
            queryWrapper.like(EnergyBranch::getBranchCode, param.getBranchCode());
        }
        if (param.getBranchType() != null) {
            queryWrapper.eq(EnergyBranch::getBranchType, param.getBranchType());
        }
        if (param.getStatus() != null) {
            queryWrapper.eq(EnergyBranch::getStatus, param.getStatus());
        }
        queryWrapper.eq(WebFrameworkUtils.getHeaderTenantId()!= null ,EnergyBranch::getTenantId, WebFrameworkUtils.getHeaderTenantId());
        queryWrapper.orderByAsc(EnergyBranch::getCreateTime);
        List< EnergyBranch> energyBranches = this.list( queryWrapper);
        List<EnergyBranchModel> energyBranchModels = BeanUtils.convertListTo(energyBranches, EnergyBranchModel::new);
        return energyBranchModels;
    }

    public static List<EnergyBranchModel> buildTree(List<EnergyBranchModel> branchModels) {
        Map<Long, EnergyBranchModel> map = new HashMap<>();
        List<EnergyBranchModel> rootNodes = new ArrayList<>();

        // 将所有节点放入map中，key为id，value为节点对象
        for (EnergyBranchModel branch : branchModels) {
            map.put(branch.getId(), branch);
        }

        // 构建树结构
        for (EnergyBranchModel branch : branchModels) {
            if (branch.getParentId() == null) {
                rootNodes.add(branch);
            } else {
                EnergyBranchModel parent = map.get(branch.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(branch);
                }
            }
        }

        // 对根节点列表进行排序
        rootNodes.sort(Comparator.comparingInt(EnergyBranchModel::getSortOrder));

        // 递归对每个节点的子节点列表进行排序
        for (EnergyBranchModel root : rootNodes) {
            sortChildren(root);
        }

        return rootNodes;
    }

    /**
     *  给树节点进行排序
     * @param node
     */
    private static void sortChildren(EnergyBranchModel node) {
        if (node.getChildren() != null) {
            node.getChildren().sort(Comparator.comparingInt(EnergyBranchModel::getSortOrder));
            for (EnergyBranchModel child : node.getChildren()) {
                sortChildren(child);
            }
        }
    }

    @Override
    public Boolean updateStatus(EnergyBranchParam param) {
        if (param.getId() == null)
            throw GenericException.fail("id不能为空");
        if (param.getStatus() == null)
            throw GenericException.fail("启用状态不能为空");
        EnergyBranch oldEnergyBranch = energyBranchRepository.selectById(param.getId());
        if (oldEnergyBranch == null)
            throw GenericException.fail("支路不存在");
        if (Status.disabled.getKey().equals(oldEnergyBranch.getDeleted()))
            throw GenericException.fail("支路已被删除");
        EnergyBranch energyBranch = new EnergyBranch();
        energyBranch.setId(param.getId());
        energyBranch.setStatus(param.getStatus());
        energyBranch.setUpdatorId(WebFrameworkUtils.getHeaderUserId());
        energyBranch.setUpdateTime(new Date());
        return this.updateById(energyBranch);
    }

    private void deleteBranchAndChildren(Long id) {
        // 删除当前支路
        UpdateWrapper<EnergyBranch> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("deleted", Status.disabled.getKey());
        this.update(updateWrapper);
        //删除支路下设备
        branchDeviceRepository.delete(new QueryWrapper<BranchDevice>().eq("branch_id", id));

        // 查找所有下级支路
        LambdaQueryWrapper<EnergyBranch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnergyBranch::getParentId, id);
        queryWrapper.eq(EnergyBranch::getDeleted, Status.enabled.getKey());
        List<EnergyBranch> children = this.list(queryWrapper);

        // 递归删除每个下级支路及其子支路
        for (EnergyBranch child : children) {
            deleteBranchAndChildren(child.getId());
        }
    }


}