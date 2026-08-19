
package com.cgnpc.bbxpark.settings.service;

import com.cgnpc.bbxpark.settings.domain.RegionSpaceRelation;
import com.cgnpc.bbxpark.settings.dto.model.RegionSpaceRelationModel;
import com.cgnpc.bbxpark.settings.dto.param.RegionSpaceRelationParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/***
 * @Description 区域管理员空间关联服务接口
 * @author huangyongtao
 * @date 2025/3/11 11:23
 */
public interface IRegionSpaceRelationService extends IBaseService<RegionSpaceRelation> {


    /**
     * 新增区域管理员空间关联.
     * @Param param 区域管理员空间关联信息
     * @Return 新增区域管理员空间关联是否成功
     */
    Boolean add(RegionSpaceRelationParam param);

    /***
     * @Description 批量查询
     * @author huangyongtao
     * @date 2025/3/12 14:06
     * @param param
     */
    List<RegionSpaceRelationModel> list(RegionSpaceRelationParam param);

    /***
     * @Description 查询当前登录用户所管理的空间id
     * @author huangyongtao
     * @date 2025/3/12 14:06
     */
    List<Long> queryAllSpaceIdByLoginUser();
}
