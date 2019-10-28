package com.recruiting.oss.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.recruiting.oss.utils.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService {

    public String uploadFile(MultipartFile file) throws Exception {
        String endPoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String fileHost = ConstantPropertiesUtil.FILE_HOST;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileType;
        String filePath = "/" + new SimpleDateFormat("yyyy-MM").format(new Date()) + "/" + fileName;

        // 上传文件流。
        InputStream inputStream = file.getInputStream();
        ossClient.putObject(bucketName, fileHost + filePath, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        return "http://" +"www.oss.imsadan.club/" + fileHost + filePath;
    }
}
