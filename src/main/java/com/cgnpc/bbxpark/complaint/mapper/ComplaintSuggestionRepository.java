
package com.cgnpc.bbxpark.complaint.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.complaint.domain.ComplaintSuggestion;
import org.apache.ibatis.annotations.Select;

/***
 * @Description 投诉建议主表;数据操作接口
 * @author huangyongtao
 * @date 2024/7/12 14:12
 */
public interface ComplaintSuggestionRepository extends BaseMapper<ComplaintSuggestion> {

    @Select("SELECT * FROM bbx_complaint_suggestion WHERE id = #{id} FOR UPDATE")
    ComplaintSuggestion selectForUpdate(Long id);
}
