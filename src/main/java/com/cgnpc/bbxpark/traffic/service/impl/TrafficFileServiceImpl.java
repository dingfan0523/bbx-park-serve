package com.cgnpc.bbxpark.traffic.service.impl;

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
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FilePageParam;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.traffic.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
public class TrafficFileServiceImpl implements ITrafficFileService {

	@Autowired
	private IFileService fileService;

    @Autowired
    private FileCenterService fileCenterService;

    @Autowired
    private IDwdVehicleInfoService dwdVehicleInfoService;

    @Autowired
    private IDwdVehicleMaintainService dwdVehicleMaintainService;

    @Autowired
    private IDwdVehiclePartReplaceService dwdVehiclePartReplaceService;

    @Autowired
    private IDwdVehicleRepairService dwdVehicleRepairService;

    @Autowired
    private IDwdVehicleCarRentApplyService dwdVehicleCarRentApplyService;

    @Autowired
    private IDwdVehicleDriverInfoService dwdVehicleDriverInfoService;

    @Autowired
    private IDwdVehicleMonthlySettlementService dwdVehicleMonthlySettlementService;

    @Autowired
    private IDwdVehicleCarTaskRecordService dwdVehicleCarTaskRecordService;
    @Autowired
    private IVehicleApplyService vehicleApplyService;
    @Autowired
    private IVehicleLineOrderInfoService vehicleLineOrderInfoService;

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
            case 41:
                return dwdVehicleInfoService.analysisAndCheckExcelData(file, f);
            case 42:
                return dwdVehicleMaintainService.analysisAndCheckExcelData(file, f);
            case 43:
                return dwdVehiclePartReplaceService.analysisAndCheckExcelData(file, f);
            case 44:
                return dwdVehicleRepairService.analysisAndCheckExcelData(file, f);
            case 45:
                return dwdVehicleCarRentApplyService.analysisAndCheckExcelData(file, f);
            case 46:
                return dwdVehicleDriverInfoService.analysisAndCheckExcelData(file, f);
            case 47:
                return dwdVehicleCarTaskRecordService.analysisAndCheckExcelData(file, f);
            case 48:
                return vehicleApplyService.analysisAndCheckExcelData(file,f);
            case 49:
                return vehicleLineOrderInfoService.analysisAndCheckExcelData(file,f);
            case 50:
            default:
                return dwdVehicleMonthlySettlementService.analysisAndCheckExcelData(file,f);
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
        param.setTypes(Arrays.asList(FileTypeEnum.DWDVEHICLEAPPLY.getValue(),FileTypeEnum.DWDVEHICLELINEORDERINFO.getValue(),
                FileTypeEnum.DWDVEHICLECARRENTAPPLY.getValue(),FileTypeEnum.DWDVEHICLECARTASKRECORD.getValue(),
                FileTypeEnum.DWDVEHICLEINFO.getValue(),FileTypeEnum.DWDVEHICLEDRIVERINFO.getValue(),
                FileTypeEnum.DWDVEHICLEREPAIR.getValue(),FileTypeEnum.DWDVEHICLEPARTREPLACE.getValue(),
                FileTypeEnum.DWDVEHICLEMAINTAIN.getValue()));
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
            case 41:
                return dwdVehicleInfoService.remove(id);
            case 42:
                return dwdVehicleMaintainService.remove(id);
            case 43:
                return dwdVehiclePartReplaceService.remove(id);
            case 44:
                return dwdVehicleRepairService.remove(id);
            case 45:
                return dwdVehicleCarRentApplyService.remove(id);
            case 46:
                return dwdVehicleDriverInfoService.remove(id);
            case 47:
                return dwdVehicleCarTaskRecordService.remove(id);
            case 48:
                return vehicleApplyService.remove(id);
            case 49:
                return vehicleLineOrderInfoService.remove(id);
            case 50:
            default:
                return dwdVehicleMonthlySettlementService.remove(id);

        }
    }

    @Override
    public Boolean syncData(Integer type) {
        switch (type) {
            case 41:
                return dwdVehicleInfoService.importData();
            case 42:
                return dwdVehicleMaintainService.importData();
            case 43:
                return dwdVehiclePartReplaceService.importData();
            case 44:
                return dwdVehicleRepairService.importData();
            case 45:
                return dwdVehicleCarRentApplyService.importData();
            case 46:
                return dwdVehicleDriverInfoService.importData();
            case 49:
                return vehicleLineOrderInfoService.importData();
            case 47:
            default:
                return dwdVehicleCarTaskRecordService.importData();
        }
    }
}
