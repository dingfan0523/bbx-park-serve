package com.cgnpc.bbxpark.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotDeviceState;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotThingModel;
import com.cgnpc.bbxpark.acl.iot.service.IotCapacityService;
import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.AlarmInfoConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.*;
import com.cgnpc.bbxpark.device.dto.model.*;
import com.cgnpc.bbxpark.device.dto.param.*;
import com.cgnpc.bbxpark.device.mapper.DeviceGroupRelRepository;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.device.service.*;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.dto.model.DepartmentInfoModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceListParam;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberListParam;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ITenantMemberService;
import com.cgnpc.bbxpark.supplier.domain.Supplier;
import com.cgnpc.bbxpark.supplier.domain.SupplierPerson;
import com.cgnpc.bbxpark.supplier.service.ISupplierPersonService;
import com.cgnpc.bbxpark.supplier.service.ISupplierService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/***
 * @Description ioc设备服务实现
 * @author huangyongtao
 * @date 2025/2/21 17:21
 */
@Service
@Slf4j
public class IocDeviceServiceImpl extends BaseServiceImpl<IocDeviceRepository, IocDevice> implements IIocDeviceService {
    @Autowired
    private ITenantMemberService tenantMemberService;
	@Autowired
	private IParkSpaceService parkSpaceService;
	@Autowired
	private IFileService fileService;
	@Autowired
    private IDeviceInfoService deviceInfoService;
	@Autowired
	private IIocProductService iocProductService;
	@Autowired
	private IDeviceGroupService deviceGroupService;
	@Autowired
	private IDeviceLabelService deviceLabelService;
	@Autowired
	private IIotDeviceRelationService iotDeviceRelationService;
	@Autowired
	private IAlarmInfoService alarmInfoService;
	@Autowired
	private IIocDeviceUpdateService iocDeviceUpdateService;
	@Autowired
	private DeviceGroupRelRepository deviceGroupRelRepository;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IRoleApiService roleApiService;
    @Autowired
    private IDepartmentApiService departmentApiService;
    @Autowired
    private IotCapacityService iotCapacityService;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private ISupplierPersonService supplierPersonService;
	@Autowired
	@Qualifier("kafkaMsgThreadPool")
	private Executor executorService;

	@Value("${parent.department.id:50259024}")
	private String departmentId;
    @Value("${bbx.role.system:}")
    private String systemRoleCode;
    @Value("${bbx.device.group.af:ZHAFJK}")
    private String afDeviceGroup;

	@Override
	public IPage<IocDeviceModel> pageDevice(IocDevicePageParam param) {
		//处理园区管理员角色权限
		buildRole(param);
		IPage<IocDevice> page =  this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(page.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<IocDeviceModel> iocDeviceModels = BeanUtils.convertListTo(page.getRecords(), IocDeviceModel::new);
		//处理空间名称和产品名称
		handleNames(iocDeviceModels);
		return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), iocDeviceModels);
	}

	@Override
	public List<IocDeviceModel> findListByJob(IocDeviceListParam param) {
		IocDevicePageParam listParam = new IocDevicePageParam();
		BeanUtils.copyProperties(param, listParam);
		listParam.setAuth(false);
		List<IocDevice> devices = this.list(buildQuery(listParam));
		if (CollectionUtil.isEmpty(devices)) {
			return Collections.emptyList();
		}
		List<IocDeviceModel> iocDeviceModels =  BeanUtils.convertListTo(devices, IocDeviceModel::new);
		//处理空间名称和产品名称
		handleNames(iocDeviceModels);
		return iocDeviceModels;
	}

	@Override
	public List<IocDeviceModel> findList(IocDeviceListParam param) {
		IocDevicePageParam listParam = new IocDevicePageParam();
		BeanUtils.copyProperties(param, listParam);
		buildRole(listParam);
		List<IocDevice> devices = this.list(buildQuery(listParam));
		if (CollectionUtil.isEmpty(devices)) {
			return Collections.emptyList();
		}
		List<IocDeviceModel> iocDeviceModels =  BeanUtils.convertListTo(devices, IocDeviceModel::new);
		//处理空间名称和产品名称
		handleNames(iocDeviceModels);
		return iocDeviceModels;
	}

	@Override
	public Boolean bindDeviceSpace(IocDeviceBindingParam param) {
		AssertUtils.isNotEmpty(param.getIdList(), "设备id集合不能为空");
		AssertUtils.notNull(param.getSpaceId(), "空间不能为空");
		return update(buildUpdate(param));
	}

	@Override
	public Boolean bindDeviceProduct(IocDeviceBindingParam param) {
		AssertUtils.isNotEmpty(param.getIdList(), "设备id集合不能为空");
		AssertUtils.notNull(param.getProductId(), "产品不能为空");
		return update(buildUpdate(param));
	}

	@Override
	public Boolean bindDeviceDepartment(IocDeviceBindingParam param) {
		AssertUtils.isNotEmpty(param.getIdList(), "设备id集合不能为空");
		AssertUtils.notNull(param.getDepartmentId(), "部门不能为空");
		AssertUtils.notNull(param.getDepartmentName(), "部门不能为空");

		this.update(Wrappers.<IocDevice>lambdaUpdate()
				.set(IocDevice::getDepartmentId, param.getDepartmentId())
				.set(IocDevice::getDepartmentName, param.getDepartmentName())
				.in(IocDevice::getId, param.getIdList()));
		return update(buildUpdate(param));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean deviceEnable(IocDeviceStatusParam param) {
		AssertUtils.notNull(param.getStatus(), "设备状态不能为空");
		IocDevice iocDevice = this.getById(param.getId());
		validateDeviceExists(iocDevice);
		iocDevice.setEnableStatus(param.getStatus());
		this.updateById(iocDevice);
		//处理告警信息
		handleAlarm(param.getId(), param.getStatus(), null, AlarmInfoConstant.ALARM_END_TYPE_7);
		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean deviceOnline(IocDeviceStatusParam param) {
		AssertUtils.notNull(param.getStatus(), "设备状态不能为空");
		IocDevice iocDevice = this.getById(param.getId());
		validateDeviceExists(iocDevice);
		iocDevice.setOnlineStatus(param.getStatus());
		iocDevice.setOnlineRemark(param.getOnlineRemark());
		this.updateById(iocDevice);
		//处理告警信息
		handleAlarm(param.getId(), param.getStatus(), param.getOnlineRemark(), AlarmInfoConstant.ALARM_END_TYPE_2);
		return true;
	}

	@Override
	public Boolean checkDeviceComplex(IocDeviceParam param) {
		long count = this.count(Wrappers.<IocDevice>lambdaQuery().eq(IocDevice::getDeviceComplexId, param.getId()).eq(IocDevice::getDeleted, Delete.NORMAL.getKey()));
		if(DeviceTypeEnum.SIMPLE.getCode().equals(param.getDeviceType())){
			AssertUtils.isFalse(count > 0, "该设备已被其他设备选为母设备，不可再被选为单体设备");
		}
		if(DeviceComplexTypeEnum.SON.getCode().equals(param.getDeviceComplexType())){
			AssertUtils.isFalse(count > 0, "该设备已被其他设备选为母设备，不可再被选为子设备");
		}
		return true;
	}

	/**
	 * 根据ioc设备标识获得ioc设备详情信息.
	 * @Param [id] ioc设备标识
	 * @Return ioc设备详情信息
	 */
	@Override
	public IocDeviceModel detail(Long id) {
		IocDevice iocDevice = this.getById(id);
		validateDeviceExists(iocDevice);
		IocDeviceModel iocDeviceModel = BeanUtils.convertTo(iocDevice, IocDeviceModel::new);
		//处理空间名称和产品名称
		CompletableFuture<Void> nameFuture = CompletableFuture.runAsync(() -> handleNames(Arrays.asList(iocDeviceModel)), executorService);
		//处理母子设备
		CompletableFuture<Void> complexFuture = CompletableFuture.runAsync(() -> handleDeviceComplex(iocDeviceModel), executorService);
		//处理设备标签
		CompletableFuture<Void> labelFuture = CompletableFuture.runAsync(() -> handleDeviceLabels(iocDeviceModel), executorService);
		//处理设备分组
		CompletableFuture<Void> groupFuture = CompletableFuture.runAsync(() -> handleDeviceGroups(iocDeviceModel), executorService);
		//处理产品
		CompletableFuture<Void> productFuture = CompletableFuture.runAsync(() -> handleProduct(iocDeviceModel), executorService);
		//处理设备图片
		CompletableFuture<Void> fileFuture = CompletableFuture.runAsync(() -> handleDeviceImages(iocDeviceModel), executorService);
		//处理设备告警状态
		CompletableFuture<Void> alarmFuture = CompletableFuture.runAsync(() -> handleDeviceAlarm(iocDeviceModel), executorService);
        //处理设备服务商
        CompletableFuture<Void> supplierFuture = CompletableFuture.runAsync(() -> handleSupplier(iocDeviceModel), executorService);
		//同步
		CompletableFuture.allOf(nameFuture, complexFuture, labelFuture, groupFuture, productFuture, fileFuture, alarmFuture, supplierFuture).join();
		return iocDeviceModel;
	}

	/**
	 * 新增ioc设备.
	 * @Param param ioc设备信息
	 * @Return 新增ioc设备是否成功
	 */
	@Override
	public Boolean add(IocDeviceParam param) {
		IocDevice iocDevice = BeanUtils.convertTo(param, IocDevice::new);
		iocDevice.setId(null);
		//新增公共校验
		handleAddOrEdit(iocDevice, param);
		this.save(iocDevice);
		//处理设备图片文件
		handleFiles(iocDevice, param);
		return Boolean.TRUE;
	}


	/**
	 * 删除ioc设备.
	 * @Param id ioc设备标识
	 * @Return 删除ioc设备是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean remove(Long id) {
		IocDevice iocDevice = this.getById(id);
		validateDeviceExists(iocDevice);
        AssertUtils.isFalse(Delete.NORMAL.getKey().equals(iocDevice.getEnableStatus()),"请将设备先禁用再删除！");
		validateDeviceHasNoChildren(id);
		iocDevice.setDeleted(Delete.DELETED.getKey());
		this.updateById(iocDevice);
		//处理告警
		handleAlarm(id, Status.disabled.getKey(), null, AlarmInfoConstant.ALARM_END_TYPE_7);
		return true;
	}


	/**
	 * 编辑ioc设备信息.
	 * @Param param ioc设备信息
	 * @Return 编辑ioc设备是否成功
	 */
	@Override
	public Boolean edit(IocDeviceParam param) {
		IocDevice iocDevice = this.getById(param.getId());
		validateDeviceExists(iocDevice);
		IocDevice editParam = BeanUtils.convertTo(param, IocDevice::new);
		//新增公共校验
		handleAddOrEdit(editParam, param);
		//处理设备图片文件
		handleFiles(iocDevice, param);
		//校验母设备是否包含子设备
		checkDeviceComplex(param);
		return this.updateById(editParam);
	}

	@Override
	public IotDeviceCheckModel findIotDevice(String iotDeviceDn) {
		IotDeviceCheckModel model = new IotDeviceCheckModel();
		model.setIotDeviceDn(iotDeviceDn);
        IotDeviceState deviceState = iotCapacityService.queryDeviceState(iotDeviceDn);
		if(ObjectUtil.isNotEmpty(deviceState)){
            model.setIotDeviceStatus(deviceState.getOnline() ? Status.enabled.getKey() : Status.disabled.getKey());
            model.setIotProductCode(deviceState.getProductKey());
		}
		return model;
	}

	@Override
	public List<DeviceThingModel> thingModelList(Long deviceId) {
		IocDevice iocDevice = getById(deviceId);
		validateDeviceExists(iocDevice);
		if(ObjectUtil.isEmpty(iocDevice.getIotDeviceDn()) || ObjectUtil.isEmpty(iocDevice.getIotProductCode())){
			return Collections.emptyList();
		}
        IotThingModel.Model model = iotCapacityService.queryThingModel(iocDevice.getIotProductCode());
        //服务
        List<IotThingModel.Service> iotServices = model.getServices();
        return iotServices.stream().map(s->{
            DeviceThingModel thingModel = new DeviceThingModel();
            thingModel.setIdentifier(s.getIdentifier());
            thingModel.setName(s.getName());
            List<DeviceThingModelParam> inputData = s.getInputData().stream().map(i->{
                DeviceThingModelParam modelParam = new DeviceThingModelParam();
                modelParam.setIdentifier(i.getIdentifier());
                modelParam.setName(i.getName());
                modelParam.setDataType(i.getDataType().getType());
                modelParam.setDataSpecs(JSON.parseObject(i.getDataType().getSpecs().toString()));
                return modelParam;
            }).collect(Collectors.toList());
            thingModel.setInputData(inputData);
            return thingModel;
        }).collect(Collectors.toList());
	}

	@Override
	public List<DepartmentInfoModel> findSubDepartments() {
        List<OrgDepartmentNode> list = departmentApiService.getOrgTreeForOrgWidget(departmentId);
        return list.stream().map(dept->{
            DepartmentInfoModel model = new DepartmentInfoModel();
            model.setId(dept.getDeptNo());
            model.setName(dept.getOrgName());
            model.setParentId(dept.getParentOrgId());
            model.setTreePath(dept.getDeptIdPath());
            return model;
        }).collect(Collectors.toList());
	}

	@Override
	public List<UserInfoModel> findUserInfoByDepartment(String departmentId) {
        return userApiService.getStaffsByOrgId(departmentId,1,100,"");
	}

	@Override
	public List<IotDeviceState> fetchIotDeviceStates(List<String> deviceIdList) {
        return iotCapacityService.queryDeviceStates(deviceIdList);
	}

	@Override
	public List<IotDeviceRelationModel> findRelationDevices(IotDeviceRelationListParam param) {
		AssertUtils.notNull(param.getDeviceId(), "设备id不能为空");
		AssertUtils.notNull(param.getRelationType(), "设备关联类型不能为空");
		List<IotDeviceRelation> relations = iotDeviceRelationService.list(Wrappers.<IotDeviceRelation>lambdaQuery().eq(IotDeviceRelation::getDeviceId, param.getDeviceId()).eq(IotDeviceRelation::getRelationType, param.getRelationType()));
		if(CollectionUtil.isEmpty(relations)){
			return Collections.emptyList();
		}
		List<IotDeviceRelationModel> models = BeanUtils.convertListTo(relations, IotDeviceRelationModel::new);
		List<Long> deviceIds = relations.stream().map(IotDeviceRelation::getRelationDeviceId).collect(Collectors.toList());
		List<IocDevice> devices = this.list(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId, IocDevice::getDeviceName, IocDevice::getIotDeviceDn).in(IocDevice::getId, deviceIds));
		if(CollectionUtil.isNotEmpty(devices)){
			Map<Long, IocDevice> deviceNameMap = devices.stream().collect(Collectors.toMap(IocDevice::getId, d->d));
			models.forEach(model -> {
                model.setRelationDeviceName(deviceNameMap.get(model.getRelationDeviceId()).getDeviceName());
                model.setIotDeviceDn(deviceNameMap.get(model.getRelationDeviceId()).getIotDeviceDn());
            });
		}
		return models;
	}

	@Override
	public Boolean saveRelationDevices(IotDeviceRelationParam param) {
		AssertUtils.notNull(param.getDeviceId(), "设备id不能为空");
		AssertUtils.notNull(param.getRelationType(), "设备关联类型不能为空");
		iotDeviceRelationService.remove(Wrappers.<IotDeviceRelation>lambdaQuery().eq(IotDeviceRelation::getDeviceId, param.getDeviceId()).eq(IotDeviceRelation::getRelationType, param.getRelationType()));
		if(CollectionUtil.isNotEmpty(param.getRelationDeviceIds())){
			List<IotDeviceRelation> relations = new ArrayList<>();
			param.getRelationDeviceIds().forEach(item->{
				IotDeviceRelation relation = new IotDeviceRelation();
				relation.setDeviceId(param.getDeviceId());
				relation.setRelationType(param.getRelationType());
				relation.setRelationDeviceId(item);
				relations.add(relation);
			});
			iotDeviceRelationService.saveBatch(relations);
		}
		return true;
	}

	@Override
	public Boolean updateIotDeviceStatus() {
		List<IocDevice> iocDevices = this.list(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId, IocDevice::getIotDeviceDn).isNotNull(IocDevice::getIotDeviceDn).eq(IocDevice::getIotDevicePlatform, DevicePlatformEnum.OWN.getCode()));
		if(CollectionUtil.isEmpty(iocDevices)){
			return true;
		}
		List<String> deviceIdList = iocDevices.stream().map(IocDevice::getIotDeviceDn).collect(Collectors.toList());
		List<IotDeviceState> iotDeviceStates = fetchIotDeviceStates(deviceIdList);
		if(CollectionUtil.isEmpty(iotDeviceStates)){
			return true;
		}
		Map<String, Boolean> iotDeviceStateMap = iotDeviceStates.stream().collect(Collectors.toMap(IotDeviceState::getDeviceId, IotDeviceState::getOnline));
		List<IocDeviceUpdate> iocDeviceUpdates = new ArrayList<>();
		Date now = new Date();
		iocDevices.forEach(item -> {
			if(iotDeviceStateMap.containsKey(item.getIotDeviceDn())){
				IocDeviceUpdate iocDeviceUpdate = new IocDeviceUpdate();
				iocDeviceUpdate.setId(item.getId());
				iocDeviceUpdate.setIotDeviceStatus(iotDeviceStateMap.get(item.getIotDeviceDn()) ? 1 : 0);
				iocDeviceUpdate.setIotDeviceStatusTime(now);
				iocDeviceUpdates.add(iocDeviceUpdate);
			}
		});
		if(CollectionUtil.isNotEmpty(iocDeviceUpdates)){
			iocDeviceUpdateService.updateBatchById(iocDeviceUpdates);
		}
		return true;
	}

	@Override
	public DeviceVideoTreeModel treeSpaceDevices(IocDeviceParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		//空间设备树
		DeviceVideoTreeModel deviceVideoTreeModel = new DeviceVideoTreeModel();
		//空间树
		List<ParkSpaceTreeModel> spaceTreeModels = parkSpaceService.tree(new ParkSpaceListParam());
		deviceVideoTreeModel.setParkSpaceTrees(spaceTreeModels);
		//查询视频设备列表
		DeviceGroupRelParam relParam = new DeviceGroupRelParam();
		relParam.setGroupCode(afDeviceGroup);
		relParam.setTenantId(param.getTenantId());
		List<DeviceGroupRelModel> relModels = deviceGroupRelRepository.findDevices(relParam);
		if(CollectionUtil.isEmpty(relModels)){
			return deviceVideoTreeModel;
		}
		List<Long> deviceIds = relModels.stream().map(DeviceGroupRelModel::getDeviceId).collect(Collectors.toList());
		List<IocDevice> devices = this.list(Wrappers.<IocDevice>lambdaQuery()
				.in(IocDevice::getId, deviceIds)
				.like(ObjectUtil.isNotEmpty(param.getDeviceName()), IocDevice::getDeviceName, param.getDeviceName())
				.eq(ObjectUtil.isNotEmpty(param.getKeyArea()), IocDevice::getKeyArea, param.getKeyArea())
				.eq(ObjectUtil.isNotEmpty(param.getTenantId()), IocDevice::getTenantId, param.getTenantId()));
		if(CollectionUtil.isEmpty(devices)){
			return deviceVideoTreeModel;
		}
		List<DeviceVideoModel> deviceVideoModels = BeanUtils.convertListTo(devices, DeviceVideoModel::new);
		List<DeviceVideoModel> spaceDeviceModels = deviceVideoModels.stream().filter(p->ObjectUtil.isNotEmpty(p.getSpaceId())).collect(Collectors.toList());
		List<DeviceVideoModel> noSpaceDeviceModels = deviceVideoModels.stream().filter(p->ObjectUtil.isEmpty(p.getSpaceId())).collect(Collectors.toList());
		deviceVideoTreeModel.setNoSpaceDeviceVideos(noSpaceDeviceModels);

		// 将有空间位置的设备放入对应空间树中，并填充设备数量和设备集合
		if (CollectionUtil.isNotEmpty(spaceTreeModels) && CollectionUtil.isNotEmpty(spaceDeviceModels)) {
			// 按空间ID分组设备
			Map<Long, List<DeviceVideoModel>> spaceDeviceMap = spaceDeviceModels.stream()
					.collect(Collectors.groupingBy(DeviceVideoModel::getSpaceId));

			// 递归填充空间树中的设备信息
			fillSpaceTreeWithDevices(spaceTreeModels, spaceDeviceMap);
		}
		return deviceVideoTreeModel;
	}

	@Override
	public DeviceVideoTreeModel treeKeySpaceDevices(IocDeviceParam param) {
		param.setKeyArea( Status.enabled.getKey());
		DeviceVideoTreeModel deviceVideoTreeModel = this.treeSpaceDevices(param);
		// 过滤设备数量为0的空间树
		List<ParkSpaceTreeModel> filteredSpaceTrees = filterZeroDeviceSpaces(deviceVideoTreeModel.getParkSpaceTrees());
		deviceVideoTreeModel.setParkSpaceTrees(filteredSpaceTrees);
		return deviceVideoTreeModel;
	}

	@Override
	public Boolean signKeyAreas(IocDeviceParam param) {
		AssertUtils.notNull(param.getKeyArea(), "重点标记不能为空");
		AssertUtils.notNull(param.getId(), "设备id不能为空");
		return this.update(Wrappers.<IocDevice>lambdaUpdate()
				.set(IocDevice::getKeyArea, param.getKeyArea())
				.eq(IocDevice::getId, param.getId()));
	}

	/**
	 * 递归过滤设备数量为0的空间节点
	 * @param spaceTreeModels 空间树模型列表
	 * @return 过滤后的空间树模型列表
	 */
	private List<ParkSpaceTreeModel> filterZeroDeviceSpaces(List<ParkSpaceTreeModel> spaceTreeModels) {
		if (CollectionUtil.isEmpty(spaceTreeModels)) {
			return Collections.emptyList();
		}

		List<ParkSpaceTreeModel> filteredList = new ArrayList<>();

		for (ParkSpaceTreeModel spaceTreeModel : spaceTreeModels) {
			// 递归处理子节点
			if (CollectionUtil.isNotEmpty(spaceTreeModel.getChildren())) {
				List<ParkSpaceTreeModel> filteredChildren = filterZeroDeviceSpaces(spaceTreeModel.getChildren());
				spaceTreeModel.setChildren(filteredChildren);
			}

			// 如果当前节点设备数大于0
			if (spaceTreeModel.getDeviceNum() > 0) {
				filteredList.add(spaceTreeModel);
			}
		}
		return filteredList;
	}


	/**
	 * 递归填充空间树中的设备信息，并计算包含子空间的设备总数
	 * @param spaceTreeModels 空间树
	 * @param spaceDeviceMap 空间设备映射
	 * @return 当前节点及其子节点的设备总数
	 */
	private int fillSpaceTreeWithDevices(List<ParkSpaceTreeModel> spaceTreeModels, Map<Long, List<DeviceVideoModel>> spaceDeviceMap) {
		int totalDeviceCount = 0;
		for (ParkSpaceTreeModel spaceTreeModel : spaceTreeModels) {
			// 获取当前空间下的设备
			List<DeviceVideoModel> currentSpaceDevices = spaceDeviceMap.get(spaceTreeModel.getId());
			int currentSpaceDeviceCount = 0;

			if (CollectionUtil.isNotEmpty(currentSpaceDevices)) {
				spaceTreeModel.setDeviceVideoModels(currentSpaceDevices);
				currentSpaceDeviceCount = currentSpaceDevices.size();
				spaceTreeModel.setDeviceNum(currentSpaceDeviceCount);
			} else {
				spaceTreeModel.setDeviceVideoModels(new ArrayList<>());
				spaceTreeModel.setDeviceNum(0);
			}

			// 递归处理子空间
			int childDeviceCount = 0;
			if (CollectionUtil.isNotEmpty(spaceTreeModel.getChildren())) {
				childDeviceCount = fillSpaceTreeWithDevices(spaceTreeModel.getChildren(), spaceDeviceMap);
			}

			// 总设备数 = 当前空间设备数 + 子空间设备数
			int totalSpaceDeviceCount = currentSpaceDeviceCount + childDeviceCount;
			spaceTreeModel.setDeviceNum(totalSpaceDeviceCount);
			totalDeviceCount += totalSpaceDeviceCount;
		}
		return totalDeviceCount;
	}

	/**
	 * 构建角色相关查询范围
	 */
	private void buildRole(IocDevicePageParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        UserInfoModel user = userApiService.getCurrentUserInfo();

        if(roleApiService.hasRole(user.getStaffNo(),systemRoleCode)){
            // 如果是超级管理员
            return ;
        }
		TenantMemberListParam param1 = new TenantMemberListParam();
		param1.setTenantId(tenantId);
		param1.setUserId(user.getId());
		List<TenantMemberDomain> domainList = tenantMemberService.list(param1);
        AssertUtils.notEmpty(domainList,"当前园区下用户不存在");

		TenantMemberDomain domain = domainList.get(0);
		// 如果是园区管理员
		if (domain.getIdentity() != null && domain.getIdentity() == 1) {
			param.setTenantId(tenantId);
			return;
		}
		// 否则就是普通人员
		param.setTenantId(tenantId);
		param.setUserId(user.getId());
	}

	private LambdaQueryWrapper<IocDevice> buildQuery(IocDevicePageParam param) {
		if(ObjectUtil.isEmpty(param.getDeleted())){
			param.setDeleted(Delete.NORMAL.getKey());
		}
		LambdaQueryWrapper<IocDevice> query = new LambdaQueryWrapper<>();
		//设备名称
		query.like(ObjectUtil.isNotEmpty(param.getDeviceName()), IocDevice::getDeviceName, param.getDeviceName());
		//产品id
		query.eq(ObjectUtil.isNotEmpty(param.getProductId()), IocDevice::getProductId, param.getProductId());
		if (param.getAuth()) {
			// 普通角色 差所属空间下所有用户
			query.and((param.getUserId() != null && param.getTenantId() != null), wrapper -> {
                return wrapper.apply(" space_id in (select space_id from bbx_user_space where tenant_id = {0} and user_id = {1} )", param.getTenantId(), param.getUserId());
			});
		}
		// 空间位置查询
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), IocDevice::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		// 空间位置查询
		query.eq(ObjectUtil.isNotEmpty(param.getSpaceId()), IocDevice::getSpaceId, param.getSpaceId());
		// 空间位置集合查询
		query.in(CollectionUtil.isNotEmpty(param.getSpaceIds()), IocDevice::getSpaceId, param.getSpaceIds());
		//设备等级
		query.eq(ObjectUtil.isNotEmpty(param.getDeviceLevel()), IocDevice::getDeviceLevel, param.getDeviceLevel());
		//使用部门
		query.eq(ObjectUtil.isNotEmpty(param.getDepartmentId()), IocDevice::getDepartmentId, param.getDepartmentId());
		//设备平台
		query.eq(ObjectUtil.isNotEmpty(param.getIotDevicePlatform()), IocDevice::getIotDevicePlatform, param.getIotDevicePlatform());
		//设备平台集合
		query.in(CollectionUtil.isNotEmpty(param.getIotDevicePlatformList()), IocDevice::getIotDevicePlatform, param.getIotDevicePlatformList());
		//启用状态
		query.eq(ObjectUtil.isNotEmpty(param.getEnableStatus()), IocDevice::getEnableStatus, param.getEnableStatus());
		//上线状态
		query.eq(ObjectUtil.isNotEmpty(param.getOnlineStatus()), IocDevice::getOnlineStatus, param.getOnlineStatus());
		//设备类型
		query.eq(ObjectUtil.isNotEmpty(param.getDeviceType()), IocDevice::getDeviceType, param.getDeviceType());
		//母子设备类型
		query.eq(ObjectUtil.isNotEmpty(param.getDeviceComplexType()), IocDevice::getDeviceComplexType, param.getDeviceComplexType());
		//抄表类型
		query.eq(ObjectUtil.isNotEmpty(param.getReadingType()), IocDevice::getReadingType, param.getReadingType());
		//抄表类型集合
		query.in(CollectionUtil.isNotEmpty(param.getReadingTypes()), IocDevice::getReadingType, param.getReadingTypes());
		//抄表设备
		query.eq(ObjectUtil.isNotEmpty(param.getReadingDevice()), IocDevice::getReadingDevice, param.getReadingDevice());
		//设备类别
		query.eq(ObjectUtil.isNotEmpty(param.getDeviceCategory()), IocDevice::getDeviceCategory, param.getDeviceCategory());
		// 查询未删除的设备
		query.eq(IocDevice::getDeleted, param.getDeleted());
		query.in(CollectionUtil.isNotEmpty(param.getDeviceIdList()), IocDevice::getId, param.getDeviceIdList());
		query.notIn(CollectionUtil.isNotEmpty(param.getNoDeviceIdList()), IocDevice::getId, param.getNoDeviceIdList());
		query.eq(ObjectUtil.isNotEmpty(param.getTenantId()), IocDevice::getTenantId, param.getTenantId());
		query.orderByDesc(IocDevice::getCreateTime);
		return query;
	}

	private LambdaUpdateWrapper<IocDevice> buildUpdate(IocDeviceBindingParam param){
		LambdaUpdateWrapper<IocDevice> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		lambdaUpdateWrapper.in(IocDevice::getId, param.getIdList())
				.set(ObjectUtil.isNotEmpty(param.getSpaceId()), IocDevice::getSpaceId, param.getSpaceId())
				.set(ObjectUtil.isNotEmpty(param.getProductId()), IocDevice::getProductId, param.getProductId())
				.set(ObjectUtil.isNotEmpty(param.getDepartmentId()), IocDevice::getDepartmentId, param.getDepartmentId())
				.set(ObjectUtil.isNotEmpty(param.getDepartmentName()), IocDevice::getDepartmentName, param.getDepartmentName())
                .set(IocDevice::getUpdatorId,userApiService.getCurrentStaffNo())
				.set(IocDevice::getUpdateTime, new Date());
		return lambdaUpdateWrapper;
	}



	private void handleNames(List<IocDeviceModel> deviceInfoModels) {
		if(CollectionUtil.isEmpty(deviceInfoModels)){
			return;
		}
		List<Long> spaceIds = deviceInfoModels.stream().filter(device -> ObjectUtil.isNotEmpty(device.getSpaceId())).map(IocDeviceModel::getSpaceId).collect(Collectors.toList());
		List<Long> productIds = deviceInfoModels.stream().filter(device -> ObjectUtil.isNotEmpty(device.getProductId())).map(IocDeviceModel::getProductId).collect(Collectors.toList());
		Map<Long, ParkSpaceFullModel> fullSpaceMap = CollectionUtil.isEmpty(spaceIds) ? new HashMap<>() : parkSpaceService.findFullSpaceMap(spaceIds, null);
		List<IocProduct> iocProducts = CollectionUtil.isEmpty(productIds) ? Collections.emptyList() : iocProductService.getBaseMapper().selectList(Wrappers.<IocProduct>lambdaQuery().in(IocProduct::getId, productIds));
		Map<Long, IocProduct> productMap = CollectionUtil.isEmpty(iocProducts)? new HashMap<>() : iocProducts.stream().collect(Collectors.toMap(IocProduct::getId, product -> product));
		deviceInfoModels.forEach(device -> {
			//空间名称
			if(ObjectUtil.isNotEmpty(device.getSpaceId())){
				ParkSpaceFullModel parkSpaceFullModel = fullSpaceMap.get(device.getSpaceId());
				Optional.ofNullable(parkSpaceFullModel).ifPresent(model -> device.setSpaceName(model.getFullPath()));
			}

			//产品名称
			if(ObjectUtil.isNotEmpty(device.getProductId())){
				IocProduct iocProduct = productMap.get(device.getProductId());
				Optional.ofNullable(iocProduct).ifPresent(model -> device.setProductName(handleProductName(model)));
			}
		});
	}

	private String handleProductName(IocProduct iocProduct){
		return iocProduct.getPdName() + (Delete.NORMAL.getKey().equals(iocProduct.getDeleted()) ? "" : "（已删除）");
	}

	private boolean verifyComplexType(Long tenantId, Long id) {
        return count(Wrappers.<IocDevice>lambdaQuery().eq(IocDevice::getTenantId, tenantId)
                .eq(IocDevice::getDeviceComplexType, DeviceComplexTypeEnum.MOTHER.getCode())
                .eq(IocDevice::getDeviceType, DeviceTypeEnum.COMPLEX.getCode())
                .eq(IocDevice::getDeleted,Delete.NORMAL.getKey())
                .eq(IocDevice::getId, id))>0;
	}

	private void handleAddOrEdit(IocDevice iocDevice, IocDeviceParam param){
		AssertUtils.notNull(param.getDeviceName(), "设备名称不能为空");
		if(DeviceTypeEnum.COMPLEX.getCode().equals(param.getDeviceType())){
			AssertUtils.notNull(param.getDeviceComplexType(), "母设备类型不能为空");
			if(DeviceComplexTypeEnum.SON.getCode().equals(param.getDeviceComplexType())){
				AssertUtils.notNull(param.getDeviceComplexId(), "母设备不能为空");
				AssertUtils.isTrue(verifyComplexType(WebFrameworkUtils.getHeaderTenantId(), param.getDeviceComplexId()),"母设备不存在");
			}
		}else{
			iocDevice.setDeviceComplexType(null);
			iocDevice.setDeviceComplexId(null);
		}
		if(Status.enabled.getKey().equals(param.getReadingDevice())){
			AssertUtils.notNull(param.getReadingType(), "抄表设备类型不能为空");
		}else{
			iocDevice.setReadingType(null);
		}
		if(!DevicePlatformEnum.NO.getCode().equals(param.getIotDevicePlatform())){
            param.setIotDeviceStatus(ObjectUtil.isEmpty(param.getIotDeviceStatus()) ? Status.enabled.getKey() : param.getIotDeviceStatus());
			AssertUtils.notNull(param.getIotDeviceDn(), "iot设备识别码不能为空");
			AssertUtils.notNull(param.getIotDeviceStatus(), "iot设备状态不能为空");
			iocDevice.setIotDeviceStatusTime(new Date());
            iocDevice.setIotDeviceStatus(param.getIotDeviceStatus());
		}else{
			iocDevice.setIotDeviceDn(null);
			iocDevice.setIotDeviceStatus(null);
			iocDevice.setIotDeviceStatusTime(null);
		}
	}
	private void handleFiles(IocDevice iocDevice, IocDeviceParam param){
        fileService.removeByRelatedId(FileTypeEnum.DEVICE.getValue(), iocDevice.getId());
		if(CollectionUtil.isNotEmpty(param.getFileParamList())){
			List<File> files = new ArrayList<>();
			param.getFileParamList().forEach(item->{
				File file = BeanUtils.convertTo(item, File::new);
				file.setRelatedId(iocDevice.getId());
				file.setType(FileTypeEnum.DEVICE.getValue());
				files.add(file);
			});
			fileService.addBatch(files);
		}
	}

	private void handleProduct(IocDeviceModel iocDeviceModel){
		if(ObjectUtil.isNotEmpty(iocDeviceModel.getProductId())){
			iocDeviceModel.setIocProductModel(iocProductService.getDetail(iocDeviceModel.getProductId()));
		}
	}

	private void handleDeviceLabels(IocDeviceModel iocDeviceModel){
		DeviceLabelListParam param = new DeviceLabelListParam();
		param.setDeviceId(iocDeviceModel.getId());
		List<DeviceLabelModel> labelModelList = deviceLabelService.list(param);
		if(CollectionUtil.isNotEmpty(labelModelList)){
			iocDeviceModel.setLabelNames(String.join(",", labelModelList.stream().map(DeviceLabelModel::getLabelName).collect(Collectors.toList())));
		}
	}

	private void handleDeviceGroups(IocDeviceModel iocDeviceModel){
		DeviceGroupListParam param = new DeviceGroupListParam();
		param.setDeviceId(iocDeviceModel.getId());
		List<DeviceGroupModel> groupModelList = deviceGroupService.list(param);
		if(CollectionUtil.isNotEmpty(groupModelList)){
			iocDeviceModel.setGroupNames(String.join(",", groupModelList.stream().map(DeviceGroupModel::getGroupName).collect(Collectors.toList())));
		}
	}

	private void handleDeviceImages(IocDeviceModel iocDeviceModel){
		List<FileModel> fileModels = fileService.findByTypeAndRelatedId(FileTypeEnum.DEVICE.getValue(), iocDeviceModel.getId());
		if(CollectionUtil.isNotEmpty(fileModels)){
			iocDeviceModel.setFileModelList(fileModels);
		}
	}

	private void handleDeviceAlarm(IocDeviceModel iocDeviceModel){
		if(DevicePlatformEnum.NO.getCode().equals(iocDeviceModel.getIotDevicePlatform())){
			return;
		}
		int count = alarmInfoService.count(Wrappers.<AlarmInfo>lambdaQuery()
				.ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3)
				.inSql(AlarmInfo::getId, "SELECT distinct alarm_id FROM bbx_alarm_device WHERE device_id = " + iocDeviceModel.getId()));
		if (count > 0) {
			iocDeviceModel.setAlarmStatus(Status.enabled.getKey());
		}else{
			iocDeviceModel.setAlarmStatus(Status.disabled.getKey());
		}
	}

    private void handleSupplier(IocDeviceModel iocDeviceModel){
        iocDeviceModel.setSupplierPerson("");
        iocDeviceModel.setSupplierMobile("");
        iocDeviceModel.setSupplierName("");
        if(ObjectUtil.isEmpty(iocDeviceModel.getSupplierId())){
            return;
        }
        Supplier supplier = supplierService.getById(iocDeviceModel.getSupplierId());
        if(ObjectUtil.isNotEmpty(supplier)){
            iocDeviceModel.setSupplierName(supplier.getName());
        }
        if(ObjectUtil.isEmpty(iocDeviceModel.getSupplierPersonId())){
            return;
        }
        SupplierPerson supplierPerson = supplierPersonService.getById(iocDeviceModel.getSupplierPersonId());
        if(ObjectUtil.isNotEmpty(supplierPerson)){
            iocDeviceModel.setSupplierPerson(supplierPerson.getName());
            iocDeviceModel.setSupplierMobile(supplierPerson.getPhone());
        }
    }

	private void handleDeviceComplex(IocDeviceModel iocDeviceModel){
		LambdaQueryWrapper<IocDevice> query = new LambdaQueryWrapper<>();
		query.select(IocDevice::getId, IocDevice::getDeviceName, IocDevice::getDeviceCode, IocDevice::getSpaceId, IocDevice::getDeviceLevel);
		query.eq(IocDevice::getDeleted, Delete.NORMAL.getKey());
		List<IocDevice> iocDevices = new ArrayList<>();
		if(DeviceComplexTypeEnum.SON.getCode().equals(iocDeviceModel.getDeviceComplexType())){
			query.eq(IocDevice::getId, iocDeviceModel.getDeviceComplexId());
			iocDevices = this.list(query);

		}else if(DeviceComplexTypeEnum.MOTHER.getCode().equals(iocDeviceModel.getDeviceComplexType())){
			query.eq(IocDevice::getDeviceComplexId, iocDeviceModel.getId());
			iocDevices = this.list(query);
		}
		if(CollectionUtil.isNotEmpty(iocDevices)){
			List<IocDeviceModel> iocDeviceModels = BeanUtils.convertListTo(iocDevices, IocDeviceModel::new);
			//处理空间名称和产品名称
			handleNames(iocDeviceModels);
			iocDeviceModel.setIocDeviceSimpleModelList(BeanUtils.convertListTo(iocDeviceModels, IocDeviceSimpleModel::new));
		}
	}
	private void validateDeviceExists(IocDevice iocDevice) {
		AssertUtils.notNull(iocDevice, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.isFalse(Status.disabled.getKey().equals(iocDevice.getDeleted()), "设备不存在（后台未找到该设备）");
	}

	private void validateDeviceHasNoChildren(Long id) {
		AssertUtils.isFalse(this.count(Wrappers.<IocDevice>lambdaQuery().eq(IocDevice::getDeviceComplexId, id).eq(IocDevice::getDeleted, Delete.NORMAL.getKey())) > 0, "该设备为母设备，存在子设备，请解除母子关系后再删除");
	}
	private void handleAlarm(Long deviceId, Integer status, String remark, Integer alarmEndType) {
		log.info("设备结束告警,设备id:{}, 设备状态：{}， 设备类型：{}, 设备备注：{}",deviceId,status, alarmEndType, remark);
		if (Status.disabled.getKey().equals(status)) {
			alarmInfoService.handAlarmsByDevices(Collections.singletonList(deviceId), null, remark, alarmEndType);
		}
	}
}
