
package com.cgnpc.bbxpark.space.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.param.*;

import java.util.List;
import java.util.Map;


public interface IParkSpaceService extends IService<ParkSpace> {
    /**
     * 获取园区空间列表
     *
     * @param param 参数
     * @return 园区树
     */
    List<ParkSpaceTreeModel> listByParentId(ParkSpaceListParam param);
    /**
     * 获取园区空间树
     *
     * @param param 参数
     * @return 园区树
     */
    List<ParkSpaceTreeModel> findTreeList(ParkSpaceListParam param);

    /**
     * 获取园区空间树
     *
     * @param param 参数
     * @return 园区树
     */
    List<ParkSpaceTreeModel> tree(ParkSpaceListParam param);

    /**
     * 根据id集合查询包含全路径的空间Map
     *
     * @param idList   空间id集合
     * @param tenantId 租户id
     * @return 空间信息map
     */
    Map<Long, ParkSpaceFullModel> findFullSpaceMap(List<Long> idList, Long tenantId);

    /**
     * 查询子集id集合
     *
     * @param id 空间id
     * @return 子集id集合
     */
    List<Long> findChildrenIdList(Long id,Long tenantId);

    /**
     * 查询子集id集合
     *
     * @param ids 空间id
     * @return 子集id集合
     */
    List<Long> findChildrenIdList(List<Long> ids,Long tenantId);

    /***
     * @Description 构造树
     * @author huangyongtao
     * @date 2025/4/3 16:18
     * @param
     */
    List<ParkSpaceTreeModel> buildTree(Long tenantId);

    /**
     * 查询子集id集合
     *
     * @param ids 空间id
     * @return 子集id集合
     */
    List<Long> findChildrenIdList(List<Long> ids, List<ParkSpaceTreeModel> treeList);

    /**
     * 根据园区ID和空间编码校验唯一性
     *
     * @param param 校验参数
     * @return 空间编码是否存在
     */
    Boolean checkOnlyByParkIdAndCode(ParkSpaceCheckCodeParam param);

    /**
     * 同一层级空间名称校验唯一性
     *
     * @param param 校验参数
     * @return 空间编码是否存在
     */
    Boolean checkOnlyByParentIdAndName(ParkSpaceCheckNameParam param);

    /**
     * 新增园区空间列.
     *
     * @Param param 园区空间列信息
     * @Return 新增园区空间列是否成功
     */
    ParkSpaceModel add(ParkSpaceParam param);

    /**
     * 编辑园区空间列信息.
     *
     * @Param param 园区空间列信息
     * @Return 编辑园区空间列是否成功
     */
    Boolean edit(ParkSpaceParam param);

    /**
     * 删除园区空间列.
     *
     * @Param id 园区空间列标识
     * @Return 删除园区空间列是否成功
     */
    Boolean remove(Long id);

    /**
     * 新增园区并创建空间
     *
     * @param param 租户信息
     * @return 是否成功
     */
    Boolean addParkAndCreateSpace(TenantInfoParam param);

    /**
     * 根据园区id获取园区集合
     *
     * @param tenantId 租户/园区id
     * @return 园区集合
     */
    List<ParkSpaceModel> findParkSpaceList(Long tenantId);

    /**
     * 修改园区并更改空间顶级父名称
     *
     * @param param
     * @return
     */
    Boolean editParkAndCreateAspace(TenantInfoParam param);
}
