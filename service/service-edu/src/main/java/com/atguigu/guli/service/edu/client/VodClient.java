package com.atguigu.guli.service.edu.client;

import com.atguigu.guli.common.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author zcgstart
 * @create 2020-03-03 10:24
 */
@Component
@FeignClient("guli-vod")
public interface VodClient {

    @DeleteMapping(value = "/admin/vod/video/remove")
    R removeVideoByIdList(@RequestBody List<String> videoSourceIdList);
}
