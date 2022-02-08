package com.hikari.danmaku.controller;

import com.alibaba.fastjson.JSONObject;
import com.hikari.danmaku.common.Response;
import com.hikari.danmaku.common.ResponseResult;
import com.hikari.danmaku.entity.Ass;
import com.hikari.danmaku.entity.AviutlExo;
import com.hikari.danmaku.entity.XmlEffect;
import com.hikari.danmaku.service.intf.IDanmakuService;
import com.hikari.danmaku.service.intf.IFileService;
import com.hikari.danmaku.service.intf.ILogService;
import com.hikari.danmaku.utils.ExoUtil;
import com.hikari.danmaku.utils.Lrc2ExoUtil;
import com.hikari.danmaku.vo.XmlEffectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hikari.danmaku.utils.LrcUtil.danmakuList;


@Api(tags = "AUL高级弹幕")
@RestController
@RequestMapping("/aul")
public class AulController {

    @Value("${api.dmPost}")
    private String dmPostUrl;

    @Autowired
    private IFileService fileService;

    @Autowired
    private ILogService logService;

    @Autowired
    private IDanmakuService danmakuService;

    @ApiOperation("LRC歌词转EXO")
    @PostMapping("/lrc2exo")
    public ResponseResult<Map<String,Object>> lrc2exo(MultipartFile file, XmlEffectVo xmlEffectVo) throws Exception{
        //1、处理文件
        String filePath = this.fileService.upload(file, xmlEffectVo.getRequestId());
        String originalFilename = file.getOriginalFilename();
        String prefix=originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        List<Ass> danmakuList = new ArrayList<>();
        boolean m1Flag = false;

        //2、解析文件
        if("lrc".equals(prefix)){
            danmakuList = Lrc2ExoUtil.lrcToAssListV2(filePath);
        }else if("ass".equals(prefix)){
            danmakuList = danmakuList(filePath);
        }else {
            return Response.makeErrRsp("");
        }
        String lrfExoStr = Lrc2ExoUtil.createLrcExo(danmakuList);
        JSONObject rspJson = new JSONObject();
        rspJson.put("lrfExoStr", lrfExoStr);
        return Response.makeOKRsp(rspJson);
    }

    @ApiOperation("EXO歌词转XML")
    @PostMapping("/exo2xml")
    public ResponseResult<Map<String,Object>> exo2xml(MultipartFile file, XmlEffectVo xmlEffectVo) throws Exception{
        //1、处理文件
        String filePath = this.fileService.upload(file, xmlEffectVo.getRequestId());
        String originalFilename = file.getOriginalFilename();
        String prefix = originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        //2、解析文件
        if(!"exo".equals(prefix)){
            return Response.makeErrRsp("");
        }
        //1.解析exo文本返回分段List
        List<Map<String,String>> exoList = ExoUtil.parseExoToList(filePath);

        XmlEffect XmlEffect = xmlEffectVo;
//        XmlEffect.setOutline(false);
//        XmlEffect.setLayer(true);
//        XmlEffect.setLinearSpeedup(false);
//        XmlEffect.setAdvanceStartTime(false);
//        XmlEffect.setDelayEndTime(true);
//        XmlEffect.setForceLine(false);//是否换行都转成新文本
//        XmlEffect.setPercentage(true);//是否百分比
//        XmlEffect.setColor10(true);

        //2.将分段List优化为exo对象list
        List<AviutlExo> exoObjectList = ExoUtil.parseExoList(exoList, XmlEffect);

        //3.将处理好的exo对象list转为m7xml
        String xmlStr = ExoUtil.exoToXml(exoObjectList);

        //ex.预览
        String previewXmlStr = ExoUtil.exoToPreviewXml(exoObjectList);
        String preStr = "let s=`"+ previewXmlStr.replace("\n","") +"`" + "\n";
        preStr = preStr + "ldanmu=xml2danmu(s)"+ "\n";
        preStr = preStr + "window.ldldanmu[window.ldldanmu.length-1].ldanmu=ldanmu";

        JSONObject rspJson = new JSONObject();
        rspJson.put("xmlStr", xmlStr);
        rspJson.put("preStr", preStr);
        return Response.makeOKRsp(rspJson);
    }



}
