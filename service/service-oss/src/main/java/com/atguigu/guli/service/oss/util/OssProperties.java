package com.atguigu.guli.service.oss.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zcgstart
 * @create 2020-02-19 10:30
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss.file")
public class OssProperties {

    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;
}
