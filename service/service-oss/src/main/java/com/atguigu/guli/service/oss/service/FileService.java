package com.atguigu.guli.service.oss.service;

import java.io.InputStream;

/**
 * @author zcgstart
 * @create 2020-02-19 10:36
 */
public interface FileService {

    String upload(InputStream inputStream, String module, String filename);
    void removeFile(String url);
}
