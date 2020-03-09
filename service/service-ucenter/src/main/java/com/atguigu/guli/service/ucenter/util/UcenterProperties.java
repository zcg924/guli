package com.atguigu.guli.service.ucenter.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zcgstart
 * @create 2020-03-06 19:42
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.open")
public class UcenterProperties {
    private String appid;
    private String appsecret;
    private String redirecturi;
}
