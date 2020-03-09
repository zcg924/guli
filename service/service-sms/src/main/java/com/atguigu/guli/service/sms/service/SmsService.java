package com.atguigu.guli.service.sms.service;

import java.util.Map;

/**
 * @author zcgstart
 * @create 2020-03-05 10:13
 */
public interface SmsService {
    void send(String phone, Map<String,Object> param);
}
