
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingService;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingServiceModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingServiceListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingServicePageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingServiceParam;

import java.util.List;

/***
 * @Description 会服服务接口
 * @author huangyongtao
 * @date 2024/8/23 15:39
 */
public interface IMeetingServiceService extends IService<MeetingService> {
    /**
     * PC端-会服分页列表
     *
     * @Param param 会服分页查询条件
     * @Return 会服信息列表（分页）
     */
    IPage<MeetingServiceModel> page(MeetingServicePageParam param);

    /**
     * PC端-会服列表
     *
     * @Param param 会服查询条件
     * @Return 会服列表
     */
    List<MeetingServiceModel> list(MeetingServiceListParam param);

    /**
     * PC端-会服详情
     *
     * @Param [id] 会服标识
     * @Return 会服详情信息
     */
    MeetingServiceModel detail(Long id);

    /**
     * PC端-查询自助会服
     *
     * @return 会服信息
     */
    MeetingService getByCommon();

    /**
     * 新增会服.
     *
     * @Param param 会服信息
     * @Return 新增会服是否成功
     */
    Boolean add(MeetingServiceParam param);

    /**
     * 编辑会服信息.
     *
     * @Param param 会服信息
     * @Return 编辑会服是否成功
     */
    Boolean edit(MeetingServiceParam param);

    /**
     * 删除会服.
     *
     * @Param id 会服标识
     * @Return 删除会服是否成功
     */
    Boolean remove(Long id);
}
