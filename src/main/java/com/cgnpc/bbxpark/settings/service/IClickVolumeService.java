
package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.settings.domain.ClickVolume;
import com.cgnpc.bbxpark.settings.dto.model.ClickVolumeModel;
import com.cgnpc.bbxpark.settings.dto.param.ClickVolumeListParam;
import com.cgnpc.bbxpark.settings.dto.param.ClickVolumeParam;

import java.util.List;



public interface IClickVolumeService extends IService<ClickVolume> {


	/**
	 * 获取点击量列表.
	 * @Param param 点击量查询条件
	 * @Return 点击量信息列表
	 */
	List<ClickVolumeModel> list(ClickVolumeListParam param);

	/**
	 * 新增点击量.
	 * @Param param 点击量信息
	 * @Return 新增点击量是否成功
	 */
	Boolean add(ClickVolumeParam param);


}
