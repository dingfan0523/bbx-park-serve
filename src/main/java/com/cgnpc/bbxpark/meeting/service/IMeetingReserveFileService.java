
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserveFile;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveFileModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingReserveFileParam;

import java.util.List;

/***
 * @Description 会议预约文件服务接口
 * @author huangyongtao
 * @date 2024/12/23 16:03
 */
public interface IMeetingReserveFileService extends IService<MeetingReserveFile> {

	/**
	 * 根据会议id查询
	 */
	List<MeetingReserveFileModel> findByReserveId(Long reserveId);

	IPage<MeetingReserveFileModel> pageBy(Long reserveId, long page, long size);

	/**
	 * 新增文件.
	 */
	Boolean add(MeetingReserveFile file);

	/**
	 * 删除附件
	 * @param id id
	 * @return 结果
	 */
	Boolean remove(Long id);

	/***
	 * @Description 批量新增会议文件
	 * @author huangyongtao
	 * @date 2024/12/24 15:23
	 * @param params
	 */
	Boolean addBatch(Long reserveId, List<MeetingReserveFileParam> params);

	/**
	 * 根据会议id删除附件
	 * @param reserveId
	 * @return 结果
	 */
	Boolean removeByReserveId(Long reserveId);

	/**
	 * 编辑文件.
	 */
	Boolean edit(MeetingReserveFileParam file);

    /**
     * 编辑文件集合.
     */
    Boolean edits(List<MeetingReserveFileParam> files);
}
