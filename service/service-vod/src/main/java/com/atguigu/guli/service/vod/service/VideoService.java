package com.atguigu.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author zcgstart
 * @create 2020-02-27 22:18
 */
public interface VideoService {
    String uploadVideo(InputStream file, String originalFilename);
    void removeVideo(String videoId) throws ClientException;

    Map<String, Object> getVideoUploadAuthAndAddress(String title, String fileName) throws ClientException;

    Map<String, Object> refreshVideoUploadAuth(String videoId) throws ClientException;

    String getVideoPlayAuth(String videoId) throws ClientException;

    void removeVideoByIdList(List<String> videoSourceIdList) throws ClientException;
}
