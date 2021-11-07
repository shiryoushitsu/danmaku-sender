package com.hikari.danmaku.service.intf;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface IFileService {

    String upload(MultipartFile multipartFile,String requestId) throws IllegalStateException, IOException;

    String createFileLog(String fileName,String requestId) throws IllegalStateException, IOException;

    String getFileLogPath(String fileName,String requestId) throws IllegalStateException, IOException;

    JSONObject getFileLog(String filePath) throws IllegalStateException, IOException;

}
