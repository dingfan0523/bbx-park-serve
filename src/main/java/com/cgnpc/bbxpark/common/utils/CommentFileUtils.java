package com.cgnpc.bbxpark.common.utils;


import com.cgnpc.cud.core.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author lhy
 * @description
 * @date 2023/11/15 11:34
 */
public class CommentFileUtils {
    private static final Logger log = LoggerFactory.getLogger(CommentFileUtils.class);

    /**
     * 文件后缀 支持的类型
     */
    private static final String[] FILE_SUFFIX_SUPPORT = {".xlsx", ".xls"};


    /**
     * 上传文件校验大小、名字、后缀
     *
     * @param multipartFile multipartFile
     */
    public static void uploadVerify(Long fileSize, MultipartFile multipartFile) {
        // 校验文件大小
        long size = multipartFile.getSize();
        if (size > fileSize * 1024 * 1024L) {
            throw new BaseException("文件大小不能超过" + fileSize + "MB！");
//            throw GenericException.fail("文件大小不能超过" + fileSize + "MB！");
        }

        // 校验文件名字
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new BaseException("文件名字不能为空！");
//            throw GenericException.fail("文件名字不能为空！");
        }

        // 校验文件后缀
        if (!originalFilename.contains(".")) {
            throw new BaseException("文件不能没有后缀！");
//            throw GenericException.fail("文件不能没有后缀！");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        // 获取文件类型
        String fileType = multipartFile.getContentType();
        if (null == fileType) {
            throw new BaseException("获取不到文件类型！");
//            throw GenericException.fail("获取不到文件类型！");
        }

        boolean flag = true;
        for (String s : FILE_SUFFIX_SUPPORT) {
            if (s.equals(suffix.toLowerCase(Locale.ROOT))) {
                flag = false;
                break;
            }
        }

        if (flag) {
            throw new BaseException("文件格式仅限于" + Arrays.toString(FILE_SUFFIX_SUPPORT) + "！");
//            throw GenericException.fail("文件格式仅限于" + Arrays.toString(FILE_SUFFIX_SUPPORT) + "！");
        }
    }


}
 