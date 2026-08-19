package com.cgnpc.bbxpark.workbench.controller;

import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.cud.core.ftp.IFtpClient;
import com.cgnpc.cud.utils.UUIDUtil;
// import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/******************************
 * 用途说明: FTP前端控制器
 * 作者姓名: PXMWLIN
 * 创建时间: 2019/08/27 14:39
 ******************************/
@Controller
@RequestMapping("/ftp")
public class FtpController extends BaseController{

    @Autowired(required = false)
    IFtpClient ftpClient;

    /**********************************
    * 用途说明: 下载文件
    * 参数说明 response
    * 参数说明 fileId
    * 返回值说明:
    ***********************************/
    @GetMapping("/download")
    public void downFile(HttpServletResponse response, String fileId) throws IOException {
        //设置到哪个目录，一般为BusinessType
        ftpClient.setDirectory("test");
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) ftpClient.download(fileId+".xlsx");
        byte[] data = outputStream.toByteArray();
        //response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\""+ UUIDUtil.uuid() +".xlsx"+"\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        // IOUtils.write(data, response.getOutputStream());
    }
}