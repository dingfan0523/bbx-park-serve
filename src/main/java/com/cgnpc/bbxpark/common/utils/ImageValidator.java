package com.cgnpc.bbxpark.common.utils;



import com.cgnpc.bbxpark.common.enums.ImageEnum;

import java.io.File;

/**
 * 图片校验工具类
 */
public class ImageValidator {

    /**
     * 获取文件的扩展名
     *
     * @param file 要获取扩展名的文件
     * @return 文件的扩展名
     */
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 检查文件是否为支持的图片格式
     *
     * @param file 要检查的文件
     * @return 如果是支持的图片格式则返回 true，否则返回 false
     */
    public static boolean isValidImageFormat(File file) {
        String extension = getFileExtension(file);
        return ImageEnum.isSupported(extension);
    }

    /**
     * 去除文件名的扩展名
     *
     * @param file 要处理的文件
     * @return 去除扩展名后的文件名
     */
    public static String removeFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
}
