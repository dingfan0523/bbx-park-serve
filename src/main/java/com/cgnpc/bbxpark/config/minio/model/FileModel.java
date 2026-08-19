package com.cgnpc.bbxpark.config.minio.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * URL地址
     */
    private String url;

    /**
     * sys_oss表id
     */
    private String ossId;

    /**
     * 文件名
     */
    private String folder;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 全路径
     */
    private String fullPath;
}
