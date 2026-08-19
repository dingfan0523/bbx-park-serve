
package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.settings.domain.BurialPoint;
import com.cgnpc.bbxpark.settings.dto.param.BurialPointParam;





public interface IBurialPointService extends IService<BurialPoint> {
	
	/**
	 * 新增埋点.
	 * @Param param 埋点信息
	 * @Return 新增埋点是否成功
	 */
	Boolean add(BurialPointParam param);


}
