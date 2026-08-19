package com.cgnpc.bbxpark.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.space.domain.UserSpace;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.UserInfoSpaceParam;
import com.cgnpc.bbxpark.space.dto.param.UserSpaceListParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @Description 用户与空间访问权限数据操作接口
 * @author huangyongtao
 * @date 2024/7/1 16:39
 */
public interface UserSpaceRepository extends BaseMapper<UserSpace> {

    int countParam(@Param("condition") UserSpaceListParam condition);

    List<UserInfoModel> findUserInfoBySpace(@Param("condition") UserInfoSpaceParam condition);

}
