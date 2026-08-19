
package com.cgnpc.bbxpark.space.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.JsonUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.domain.SpaceImage;
import com.cgnpc.bbxpark.space.dto.model.SpaceImageModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceImageListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceImageParam;
import com.cgnpc.bbxpark.space.mapper.SpaceImageRepository;
import com.cgnpc.bbxpark.space.service.ISpaceImageService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SpaceImageServiceImpl extends ServiceImpl<SpaceImageRepository, SpaceImage> implements ISpaceImageService {
	@Resource
	private IUserApiService userApiService;

	/**
	 * 根据空间图片标识获得空间图片详情信息.
	 * @Param [id] 空间图片标识
	 * @Return 空间图片详情信息
	 */
	@Override
	public SpaceImageModel detail(Long id) {
		SpaceImage spaceImage = this.getById(id);
		AssertUtils.notNull(spaceImage, SystemResultCode.RESULT_DATA_NONE.message());
		SpaceImageModel model = BeanUtils.convertTo(spaceImage, SpaceImageModel::new);
		// 图片集合
		model.setImageList(JsonUtil.convertJsonArrStrToList(spaceImage.getImages()));
		//更新人信息
        model.setUserName(spaceImage.getCreateBy());
        model.setStaffid(spaceImage.getUpdatorId());
//		Optional.ofNullable(spaceImage.getUpdatorId())
//				.flatMap(userId -> Optional.ofNullable(userApiService.getByStaffNo(userId)))
//				.ifPresent(user -> {
//					model.setUserName(user.getUserName());
//					model.setStaffid(user.getStaffid());
//				});
		return model;
	}


	/**
	 * 获取空间图片列表.
	 * @Param param 空间图片查询条件
	 * @Return 空间图片信息列表
	 */
	@Override
	@SneakyThrows
	public List<SpaceImageModel> list(SpaceImageListParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		List<SpaceImage> spaceImages = this.list(Wrappers.<SpaceImage>lambdaQuery().eq(tenantId != null,SpaceImage::getTenantId, tenantId).eq(param.getSpaceId() != null,SpaceImage::getSpaceId, param.getSpaceId()).orderByDesc(SpaceImage::getCreateTime));
		//用户id集合
//		List<String> userIds = spaceImages.stream().map(SpaceImage::getUpdatorId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//		List<UserInfoModel> userInfos = Optional.of(userIds).filter(list -> !list.isEmpty()).flatMap(userParam -> Optional.ofNullable(userApiService.getByStaffNos(userIds)))
//				.orElse(Collections.emptyList());

//		Map<String, UserInfoModel> userMap = Optional.of(userInfos).map(users -> users.stream()
//                .collect(Collectors.toMap(UserInfoModel::getId, Function.identity()))).orElse(Collections.emptyMap());
		//数据组装
		return spaceImages.stream().map(item -> {
			SpaceImageModel model = BeanUtils.convertTo(item, SpaceImageModel::new);
			//图片集合
			model.setImageList(JsonUtil.convertJsonArrStrToList(item.getImages()));
			//更新人信息
            model.setStaffid(item.getUpdatorId());
            model.setUserName(item.getUpdateBy());
//			Optional.ofNullable(item.getUpdatorId()).map(userMap::get).ifPresent(user -> {
//				model.setUserName(user.getUserName());
//				model.setStaffid(user.getStaffid());
//			});
			return model;
		}).collect(Collectors.toList());
	}

	/**
	 * 新增空间图片.
	 * @Param param 空间图片信息
	 * @Return 新增空间图片是否成功
	 */
	@Override
	public synchronized Boolean add(SpaceImageParam param) {
		//校验类型是否存在
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		List<SpaceImage> list = this.list(Wrappers.<SpaceImage>lambdaQuery().eq(tenantId != null,SpaceImage::getTenantId, tenantId).eq(SpaceImage::getSpaceId, param.getSpaceId()).eq(SpaceImage::getType, param.getType()));
		AssertUtils.isTrue(list.isEmpty(), "该类型图片已存在");

		SpaceImage spaceImage = BeanUtils.convertTo(param, SpaceImage::new);
		spaceImage.setId(null);
		spaceImage.setImages(JsonUtil.convertListToJsonStr(param.getImageList()));
        spaceImage.setCreateBy(userApiService.getCurrentStaffName());
        spaceImage.setUpdateBy(spaceImage.getCreateBy());
		return save(spaceImage);
	}

	/**
	 * 编辑空间图片信息.
	 * @Param param 空间图片信息
	 * @Return 编辑空间图片是否成功
	 */
	@Override
	public Boolean edit(SpaceImageParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		//校验数据是否存在
		SpaceImage spaceImage = this.getById(param.getId());
		AssertUtils.notNull(spaceImage, SystemResultCode.RESULT_DATA_NONE.message());
		//校验类型是否存在
		List<SpaceImage> list = this.list(Wrappers.<SpaceImage>lambdaQuery().eq(tenantId != null,SpaceImage::getTenantId, tenantId).eq(SpaceImage::getSpaceId, param.getSpaceId()).eq(SpaceImage::getType, param.getType()).ne(SpaceImage::getId, param.getId()));
		AssertUtils.isTrue(list.isEmpty(), "该类型图片已存在");

		BeanUtils.copyProperties(param, spaceImage);
		spaceImage.setImages(JsonUtil.convertListToJsonStr(param.getImageList()));
        spaceImage.setUpdateBy(userApiService.getCurrentStaffName());
		return updateById(spaceImage);
	}

	/**
	 * 删除空间图片.
	 * @Param id 空间图片标识
	 * @Return 删除空间图片是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		SpaceImage spaceImage = this.getById(id);
		AssertUtils.notNull(spaceImage, SystemResultCode.RESULT_DATA_NONE.message());
		return this.removeById(id);
	}
}
