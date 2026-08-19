
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.common.enums.FileTypeEnum;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserveFile;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveFileModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingReserveFileParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveFileRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @Description 会议预约文件服务实现
 * @author huangyongtao
 * @date 2024/12/23 16:11
 */
@Service("meetingReserveFileService")
public class MeetingReserveFileServiceImpl extends ServiceImpl<MeetingReserveFileRepository, MeetingReserveFile> implements IMeetingReserveFileService {

	@Autowired
	private IFileService fileService;

	@Override
	public List<MeetingReserveFileModel> findByReserveId(Long reserveId) {
		List<MeetingReserveFile> list = list(Wrappers.<MeetingReserveFile>lambdaQuery().eq(MeetingReserveFile::getReserveId, reserveId));
		return BeanUtils.convertListTo(list, MeetingReserveFileModel::new);
	}

	@Override
	public IPage<MeetingReserveFileModel> pageBy(Long reserveId, long page, long limit) {
		IPage<MeetingReserveFile> filePage = page(new Page<>(page, limit), Wrappers.<MeetingReserveFile>lambdaQuery().eq(MeetingReserveFile::getReserveId, reserveId));
		return ConvertUtil.pageConvert(filePage.getCurrent(),filePage.getTotal(),filePage.getSize(),BeanUtils.convertListTo(filePage.getRecords(),MeetingReserveFileModel::new));
	}

	@Override
	public Boolean add(MeetingReserveFile reserveFile) {
		File file = BeanUtils.convertTo(reserveFile,File::new);
		file.setRelatedId(reserveFile.getReserveId());
		file.setType(FileTypeEnum.MEETING.getValue());
		fileService.add(file);
		return save(reserveFile);
	}

	@Override
	public Boolean remove(Long id) {
		return removeById(id);
	}

	@Override
	public Boolean addBatch(Long reserveId, List<MeetingReserveFileParam> params) {
		List<MeetingReserveFile> reserveFiles = new ArrayList<>();
		List<File> files = new ArrayList<>();
		params.forEach(param->{
			MeetingReserveFile reserveFile = BeanUtils.convertTo(param,MeetingReserveFile::new);
			reserveFile.setId(null);
			reserveFile.setReserveId(reserveId);
			reserveFiles.add(reserveFile);
			File file = BeanUtils.convertTo(param,File::new);
			file.setId(null);
			file.setRelatedId(reserveId);
			file.setType(FileTypeEnum.MEETING.getValue());
			files.add(file);
		});
		fileService.addBatch(files);
		return saveBatch(reserveFiles);
	}

	@Override
	public Boolean removeByReserveId(Long reserveId) {
		List<MeetingReserveFile> list = list(Wrappers.<MeetingReserveFile>lambdaQuery().select(MeetingReserveFile::getUrl).eq(MeetingReserveFile::getReserveId, reserveId));
		if(CollectionUtil.isNotEmpty(list)){
			List<String> urls = list.stream().map(MeetingReserveFile::getUrl).collect(Collectors.toList());
			fileService.getBaseMapper().delete(Wrappers.<File>lambdaQuery().in(File::getUrl, urls));
		}
		return remove(Wrappers.<MeetingReserveFile>lambdaQuery().eq(MeetingReserveFile::getReserveId, reserveId));
	}

	@Override
	public Boolean edit(MeetingReserveFileParam file) {
        if(ObjectUtil.isEmpty(file)){
            return true;
        }
		MeetingReserveFile reserveFile = BeanUtils.convertTo(file,MeetingReserveFile::new);
		return updateById(reserveFile);
	}

    @Override
    public Boolean edits(List<MeetingReserveFileParam> files) {
        if(ObjectUtil.isEmpty(files)){
            return true;
        }
        List<MeetingReserveFile> reserveFiles = BeanUtils.convertListTo(files,MeetingReserveFile::new);
        return this.updateBatchById(reserveFiles);
    }
}
