package com.cgnpc.bbxpark.settings.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.base.CustomMessageResultCode;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.settings.domain.ConfigInfo;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.bbxpark.settings.dto.param.ConfigInfoParam;
import com.cgnpc.bbxpark.settings.dto.param.ConfigListParam;
import com.cgnpc.bbxpark.settings.dto.param.ConfigPageParam;
import com.cgnpc.bbxpark.settings.mapper.ConfigInfoRepository;
import com.cgnpc.bbxpark.settings.service.IConfigInfoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfigInfoServiceImpl extends ServiceImpl<ConfigInfoRepository, ConfigInfo> implements IConfigInfoService {
	/**
	 * 注入repository.
	 */
	@Autowired
	private ConfigInfoRepository configInfoRepository;
    @Autowired
    private IUserApiService userApiService;


	/**
	 * 根据系统配置标识获得系统配置详情信息.
	 * @Param [id] 系统配置标识
	 * @Return 系统配置详情信息
	 */
	@Override
	public ConfigInfoModel detail(Long id) {
		ConfigInfo configInfo = getById(id);
        AssertUtils.notNull(configInfo, "数据不存在");
		return BeanUtils.convertTo(configInfo, ConfigInfoModel::new);
	}

	/**
	 * 获取系统配置列表(分页).
	 * @Param param 系统配置查询条件
	 * @Return 系统配置信息列表（分页）
	 */
	@Override
	public IPage<ConfigInfoModel> page(ConfigPageParam param) {
        IPage<ConfigInfo> result = page(new Page<>(param.getCurrent(),param.getSize()),Wrappers.<ConfigInfo>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getName()),ConfigInfo::getName,param.getName())
                .like(StringUtils.isNotEmpty(param.getCode()),ConfigInfo::getCode,param.getCode())
                .eq(param.getType() != null,ConfigInfo::getType,param.getType())
                .eq(ConfigInfo::getDeleted, Delete.NORMAL.getKey())
                .orderByDesc(ConfigInfo::getCreateTime));
        if(CollectionUtils.isEmpty(result.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        List<ConfigInfoModel> list = BeanUtils.convertListTo(result.getRecords(),ConfigInfoModel::new);
        return ConvertUtil.pageConvert(result,list);
	}

	/**
	 * 获取系统配置列表.
	 * @Param param 系统配置查询条件
	 * @Return 系统配置信息列表
	 */
	@SneakyThrows
	@Override
	public List<ConfigInfoModel> list(ConfigListParam param) {
        List<ConfigInfo> result = list(Wrappers.<ConfigInfo>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getName()),ConfigInfo::getName,param.getName())
                .like(StringUtils.isNotEmpty(param.getCode()),ConfigInfo::getCode,param.getCode())
                .eq(param.getType() != null,ConfigInfo::getType,param.getType())
                .eq(ConfigInfo::getDeleted, Delete.NORMAL.getKey())
                .orderByDesc(ConfigInfo::getCreateTime));
        return BeanUtils.convertListTo(result,ConfigInfoModel::new);
	}

	/**
	 * 批量新增系统配置.
	 * @Param params 系统配置信息列表
	 * @Return 批量新增系统配置是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<ConfigInfoParam> params) {
		List<ConfigInfo> configInfoList = BeanUtils.convertListTo(params, ConfigInfo::new);
		for (ConfigInfo configInfo : configInfoList) {
			configInfo.setCreateTime(new Date(System.currentTimeMillis()));
			checkCodeUnique(configInfo);
			checkNameUnique(configInfo);
		}
        return saveBatch(configInfoList);
	}

	/**
	 * 删除系统配置.
	 * @Param id 系统配置标识
	 * @Return 删除系统配置是否成功
	 */
	@Override
	public Boolean remove(Long id) {
        ConfigInfo configInfo = getById(id);
		AssertUtils.notNull(configInfo, SystemResultCode.RESULT_DATA_NONE);
		//系统配置无法删除
		AssertUtils.isTrue(configInfo.getType() != 0, SystemResultCode.CONFIG_SYS_CONFIG_CAN_NOT_DELETE);
        configInfo.setDeleted(Delete.DELETED.getKey());
        return updateById(configInfo);
	}

	/**
	 * 批量删除系统配置.
	 * @Param ids 系统配置标识列表
	 * @Return 批量删除系统配置是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeBatch(List<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
		return true;
	}

	/**
	 * 编辑系统配置信息.
	 * @Param param 系统配置信息
	 * @Return 编辑系统配置是否成功
	 */
	@Override
	public Boolean edit(ConfigInfoParam param) {
		ConfigInfo configDb = getById(param.getId());
		AssertUtils.notNull(configDb, SystemResultCode.RESULT_DATA_NONE);
		ConfigInfo configInfo = BeanUtils.convertTo(param, ConfigInfo::new);
		/**
		 * 校验code唯一
		 */
		if (StringUtils.isNotBlank(configInfo.getCode()) && !configDb.getCode().equals(configInfo.getCode())) {
			checkCodeUnique(configInfo);
		}
		/**
		 * 校验name唯一
		 */
		if (StringUtils.isNotBlank(configInfo.getName()) && !configDb.getName().equals(configInfo.getName())) {
			checkNameUnique(configInfo);
		}

		/**
		 * 保护不可编辑字段
		 */
		configInfo.setCreateTime(null);
		configInfo.setCreatorId(null);
		configInfo.setUpdateTime(new Date(System.currentTimeMillis()));
//		configInfo.setUpdatorId(null);
        return updateById(configInfo);
	}

	private void checkCodeUnique(ConfigInfo configInfo) {
		List<ConfigInfo> codeConfigInfos = list(Wrappers.<ConfigInfo>lambdaQuery()
                .eq(ConfigInfo::getCode,configInfo.getCode())
                .eq(ConfigInfo::getDeleted, Delete.NORMAL.getKey()));
		AssertUtils.isTrue(CollectionUtils.isEmpty(codeConfigInfos),
				new CustomMessageResultCode(SystemResultCode.DATA_ALREADY_EXISTED, "参数编码已存在,请确认")
		);
	}

	private void checkNameUnique(ConfigInfo configInfo) {
		List<ConfigInfo> nameConfigInfos = list(Wrappers.<ConfigInfo>lambdaQuery()
                .eq(ConfigInfo::getName,configInfo.getName())
                .eq(ConfigInfo::getDeleted, Delete.NORMAL.getKey()));
		AssertUtils.isTrue(CollectionUtils.isEmpty(nameConfigInfos),
				new CustomMessageResultCode(SystemResultCode.DATA_ALREADY_EXISTED, "名称已存在")
		);
	}

	/**
	 * 批量启用系统配置信息.
	 * @Param ids 系统配置标识列表
	 * @Return 批量启用系统配置是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean enableBatch(List<Long> ids) {
		List<ConfigInfo> configInfos = ids.stream().map(e -> {
			ConfigInfo configInfo = new ConfigInfo();
			configInfo.setId(e);
			configInfo.setStatus(Status.enabled.getKey());
			return configInfo;
		}).collect(Collectors.toList());
        return updateBatchById(configInfos);
	}

	/**
	 * 批量禁用系统配置信息.
	 * @Param ids 系统配置标识列表
	 * @Return 批量禁用系统配置是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean disableBatch(List<Long> ids) {
		List<ConfigInfo> configInfos = ids.stream().map(e -> {
			ConfigInfo configInfo = new ConfigInfo();
			configInfo.setId(e);
			configInfo.setStatus(Status.disabled.getKey());
			return configInfo;
		}).collect(Collectors.toList());
        return updateBatchById(configInfos);
	}

    @Override
    public ConfigInfoModel getByCodeDetail(String code) {
		List<ConfigInfo> configInfos = list(Wrappers.<ConfigInfo>lambdaQuery()
                .eq(ConfigInfo::getCode,code)
                .eq(ConfigInfo::getDeleted, Delete.NORMAL.getKey())
                .eq(ConfigInfo::getStatus,Status.enabled.getKey()));
		AssertUtils.notEmpty(configInfos, "数据不存在");
		return BeanUtils.convertTo(configInfos.get(0), ConfigInfoModel::new);
    }
}
