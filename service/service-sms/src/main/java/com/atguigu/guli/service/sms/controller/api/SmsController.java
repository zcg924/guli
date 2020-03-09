package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.RandomUtils;
import com.atguigu.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zcgstart
 * @create 2020-03-04 22:47
 */
@RestController
@RequestMapping("/api/sms")
@Api(description = "短信管理")
@Slf4j
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("send/{phone}")
    public R getCode(@PathVariable String phone){

        //生成验证码
        String code = RandomUtils.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);

        //发送短信验证码
        //smsService.send(phone, param);

        //将验证码存入redis缓存
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }
}
