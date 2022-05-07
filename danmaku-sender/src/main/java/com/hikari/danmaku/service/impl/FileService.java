package com.hikari.danmaku.service.impl;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSONObject;
import com.hikari.danmaku.service.intf.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileService implements IFileService {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private SimpleDateFormat nyFomart = new SimpleDateFormat("yyyyMM");

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(FileService.class);


    @Override
    public String upload(MultipartFile multipartFile,String requestId) throws IllegalStateException, IOException {
        //文件根路径为项目根路径
        String path = System.getProperty("user.dir");

        String originalFilename = multipartFile.getOriginalFilename();
        long fileSize = multipartFile.getSize();
        if (fileSize > (10 * 1024 * 1024)) {
            return "文件超过10MB";
        }
        String[] strings = originalFilename.split("\\.");
        String fileName = strings[0] +"_" + requestId;
        String fileType = "txt";
        if (strings.length > 1) {
            fileType = strings[1];
        }
        String filePath = path + "/danmakuLog/" + sdf.format(new Date()) + "/" + fileName + "." + fileType;
        if (!createFile(filePath)) {
            throw new IOException("创建文件失败");
        }
        System.out.println("文件路径====" + filePath);
        File file = new File(filePath);
        multipartFile.transferTo(file);
        return filePath;
    }


    @Override
    public String createFileLog(String fileLogName,String requestId) throws IllegalStateException, IOException {
        String originalFilename = fileLogName;
        String[] strings = originalFilename.split("\\.");
        String fileName = strings[0] +"_" + requestId + "_log";

        String filePath = System.getProperty("user.dir") + "/danmakuLog/" + sdf.format(new Date()) + "/" + fileName + ".txt";
        if (!createFile(filePath)) {
            throw new IOException("创建文件失败");
        }
        System.out.println("文件日志路径====" + filePath);
        return filePath;
    }

    @Override
    public String getFileLogPath(String fileLogName,String requestId) throws IllegalStateException, IOException {
        String originalFilename = fileLogName;
        String[] strings = originalFilename.split("\\.");
        String fileName = strings[0] +"_" + requestId + "_log";
        String filePath = System.getProperty("user.dir") + "/danmakuLog/" + sdf.format(new Date()) + "/" + fileName + ".txt";
        return filePath;
    }


    @Override
    public JSONObject getFileLog(String fileLogPath) throws IllegalStateException, IOException {
        FileReader reader = new FileReader(fileLogPath);
        JSONObject rspJson = new JSONObject();
        rspJson.put("txtLog",reader.readString());
        return rspJson;
    }


    private synchronized boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if(file.exists()) {//失败，目标文件已存在！
            return false;
        }
        if (destFileName.endsWith(File.separator)) {//失败，目标文件不能为目录！
            return false;
        }
        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            //目标文件所在目录不存在，准备创建它！
            if(!file.getParentFile().mkdirs()) {//创建目标文件所在目录失败！
                return false;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {//成功！
                return true;
            } else { //失败！
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
