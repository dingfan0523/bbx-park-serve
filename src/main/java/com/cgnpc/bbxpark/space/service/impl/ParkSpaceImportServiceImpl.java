
package com.cgnpc.bbxpark.space.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFileModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceImportTemporaryModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceModel;
import com.cgnpc.bbxpark.space.dto.model.SpaceImportBatchModel;
import com.cgnpc.bbxpark.space.dto.param.*;
import com.cgnpc.bbxpark.space.service.*;
import com.cgnpc.cud.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cgnpc.bbxpark.common.utils.EasyExcelUtils.downLoadExcel;

@Service("parkSpaceImportService")
@Slf4j
public class ParkSpaceImportServiceImpl implements IParkSpaceImportService {

    private static final long FILE_SIZE = 20;


    @Autowired
    private IParkSpaceImportTemporaryService parkSpaceImportTemporaryService;

    @Autowired
    private ISpaceImportBatchService spaceImportBatchService;

    @Autowired
    private IParkSpaceService parkSpaceService;

    @Autowired
    private ITenantMemberService tenantMemberService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ParkSpaceFileModel importSpace(ParkSpaceDataParam param) {
        String batchCode = param.getBatchCode();
        List<ParkSpaceImportTemporaryParam> parkSpaceImportTemporaryParams = param.getParkSpaceImportTemporaryParams();

        ParkSpaceFileModel parkSpaceFileModel = new ParkSpaceFileModel();
        SpaceImportBatchParam spaceImportBatchParam = new SpaceImportBatchParam();
        List<ParkSpaceImportTemporaryModel> parkSpaceImportTemporaryModels = BeanUtils.convertListTo(parkSpaceImportTemporaryParams, ParkSpaceImportTemporaryModel::new);
        List<ParkSpaceFileParam> parkSpaceFileParamList = param.getParkSpaceFileParamList();
        List<ParkSpaceModel> parkSpaceList = parkSpaceService.findParkSpaceList(WebFrameworkUtils.getHeaderTenantId());
        Integer startNum = parkSpaceList.size();
        Map<String, ParkSpaceModel> spaceCodeMap = parkSpaceList.stream().collect(Collectors.toMap(ParkSpaceModel::getSpaceCode, Function.identity(), (key1, key2) -> key2));
        Map<String, List<ParkSpaceModel>> spaceNameMap = parkSpaceList.stream().collect(Collectors.groupingBy(a -> a.getSpaceName()));
        ListIterator<ParkSpaceFileParam> paramListIterator = parkSpaceFileParamList.listIterator();
        while (paramListIterator.hasNext()) {
            ParkSpaceFileParam parkSpaceFileParam = paramListIterator.next();
            if (spaceCodeMap.containsKey(parkSpaceFileParam.getSpaceCode())) {
                createTempAndReturn(parkSpaceFileParam, parkSpaceImportTemporaryModels, parkSpaceImportTemporaryParams, batchCode, "空间编码重复");
                paramListIterator.remove();
            } else if (spaceNameMap.containsKey(parkSpaceFileParam.getSpaceName())) {
                List<ParkSpaceModel> parkSpaceModels = spaceNameMap.get(parkSpaceFileParam.getSpaceName());
                List<String> parentSpaceCodes = parkSpaceModels.stream().map(ParkSpaceModel::getParentSpaceCode).collect(Collectors.toList());
                if (parentSpaceCodes.contains(parkSpaceFileParam.getParentSpaceCode())) {
                    createTempAndReturn(parkSpaceFileParam, parkSpaceImportTemporaryModels, parkSpaceImportTemporaryParams, batchCode, "同父级空间名称重复");
                    paramListIterator.remove();
                }
            } else if (spaceCodeMap.containsKey(parkSpaceFileParam.getParentSpaceCode())) {
                ParkSpaceParam parkSpaceParam = BeanUtil.toBean(parkSpaceFileParam, ParkSpaceParam.class);
                parkSpaceParam.setParentSpaceId(spaceCodeMap.get(parkSpaceFileParam.getParentSpaceCode()).getId());
                ParkSpaceModel parkSpaceModel = parkSpaceService.add(parkSpaceParam);
                parkSpaceParam.setId(parkSpaceModel.getId());
                parkSpaceList.add(BeanUtil.toBean(parkSpaceParam, ParkSpaceModel.class));
                paramListIterator.remove();
            }
        }
        if (CollUtil.isNotEmpty(parkSpaceFileParamList)) {
            insertParkSpace(parkSpaceList, parkSpaceFileParamList);
        }
        Integer endNum = parkSpaceList.size();
        for (ParkSpaceFileParam parkSpaceFileParam : parkSpaceFileParamList) {
            //组装临时表和返回参数
            createTempAndReturn(parkSpaceFileParam, parkSpaceImportTemporaryModels, parkSpaceImportTemporaryParams, batchCode, "上级空间编码不存在");
        }

        spaceImportBatchParam.setBatchCode(batchCode);
        spaceImportBatchParam.setImportAllNum(parkSpaceImportTemporaryParams.size() + endNum - startNum);
        spaceImportBatchParam.setImportErrorNum(parkSpaceImportTemporaryModels.size());
        spaceImportBatchParam.setImportSuccessNum(endNum - startNum);
        spaceImportBatchParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        //批量插入临时表
        parkSpaceImportTemporaryService.batchAdd(parkSpaceImportTemporaryParams);
        //插入批次表
        spaceImportBatchService.add(spaceImportBatchParam);
        parkSpaceFileModel.setSpaceImportBatchModel(BeanUtil.toBean(spaceImportBatchParam, SpaceImportBatchModel.class));
        parkSpaceFileModel.setParkSpaceImportTemporaryModels(parkSpaceImportTemporaryModels);
        return parkSpaceFileModel;
    }


    /**
     * 根据批次号查询错误信息列表.
     *
     * @Param batchCode 批次
     * @Return 临时表集合
     */
    @Override
    public List<ParkSpaceImportTemporaryModel> findByBatchCodeParkSpaceList(ParkSpaceImportTemporaryListParam param) {
        return parkSpaceImportTemporaryService.findAllList(param);
    }


    /**
     * 组装临时表和返回参数
     */
    public void createTempAndReturn(ParkSpaceFileParam parkSpaceFileParam, List<ParkSpaceImportTemporaryModel> parkSpaceImportTemporaryModels,
                                    List<ParkSpaceImportTemporaryParam> parkSpaceImportTemporaryParams, String batchCode, String importErrorDesc) {
        ParkSpaceImportTemporaryModel parkSpaceImportTemporaryModel = BeanUtil.toBean(parkSpaceFileParam,ParkSpaceImportTemporaryModel.class);
        parkSpaceImportTemporaryModel.setBatchCode(batchCode);
        parkSpaceImportTemporaryModel.setImportErrorDesc(importErrorDesc);
        parkSpaceImportTemporaryModels.add(parkSpaceImportTemporaryModel);

        ParkSpaceImportTemporaryParam parkSpaceImportTemporaryParam = BeanUtil.toBean(parkSpaceFileParam,ParkSpaceImportTemporaryParam.class);
        parkSpaceImportTemporaryParam.setBatchCode(batchCode);
        parkSpaceImportTemporaryParam.setImportErrorDesc(importErrorDesc);
        parkSpaceImportTemporaryParams.add(parkSpaceImportTemporaryParam);
    }


    /**
     * 遍历插入空间表
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertParkSpace(List<ParkSpaceModel> parkSpaceList, List<ParkSpaceFileParam> parkSpaceFileParamList) {
        Boolean insertFlag = Boolean.FALSE;
        Map<String, ParkSpaceModel> spaceCodeMap = parkSpaceList.stream().collect(Collectors.toMap(ParkSpaceModel::getSpaceCode, Function.identity(), (key1, key2) -> key2));
        ListIterator<ParkSpaceFileParam> paramListIterator = parkSpaceFileParamList.listIterator();
        while (paramListIterator.hasNext()) {
            ParkSpaceFileParam parkSpaceFileParam = paramListIterator.next();
            if (spaceCodeMap.containsKey(parkSpaceFileParam.getParentSpaceCode())) {
                ParkSpaceParam parkSpaceParam = BeanUtil.toBean(parkSpaceFileParam, ParkSpaceParam.class);
                parkSpaceParam.setParentSpaceId(spaceCodeMap.get(parkSpaceFileParam.getParentSpaceCode()).getId());
                ParkSpaceModel parkSpaceModel = parkSpaceService.add(parkSpaceParam);
                parkSpaceParam.setId(parkSpaceModel.getId());
                parkSpaceList.add(BeanUtil.toBean(parkSpaceParam, ParkSpaceModel.class));
                paramListIterator.remove();
                insertFlag = Boolean.TRUE;
            }
        }
        if (CollUtil.isNotEmpty(parkSpaceFileParamList) && insertFlag) {
            insertParkSpace(parkSpaceList, parkSpaceFileParamList);
        }
    }

    /**
     * 校验是否拥有本租户管理员权限
     * @return 是否拥有权限 true:是 false:否
     */
    @Override
    public Boolean checkTentAdminAuthority(){
        return CollUtil.isNotEmpty(findTenantIdByUserId());
    }

    /**
     * 查询用户拥有的租户管理员id集合
     * @return 租户id集合
     */
    private List<Long> findTenantIdByUserId(){
        String userId = WebFrameworkUtils.getHeaderUserId();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        tenantId = 24L;
        if(userId == null){
            return Collections.emptyList();
        }
        if(tenantId == null){
            return Collections.emptyList();
        }
        TenantMemberListParam param = new TenantMemberListParam();
        param.setUserId(userId);
        param.setTenantId(tenantId);
        List<TenantMemberDomain> list = tenantMemberService.list(param);
        return list.stream().filter(domain->domain.getIdentity() != null && domain.getIdentity() == 1).map(TenantMemberDomain::getTenantId).collect(Collectors.toList());
    }

    public ParkSpaceFileModel importSpaceFile(MultipartFile file) {
        //判断是否为本租户管理员
        AssertUtils.isTrue(checkTentAdminAuthority(), SystemResultCode.PERMISSION_UNAUTHORISE.message());
        CommentFileUtils.uploadVerify(FILE_SIZE,file);
        //解析总集合
        List<ParkSpaceFileParam> fileParamList = new ArrayList<>();
        analysisExcelData(fileParamList, file);
        ParkSpaceFileModel returnParkSpaceFileModel = new ParkSpaceFileModel();
        List<ParkSpaceFileParam> excelPassFileParamList = new ArrayList<>();
        //excel校验不通过数据集合
        List<ParkSpaceImportTemporaryParam> excelParkSpaceImportTemporaryParams = new ArrayList<>();
        ParkSpaceDataParam parkSpaceDataParam = new ParkSpaceDataParam();
        String batchCode = String.valueOf(System.currentTimeMillis());
        checkImportData(batchCode, fileParamList, returnParkSpaceFileModel, excelPassFileParamList, excelParkSpaceImportTemporaryParams);
        parkSpaceDataParam.setBatchCode(batchCode);
        parkSpaceDataParam.setParkSpaceFileParamList(excelPassFileParamList);
        parkSpaceDataParam.setParkSpaceImportTemporaryParams(excelParkSpaceImportTemporaryParams);
        return this.importSpace(parkSpaceDataParam);
    }

    private void analysisExcelData(List<ParkSpaceFileParam> fileParamList, MultipartFile file) {
        BusinessParkSpaceClassListener deviceClassDataListener = new BusinessParkSpaceClassListener();
        try {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), ParkSpaceFileParam.class, deviceClassDataListener).sheet().doRead();
        } catch (Exception e) {
            log.error("空间导入异常，原因是{}", e.getMessage());
        }
        fileParamList.addAll(deviceClassDataListener.getAddList());
        if (CollUtil.isEmpty(fileParamList)) {
            throw new BaseException("文件内容不能为空！");
//            throw GenericException.fail("文件内容不能为空！");
        }
    }

    private void checkImportData(String batchCode, List<ParkSpaceFileParam> params, ParkSpaceFileModel returnParkSpaceFileModel, List<ParkSpaceFileParam> excelPassFileParamList, List<ParkSpaceImportTemporaryParam> excelParkSpaceImportTemporaryParams) {
        params.forEach(f -> f.setNum(IDHelper.uuid()));
        List<ParkSpaceFileParam> spaceFileFailData = params.stream().filter(f -> StrUtil.isNotBlank(f.getImportErrorDesc())).collect(Collectors.toList());
        List<ParkSpaceFileParam> spaceFileSuccessReasons = params.stream().filter(f -> StrUtil.isBlank(f.getImportErrorDesc())).collect(Collectors.toList());
        SpaceImportBatchModel spaceImportBatchModel = new SpaceImportBatchModel();
        List<ParkSpaceImportTemporaryModel> parkSpaceImportTemporaryModels = new ArrayList<>();
        spaceImportBatchModel.setBatchCode(batchCode);
        for (ParkSpaceFileParam p : spaceFileFailData) {
            //组装临时表和返回参数
            createTempAndReturn(p, parkSpaceImportTemporaryModels, excelParkSpaceImportTemporaryParams, batchCode, p.getImportErrorDesc());
        }

        Map<String, List<ParkSpaceFileParam>> spaceCodeMap = spaceFileSuccessReasons.stream().collect(Collectors.groupingBy(ParkSpaceFileParam::getSpaceCode));
        spaceCodeMap.forEach((k, v) -> {
            if (spaceCodeMap.get(k).size() == 1) {
                excelPassFileParamList.add(spaceCodeMap.get(k).get(0));
            } else {
                List<ParkSpaceFileParam> fileParamList = spaceCodeMap.get(k);
                for (int i = 0; i < fileParamList.size(); i++) {
                    ParkSpaceFileParam fileParam = fileParamList.get(i);
                    if (i == 0) {
                        excelPassFileParamList.add(fileParam);
                    } else {
                        createTempAndReturn(fileParam, parkSpaceImportTemporaryModels, excelParkSpaceImportTemporaryParams, batchCode, "空间编码重复");
                    }
                }
            }
        });

        Map<String, List<ParkSpaceFileParam>> parentSpaceCodeMap = excelPassFileParamList.stream().collect(Collectors.groupingBy(ParkSpaceFileParam::getParentSpaceCode));
        parentSpaceCodeMap.forEach((k, v) -> {
            if (ObjectUtil.isNotEmpty(parentSpaceCodeMap.get(k)) && parentSpaceCodeMap.get(k).size() > 1) {
                List<ParkSpaceFileParam> fileParamList = parentSpaceCodeMap.get(k);
                Map<String, List<ParkSpaceFileParam>> spaceNameMap = fileParamList.stream().collect(Collectors.groupingBy(ParkSpaceFileParam::getSpaceName));
                spaceNameMap.forEach((k1, v1) -> {
                    if (ObjectUtil.isNotEmpty(spaceNameMap.get(k1)) && spaceNameMap.get(k1).size() > 1) {
                        List<ParkSpaceFileParam> spaceFileParams = spaceNameMap.get(k1);
                        for (int i = 0; i < spaceFileParams.size(); i++) {
                            ParkSpaceFileParam parkSpaceFileParam = spaceFileParams.get(i);
                            if (i > 0) {
                                //组装临时表和返回参数
                                createTempAndReturn(parkSpaceFileParam, parkSpaceImportTemporaryModels, excelParkSpaceImportTemporaryParams, batchCode, "同父级空间名称重复");
                            }
                        }
                    }
                });
            }
        });
        List<ParkSpaceFileParam> paramList = params.stream().filter(item -> !excelParkSpaceImportTemporaryParams.stream().map(ParkSpaceImportTemporaryParam::getNum).collect(Collectors.toList()).contains(item.getNum())).collect(Collectors.toList());
        excelPassFileParamList.clear();
        excelPassFileParamList.addAll(paramList);
        returnParkSpaceFileModel.setSpaceImportBatchModel(spaceImportBatchModel);
        returnParkSpaceFileModel.setParkSpaceImportTemporaryModels(parkSpaceImportTemporaryModels);
    }

    public void downloadSpaceTemplate(HttpServletResponse response, HttpServletRequest request) {
         AssertUtils.isTrue(checkTentAdminAuthority(), SystemResultCode.PERMISSION_UNAUTHORISE.message());
        try {
            ExportParams exportParams = new ExportParams();
            exportParams.setCreateHeadRows(true);
            exportParams.setType(ExcelType.XSSF);
            exportParams.setSheetName("空间信息导入模板");
            ArrayList<ParkSpaceTemplateParam> list = new ArrayList<>();
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ParkSpaceTemplateParam.class, list);
            downLoadExcel("空间信息导入模板",response,workbook);
        } catch (Exception e) {
            log.error("空间信息导入模板下载失败，原因是{}", e);
        }
    }

    public void downloadFailData(HttpServletResponse response, String batchCode) {
        //判断是否为本租户管理员
        AssertUtils.isTrue(checkTentAdminAuthority(), SystemResultCode.PERMISSION_UNAUTHORISE.message());
        try {
            ParkSpaceImportTemporaryListParam param = new ParkSpaceImportTemporaryListParam();
            param.setBatchCode(batchCode);

            List<ParkSpaceImportTemporaryModel> temporaryModels = findByBatchCodeParkSpaceList(param);
            ExportParams exportParams = new ExportParams();
            exportParams.setCreateHeadRows(true);
            exportParams.setType(ExcelType.XSSF);
            exportParams.setSheetName("校验失败数据");
            List<ParkSpaceTempOraryParam> list = BeanUtils.convertListTo(temporaryModels, ParkSpaceTempOraryParam::new);
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ParkSpaceTempOraryParam.class, list);
            CellStyle style = workbook.createCellStyle();
            //设置自动换行
            style.setWrapText(true);
            downLoadExcel("校验失败数据", response, workbook);
        } catch (Exception e) {
            log.error("校验失败数据下载失败，原因是{}", e);
        }
    }

}
