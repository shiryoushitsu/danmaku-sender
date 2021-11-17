package com.hikari.danmaku.controller;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSONObject;
import com.hikari.danmaku.common.Response;
import com.hikari.danmaku.common.ResponseResult;
import com.hikari.danmaku.service.intf.IDanmakuService;
import com.hikari.danmaku.service.intf.IFileService;
import com.hikari.danmaku.service.intf.ILogService;
import com.hikari.danmaku.utils.AsciiUtil;
import com.hikari.danmaku.entity.FontFamily;
import com.hikari.danmaku.vo.SendDanmakuM7Vo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Api(tags = "弹幕工具箱")
@RestController
@RequestMapping("/tool")
public class ToolController {

    @Value("${api.dmPost}")
    private String dmPostUrl;

    @Autowired
    private IFileService fileService;

    @Autowired
    private ILogService logService;

    @Autowired
    private IDanmakuService danmakuService;


    @ApiOperation("获取视频弹幕xml文件")
    @GetMapping("/getVideoDanmakuXml")
    public ResponseResult getEffectTemM7(@RequestParam String videoUrl) throws Exception {
        String path = System.getProperty("user.dir") + "/effectTem.txt";
        FileReader reader = new FileReader(path);
        JSONObject rspJson = new JSONObject();
        rspJson.put("effectTem",reader.readString());

        List<SendDanmakuM7Vo> m7VoList = new ArrayList<>();
        List<String> linesList = reader.readLines();
        for(String lines : linesList){
            JSONObject jsonObject1 = JSONObject.parseObject(lines);
            SendDanmakuM7Vo m7Vo = JSONObject.toJavaObject(jsonObject1, SendDanmakuM7Vo.class);
            m7VoList.add(m7Vo);
        }

        return Response.makeOKRsp(m7VoList);
    }

    @ApiOperation("生成字符字")
    @GetMapping("/asciiText")
    public ResponseResult getConfig(@RequestParam("content") String content ,@RequestParam("font") String font) throws Exception {
        FontFamily fontFamily = new FontFamily();
        String str =  AsciiUtil.getFontAscii(content,fontFamily);
        JSONObject rspJson = new JSONObject();
        rspJson.put("asciiText", str);
        return Response.makeOKRsp(rspJson);
    }



}
