package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author zcgstart
 * @create 2020-02-19 10:36
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String module, String filename) {

        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        //文件名
        String newFileName = UUID.randomUUID().toString();
        String fileExtention = filename.substring(filename.lastIndexOf("."));

        // 上传文件流。
        //yourObjectName bucket下的路径+文件名
        String dateFolder = new DateTime().toString("yyyy/MM/dd");//yyyy/MM/dd
        //String yourObjectName = module + "/" + dateFolder + "/" + newFileName + fileExtention;
        String yourObjectName = new StringBuffer()
                .append(module)
                .append("/")
                .append(dateFolder)
                .append("/")
                .append(newFileName)
                .append(fileExtention)
                .toString();
        ossClient.putObject(bucketname, yourObjectName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        //返回url地址
        //https://guli924.oss-cn-beijing.aliyuncs.com/avatar/default.jpg
        return new StringBuffer()
                .append("https://")
                .append(bucketname)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(yourObjectName)
                .toString();
    }

    @Override
    public void removeFile(String url) {
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        String host = "https://" + bucketname + "." + endpoint + "/";
        String objectName = url.substring(host.length());
        // 删除文件。
        ossClient.deleteObject(bucketname, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
