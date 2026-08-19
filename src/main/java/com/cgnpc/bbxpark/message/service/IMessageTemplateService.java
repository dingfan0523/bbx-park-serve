
package com.cgnpc.bbxpark.message.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.message.domain.MessageTemplate;
import com.cgnpc.bbxpark.message.dto.req.MessageRoleParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplateListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplatePageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplateParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageRoleModel;
import com.cgnpc.bbxpark.message.dto.resp.MessageTemplateModel;
import com.cgnpc.bbxpark.message.dto.resp.MessageUserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.UserInfoListParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @Description 消息模版服务接口
 * @author huangyongtao
 * @date 2024/10/24 16:52
 */
public interface IMessageTemplateService extends IService<MessageTemplate> {

	/**
	 * 根据消息模版标识获得消息模版详情信息.
	 * @Param [id] 消息模版标识
	 * @Return 消息模版详情信息
	 */
	MessageTemplateModel detail(MessageTemplateParam param);

	/**
	 * 获取消息模版列表(分页).
	 * @Param param 消息模版查询条件
	 * @Return 消息模版信息列表（分页）
	 */
	IPage<MessageTemplateModel> page(MessageTemplatePageParam param);

	/**
	 * 获取消息模版列表.
	 * @Param param 消息模版查询条件
	 * @Return 消息模版信息列表
	 */
	List<MessageTemplateModel> list(MessageTemplateListParam param);

	/**
	 * 新增消息模版.
	 * @Param param 消息模版信息
	 * @Return 新增消息模版是否成功
	 */
	Boolean add(MessageTemplateParam param);


	/**
	 * 删除消息模版.
	 * @Param id 消息模版标识
	 * @Return 删除消息模版是否成功
	 */
	Boolean remove(MessageTemplateParam param);


	/**
	 * 编辑消息模版信息.
	 * @Param param 消息模版信息
	 * @Return 编辑消息模版是否成功
	 */
	Boolean edit(MessageTemplateParam param);


	/**
	 * 启用消息模版.
	 * @Param id 消息模版标识
	 * @Return 启用消息模版是否成功
	 */
	Boolean enable(MessageTemplateParam param);

	/**
	 * 禁用消息模版.
	 * @Param id 消息模版标识
	 * @Return 禁用消息模版是否成功
	 */
	Boolean disable(MessageTemplateParam param);

	/***
	 * @Description 查询模版推送配置详情
	 * @author huangyongtao
	 * @date 2024/10/28 9:34
	 * @param param
	 */
	MessageTemplateModel detailConfig(MessageTemplateParam param);
	/**
	 * 新增消息模版配置.
	 * @Param param 消息模版信息
	 * @Return 新增消息模版是否成功
	 */
	Boolean addConfig(MessageTemplateParam param);

	/***
	 * @Description 查询模版信息
	 * @author huangyongtao
	 * @date 2024/10/28 9:34
	 * @param param
	 */
	List<MessageTemplateModel> findTemplates(MessageTemplateListParam param);

    /***
     * @Description 查询用户信息集合
     * @author huangyongtao
     * @date 2024/10/28 17:35
     * @param condition
     */
    List<MessageUserInfoModel> findUserList(@Param("condition") UserInfoListParam condition);

	/***
	 * @Description 查询角色列表
	 * @author huangyongtao
	 * @date 2024/11/4 14:53
	 * @param param
	 */
	List<MessageRoleModel> findRoleList(@Param("condition") MessageRoleParam param);



}
