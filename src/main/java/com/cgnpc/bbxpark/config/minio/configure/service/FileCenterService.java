package com.cgnpc.bbxpark.config.minio.configure.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.cgnpc.bbxpark.config.minio.model.FileModel;
import com.cgnpc.bbxpark.config.minio.properties.MinioProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service("fileCenterService")
public class FileCenterService {

    @Autowired
    private MinioProperties minioConfiguration;


    /**
     * 文件上传
     * @param file
     * @param fileName
     */
    public FileModel upload(MultipartFile file, String fileName, Long tenantId) {
        String originalfileName = file.getOriginalFilename();
        if (StringUtils.hasText(fileName)) {
            originalfileName = fileName;
        }

        FileModel fileModel = new FileModel();
        String fullPath = buildFileFolder(fileModel, originalfileName,tenantId);

        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(minioConfiguration.getAccessKey(), minioConfiguration.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                            minioConfiguration.getUrl(),
                            minioConfiguration.getBucketName())).build();

            InputStream ins= file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            PutObjectRequest request = new PutObjectRequest(minioConfiguration.getBucketName(), fullPath ,ins, objectMetadata);
            PutObjectResult result = s3Client.putObject(request.withCannedAcl(CannedAccessControlList.PublicRead));

            return fileModel;
        }catch(SdkClientException | IOException e) {
            throw new RuntimeException("文件服务器上传失败");
        }
    }

    private String buildFileFolder(FileModel model, String fileName,Long tenantId){
        String suffix = StrUtil.sub(fileName, fileName.lastIndexOf("."), fileName.length());
        // 根据租户分目录
        String tenantIdStr = tenantId == null ? "tenant-public" : "tenant-" + tenantId;
        //文件夹
        String folder = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm");
        // 文件名称
        String fileKey = UUID.randomUUID().toString().replaceAll("-", "");
        folder = tenantIdStr + "/" + folder;

        String fullPath = folder + "/" + fileKey + suffix;
        fullPath = fullPath.replaceAll("-","/").replaceAll(":","/").replaceAll(" ","/");

        model.setFileName(fileName);
        model.setUrl(minioConfiguration.getUrl()+"/"+minioConfiguration.getBucketName()+"/"+fullPath);
        return fullPath;
    }
}
