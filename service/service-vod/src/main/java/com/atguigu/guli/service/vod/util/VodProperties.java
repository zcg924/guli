package com.atguigu.guli.service.vod.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zcgstart
 * @create 2020-02-27 22:18
 */
@Data
@Component
//注意prefix要写到最后一个 "." 符号之前
@ConfigurationProperties(prefix="aliyun.vod.file")
public class VodProperties {
    private String keyid;
    private String keysecret;
}
