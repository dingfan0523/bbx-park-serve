package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.FileTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.config.minio.configure.service.FileCenterService;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.restaurant.service.*;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FilePageParam;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
public class RestaurantFileServiceImpl implements IRestaurantFileService {

	@Autowired
	private IFileService fileService;

    @Autowired
    private FileCenterService fileCenterService;

    @Autowired
    private IRestaurantCardRecordService restaurantCardRecordService;

    @Autowired
    private IRestaurantInboundRecordService restaurantInboundRecordService;

    @Autowired
    private IRestaurantInventoryRecordService restaurantInventoryRecordService;

    @Autowired
    private IRestaurantWasteRecordService restaurantWasteRecordService;

    @Autowired
    private IUserApiService userApiService;


    @Override
    public ImportReturnModel importFile(MultipartFile file, Integer type) {
        AssertUtils.notNull(type, "文件类型不能为空");
        AssertUtils.isFalse(checkFileName(file.getOriginalFilename(), type), "文件名与已上传的文件一致");
        //文件大小校验
        CommentFileUtils.uploadVerify(20L, file);
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        com.cgnpc.bbxpark.config.minio.model.FileModel fileModel = fileCenterService.upload(file, null,tenantId);
        UserInfoModel user = userApiService.getByStaffNo(WebFrameworkUtils.getHeaderUserId());
        File f = new File();
        f.setCreateBy(user.getUserName());
        f.setName(file.getOriginalFilename());
        f.setUrl(fileModel.getUrl());
        f.setType(type);
        fileService.save(f);
        switch (type) {
            case 31:
                return restaurantCardRecordService.analysisAndCheckExcelData(file, f);
            case 32:
                return restaurantWasteRecordService.analysisAndCheckExcelData(file, f);
            case 33:
                return restaurantInboundRecordService.analysisAndCheckExcelData(file, f);
            case 34:
            default:
                return restaurantInventoryRecordService.analysisAndCheckExcelData(file, f);
        }
    }

    private Boolean checkFileName(String name, Integer type){
        return fileService.count(Wrappers.<File>lambdaQuery()
                .eq(File::getName, name)
                .eq(File::getType, type)
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()),File::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(File::getDeleted, Status.enabled.getKey())) > 0;
    }

    @Override
    public IPage<FileModel> pageFile(FilePageParam param) {
        param.setTypes(Arrays.asList(FileTypeEnum.RESTAURANTCARD.getValue(),FileTypeEnum.RESTAURANTWASTE.getValue(),FileTypeEnum.RESTAURANTINBOUND.getValue(),FileTypeEnum.RESTAURANTINVENTORY.getValue()));
        IPage<File> filePage = fileService.page(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<File>lambdaQuery()
                .in(File::getType, param.getTypes())
                .eq(ObjectUtil.isNotEmpty(param.getType()), File::getType, param.getType())
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), File::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(File::getDeleted, Status.enabled.getKey())
                .like(ObjectUtil.isNotEmpty(param.getName()),File::getName, param.getName()));
        return ConvertUtil.pageConvert(filePage,BeanUtils.convertListTo(filePage.getRecords(),FileModel::new));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeFile(Long id) {
        File file = fileService.getById(id);
        AssertUtils.notNull(file, SystemResultCode.RESULT_DATA_NONE.message());
        fileService.remove(id);
        switch (file.getType()) {
            case 31:
                return restaurantCardRecordService.remove(id);
            case 32:
                return restaurantWasteRecordService.remove(id);
            case 33:
                return restaurantInboundRecordService.remove(id);
            case 34:
            default:
                return restaurantInventoryRecordService.remove(id);
        }
    }
}
