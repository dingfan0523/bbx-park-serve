package com.cgnpc.qrtz.mapper;

import com.cgnpc.qrtz.model.IpmAssAll;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

public interface IpmAssAllMapper extends BaseMapper<IpmAssAll> {

    List<IpmAssAll> selectAllParamOnDm();

}
