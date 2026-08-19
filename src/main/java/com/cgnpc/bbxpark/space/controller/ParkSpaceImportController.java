package com.cgnpc.bbxpark.space.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFileModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceImportTemporaryModel;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceImportTemporaryListParam;
import com.cgnpc.bbxpark.space.service.IParkSpaceImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(Constant.BASE_PATH + "/park/space/import")
@Api(tags = "BBX-园区空间导入")
public class ParkSpaceImportController {



    /**
     * 园区空间导入临时服务接口.
     */
    @Autowired
    private IParkSpaceImportService parkSpaceImportService;

    /**
     * 空间信息导入
     */
 /*   @ApiOperation(value = "空间信息导入")
    @Parameter(name = "params")
    @PostMapping(value = "/importSpace")
    public CudResult<ParkSpaceFileModel> importSpace(@RequestBody ParkSpaceDataParam param)  {
        return  CudResult.success(parkSpaceImportService.importSpace(param));
    }*/



    /**
     * 根据批次号查询错误信息列表.
     *
     * @Param batchCode 批次
     * @Return 临时表集合
     */
    @ApiOperation(value = "根据批次号查询错误信息列表")
    @PostMapping(value = "/findByBatchCodeParkSpaceList")
    public CudResult<List<ParkSpaceImportTemporaryModel>> findByBatchCodeParkSpaceList(@RequestBody ParkSpaceImportTemporaryListParam param) {
        return CudResult.success(parkSpaceImportService.findByBatchCodeParkSpaceList(param));
    }


    /**
     * 判断用户是否为租户管理员.
     *
     * @Param
     * @Return true 是 ,false 否
     */
    @ApiOperation(value = "判断用户是否为租户管理员")
    @PostMapping(value = "/checkTentAdminAuthority")
    public CudResult<Boolean> checkTentAdminAuthority() {
        return CudResult.success(parkSpaceImportService.checkTentAdminAuthority());
    }


    @ApiOperation(value = "空间信息导入")
    @PostMapping(value = "/importSpace")
    public CudResult<ParkSpaceFileModel> importSpace(@RequestParam(value = "file") MultipartFile file) {
        return CudResult.success(parkSpaceImportService.importSpaceFile(file));
    }


    /**
     * 下载空间信息导入模板
     *
     */
    @ApiOperation(value = "下载空间信息导入模板")
    @GetMapping(value = "/downloadSpaceTemplate")
    public void downloadSpace(HttpServletResponse response, HttpServletRequest request)  throws IOException{
        parkSpaceImportService.downloadSpaceTemplate(response,request);
    }


    /**
     * 下载校验失败数据
     *
     */
    @ApiOperation(value = "下载校验失败数据")
    @GetMapping(value = "/downloadFailData")
    public void downloadFailData(HttpServletResponse response,@RequestParam String batchCode) {
        parkSpaceImportService.downloadFailData(response,batchCode);
    }

    @GetMapping("/test-simple")
    public void testSimpleExcel(HttpServletResponse response) throws IOException {
        // 完全绕过Spring的序列化，直接写二进制
        byte[] excelBytes = createSimpleExcelBytes();

        // 设置响应类型和长度
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setContentLength(excelBytes.length);
        response.setHeader("Content-Disposition", "attachment; filename=test.xlsx");

        // 直接写二进制，不通过任何转换
        ServletOutputStream out = response.getOutputStream();
        out.write(excelBytes);
        out.flush();
    }
    private byte[] createSimpleExcelBytes() throws IOException {
        // 创建一个最简单的Excel文件
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Test");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Test");

            // 写入字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        }
    }
}
