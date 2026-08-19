
package com.cgnpc.bbxpark.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.settings.domain.LoginLog;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface LoginLogRepository  extends BaseMapper<LoginLog> {

}
