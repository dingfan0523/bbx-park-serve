
package com.cgnpc.bbxpark.invitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.invitation.domain.Invitation;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationPageParam;
import com.cgnpc.bbxpark.meeting.dto.model.AppMeetingReserveModel;
import com.cgnpc.bbxpark.meeting.dto.param.AppMeetingReservePageParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/***
 * @Description 邀约数据操作接口
 * @author huangyongtao
 * @date 2025/8/1 14:16
 */
@Repository
public interface InvitationRepository extends BaseMapper<Invitation>{
    /**
     * 移动端-分页查询
     */
    IPage<InvitationModel> pageApp(IPage<InvitationModel> page, @Param("condition") InvitationPageParam condition);


    /**
     * pc端-分页查询
     */
    IPage<InvitationModel> page(IPage<InvitationModel> page, @Param("condition") InvitationPageParam condition);

    /**
     * pc端-获取我的审批的邀约列表(分页)
     */
    IPage<InvitationModel> allApprovePage(IPage<InvitationModel> page, @Param("condition") InvitationPageParam condition);
}
