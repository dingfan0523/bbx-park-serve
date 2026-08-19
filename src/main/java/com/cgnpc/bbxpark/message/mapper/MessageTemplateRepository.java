
package com.cgnpc.bbxpark.message.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.message.domain.MessageTemplate;
import com.cgnpc.bbxpark.message.dto.req.MessageRoleParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageRoleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @Description 消息模版数据操作接口
 * @author huangyongtao
 * @date 2024/10/24 16:47
 */
public interface MessageTemplateRepository extends BaseMapper<MessageTemplate> {
    /***
     * @Description 查询角色列表
     * @author huangyongtao
     * @date 2024/11/4 14:53
     * @param param
     */
    List<MessageRoleModel> findRoleList(@Param("condition") MessageRoleParam param);
}
