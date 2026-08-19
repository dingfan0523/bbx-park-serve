
package com.cgnpc.bbxpark.invitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.invitation.domain.InvitationVisitor;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationVisitorModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationVisitorPageParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/***
 * @Description 邀约访客数据操作接口
 * @author huangyongtao
 * @date 2025/8/1 14:17
 */
@Repository
public interface InvitationVisitorRepository extends BaseMapper<InvitationVisitor> {

    /**
     * pc端-分页查询
     */
    IPage<InvitationVisitorModel> page(IPage<InvitationVisitorModel> page, @Param("condition") InvitationVisitorPageParam condition);
}
