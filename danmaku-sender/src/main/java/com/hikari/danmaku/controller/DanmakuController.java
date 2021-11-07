package com.hikari.danmaku.controller;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.hikari.danmaku.common.Response;
import com.hikari.danmaku.common.ResponseResult;
import com.hikari.danmaku.entity.BaseDanmaku;
import com.hikari.danmaku.entity.SeniorDanmaku;
import com.hikari.danmaku.service.intf.IDanmakuService;
import com.hikari.danmaku.service.intf.IFileService;
import com.hikari.danmaku.service.intf.ILogService;
import com.hikari.danmaku.utils.*;
import com.hikari.danmaku.entity.Ass;
import com.hikari.danmaku.vo.SendDanmakuM1Vo;
import com.hikari.danmaku.vo.SendDanmakuM7Vo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hikari.danmaku.service.impl.LogService.*;
import static com.hikari.danmaku.utils.DanmakuUtil.wrapperDanmaku;
import static com.hikari.danmaku.utils.LrcUtil.danmakuList;
import static com.hikari.danmaku.utils.LrcUtil.lrcToAssList;
import static com.hikari.danmaku.utils.LrcUtil.parseOffset;


@Api(tags = "发送歌词弹幕")
@RestController
@RequestMapping("/danmaku")
public class DanmakuController {

    @Value("${api.dmPost}")
    private String dmPostUrl;

    @Autowired
    private IFileService fileService;

    @Autowired
    private ILogService logService;

    @Autowired
    private IDanmakuService danmakuService;


    @ApiOperation("发送普通弹幕歌词")
    @PostMapping("/sendDanmakuM1")
    public ResponseResult<Map<String,Object>> sendDanmakuM1(MultipartFile file,SendDanmakuM1Vo sendDanmakuM1Vo) throws Exception{
        //1、处理文件
        String filePath = this.fileService.upload(file,sendDanmakuM1Vo.getRequestId());
        String originalFilename = file.getOriginalFilename();
        String prefix=originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        List<Ass> danmakuList = new ArrayList<>();
        boolean m1Flag = false;

        //2、解析文件
        if("lrc".equals(prefix)){
            danmakuList = lrcToAssList(filePath);
        }else if("ass".equals(prefix)){
            danmakuList = danmakuList(filePath);
        }else if("m1".equals(prefix)){
            danmakuList = lrcToAssList(filePath);
            m1Flag = true;
        }else {
            return Response.makeErrRsp("");
        }

        //3、初始化
        danmakuService.initDanmakuM1(sendDanmakuM1Vo,danmakuList);
        String color = sendDanmakuM1Vo.getColor();
        int sendMode = sendDanmakuM1Vo.getSendMode() ;
        int startRow = 1;//开始行
        int timesOffset = 0;
        int firstDmOffset = 0;//时间偏移量
        int danmakuSum = 0;//发送弹幕统计数
        if(sendDanmakuM1Vo.getSendFirstDmOffset()!=null){
            firstDmOffset =  sendDanmakuM1Vo.getSendFirstDmOffset()  ;
            timesOffset = timesOffset + firstDmOffset;
        }
        if(sendDanmakuM1Vo.getSendStartDmRow() != null){
            startRow = sendDanmakuM1Vo.getSendStartDmRow() ;
        }
        if(StringUtils.isNotBlank(sendDanmakuM1Vo.getSendFirstDmTime())){
            String fdt = sendDanmakuM1Vo.getSendFirstDmTime();
            int sendFirstDmTime = parseOffset(fdt); // 修改后第一句时间
            int orgFirstDmTime = danmakuList.get(0).getStartTime(); // 原始第一句时间
            timesOffset = timesOffset + sendFirstDmTime - orgFirstDmTime ; //时间偏移
        }
        // 预览信息
        String preivewLog = "";
        String configTxt = logService.configLog(sendDanmakuM1Vo);
        String linesTxt = "";
        int lrcCount = danmakuList.size();//弹幕次数
        for (int i = startRow ;i <= lrcCount;i++) {
            Ass danmakuPreview = danmakuList.get(i-1);
            String previewLog = logService.previewLog(i,danmakuPreview.getStartTime(),(danmakuPreview.getStartTime() + timesOffset ),danmakuPreview.getContent());
            linesTxt += previewLog;
        }
        if(sendMode == -1){
            String endTxt = "─────────────────────结束────────────────────────\n";
            preivewLog = configTxt + linesTxt + endTxt ;
            return Response.makeOKRsp(preivewLog);
        }

        // 测试发送配置
        if(sendMode == 0){
            lrcCount = startRow + 2;   //弹幕次数【通过修改lrcCount可只发送3条】
        }

        // 创建弹幕日志文件
        String fileLogPath = fileService.createFileLog(originalFilename, sendDanmakuM1Vo.getRequestId());
        FileWriter writer = new FileWriter(fileLogPath);
        writer.append(configTxt);
        writer.append(linesTxt);

        // 循环调用接口发送弹幕
        logService.writePlus(writer,"───────────────────────发送──────────────────────\n");
        long startRecord = DateUtil.getNowDate().getTime();
        int randomTime = 0;
        for (int j = startRow; j <= lrcCount; j++) {
            Random random =new Random();
            if(sendDanmakuM1Vo.getSendRandomTime() > 0){
                randomTime = random.nextInt(sendDanmakuM1Vo.getSendRandomTime());
            }
            int sleepTime = sendDanmakuM1Vo.getSendInterval() * 1000 + randomTime;
            Ass lrcDanmaku = danmakuList.get(j-1);
            String content =lrcDanmaku.getContent();
            int startTime = lrcDanmaku.getStartTime() + timesOffset;

            if(danmakuSum > 1000){
                writer.append("───────────────发送弹幕已超1000条───────────────\n");
                writer.append("─────────────────────停止─────────────────────");
                return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
            }
            if(danmakuService.checkStop(fileLogPath)){
                System.out.println("─────────────────────停止─────────────────────");
                return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
            }

            color =  danmakuService.updateColor(color);   //微调颜色
            sendDanmakuM1Vo.setColor(color);
            //发送弹幕
            ResponseResult sendResponse = HttpUtil.dmPost(content,startTime, sendDanmakuM1Vo);
            if(sendResponse.getCode() == 0){
                danmakuSum +=1;
                logService.writePlus(writer,"{} 第{}次发送成功：{}\n",DateUtil.getTime(),danmakuSum,content);
                Thread.sleep(sleepTime);
            } else if(sendResponse.getCode() == 36703){
                danmakuSum +=1;
                logService.writePlus(writer,retryTem,DateUtil.getTime(),sendDanmakuM1Vo.getSendRetryInterval());
                Thread.sleep(sendDanmakuM1Vo.getSendRetryInterval() * 60 * 1000); //设置暂停的时间
                sendDanmakuM1Vo.setColor(danmakuService.updateColor(color)); //微调颜色
                sendResponse = HttpUtil.dmPost(content, startTime, sendDanmakuM1Vo);
                if(sendResponse.getCode() == 0){
                    logService.writePlus(writer,"{} 第{}次延时发送成功: {}\n",DateUtil.getTime(),danmakuSum,content);
                    Thread.sleep(sleepTime);
                }else {
                    writer.append("延时发送失败");
                    Thread.sleep(sleepTime);
                }
            } else {
                logService.writePlus(writer,"错误码：{}，错误信息：{}\n",sendResponse.getCode(),sendResponse.getMsg());
                logService.writePlus(writer,stopDivider);
                return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
            }
        }
        long endRecord = DateUtil.getNowDate().getTime();
        long diffTime = (endRecord - startRecord) / 1000 / (danmakuSum - 1);
        System.out.println(endRecord - startRecord);
        logService.writePlus(writer,finishDivider);
        logService.writePlus(writer,"发送{}条，总耗时{}，发送间隔{}秒，随机{}秒，重发间隔{}分\n平均每发送一条弹幕花费{}秒",
                danmakuSum,
                DateUtil.getDatePoorPlus(endRecord,startRecord),
                sendDanmakuM1Vo.getSendInterval(),
                randomTime,
                sendDanmakuM1Vo.getSendRetryInterval(),
                diffTime
        );
        return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
    }


    @ApiOperation("发送高级弹幕歌词")
    @PostMapping("/sendDanmakuM7")
    public ResponseResult<Map<String,Object>> sendDanmakuM7(MultipartFile file,SendDanmakuM7Vo sendDanmakuM7Vo) throws Exception{
        //1、处理文件
        String filePath = this.fileService.upload(file,sendDanmakuM7Vo.getRequestId());
        String originalFilename = file.getOriginalFilename();
        String prefix=originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        List<Ass> danmakuList = new ArrayList<>();
        boolean m1Flag = false;


        int outOffset = 0;//lrc歌词用于处理句与句之间间隙时间
        //2、解析文件
        if("lrc".equals(prefix)){
            danmakuList = lrcToAssList(filePath);
            outOffset = sendDanmakuM7Vo.getLrcIntervalTime();//250为结尾提前250结束
        }else if("ass".equals(prefix)){
            danmakuList = danmakuList(filePath);
        }else {
            return Response.makeErrRsp("");
        }

        //3、初始化
        danmakuService.initDanmakuM7(sendDanmakuM7Vo,danmakuList);
        String color = sendDanmakuM7Vo.getColor();
        int sendMode = sendDanmakuM7Vo.getSendMode() ;


        int startRow = 1;//开始行
        int timesOffset = 0;
        int firstDmOffset = 0;//时间偏移量
        int danmakuSum = 0;//发送弹幕统计数
        if(sendDanmakuM7Vo.getSendFirstDmOffset()!=null){
            firstDmOffset =  sendDanmakuM7Vo.getSendFirstDmOffset()  ;
            timesOffset = timesOffset + firstDmOffset;
        }
        if(sendDanmakuM7Vo.getSendStartDmRow() != null){
            startRow = sendDanmakuM7Vo.getSendStartDmRow() ;
        }
        if(!sendDanmakuM7Vo.getSendFirstDmTime().isEmpty()){
            String fdt = sendDanmakuM7Vo.getSendFirstDmTime();
            int sendFirstDmTime = parseOffset(fdt); // 修改后第一句时间
            int orgFirstDmTime = danmakuList.get(0).getStartTime(); // 原始第一句时间
            timesOffset = timesOffset + sendFirstDmTime - orgFirstDmTime ; //时间偏移
        }
        // 预览信息
        String preivewLog = "";
        String configTxt = logService.configLog(sendDanmakuM7Vo);
        String linesTxt = "";
        int lrcCount = danmakuList.size();//弹幕次数
        for (int i = startRow ;i <= lrcCount;i++) {
            Ass danmakuPreview = danmakuList.get(i-1);
            String previewLog = logService.previewLog(i,danmakuPreview.getStartTime(),(danmakuPreview.getStartTime() + timesOffset ),danmakuPreview.getContent());
            linesTxt += previewLog;
        }
        if(sendMode == -1){
            String endTxt = "─────────────────────结束────────────────────────\n";
            preivewLog = configTxt + linesTxt + endTxt ;
            return Response.makeOKRsp(preivewLog);
        }

        // 测试发送配置
        if(sendMode == 0){
            lrcCount = startRow;   //弹幕次数【通过修改lrcCount可只发送3条】
        }

        // 创建弹幕日志文件
        String fileLogPath = fileService.createFileLog(originalFilename,sendDanmakuM7Vo.getRequestId());
        FileWriter writer = new FileWriter(fileLogPath);
        writer.append(configTxt);
        writer.append(linesTxt);

        String inCheck = "true";
        String outCheck = "true";

        // 循环调用接口发送弹幕
        logService.writePlus(writer,"───────────────────────发送──────────────────────\n");
        int randomTime = 0;
        long startRecord = DateUtil.getNowDate().getTime();
        for (int j = startRow; j <= lrcCount; j++) {
            //高级弹幕三段发送
            for(int k = 0; k < 3; k++) {
                if (!"true".equals(inCheck) && k == 0) {//跳过淡入部分
                    continue;
                }
                if (!"true".equals(outCheck) && k == 2) {//跳过淡出部分
                    continue;
                }
                Random random =new Random();
                if(sendDanmakuM7Vo.getSendRandomTime() > 0){
                    randomTime = random.nextInt(sendDanmakuM7Vo.getSendRandomTime());
                }
                int sleepTime = sendDanmakuM7Vo.getSendInterval() * 1000 + randomTime;


                Ass lrcDanmaku = danmakuList.get(j-1);
                String danmakuText = lrcDanmaku.getContent();
                sendDanmakuM7Vo.setText(danmakuText);

                String content = "";
                int startTime = lrcDanmaku.getStartTime() + timesOffset;

                if(danmakuSum > 1000){
                    writer.append("───────────────发送弹幕已超1000条───────────────\n");
                    writer.append("─────────────────────停止─────────────────────");
                    return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
                }
                if(danmakuService.checkStop(fileLogPath)){
                    System.out.println("─────────────────────停止─────────────────────");
                    return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
                }

                if(k == 0){//淡入
                    startTime = startTime;
                    content = DanmakuUtil.wrapperInDanmaku(sendDanmakuM7Vo);
                }else if(k == 2){//淡出
                    startTime = startTime + lrcDanmaku.getSyl() - sendDanmakuM7Vo.getOverlapTime()  - outOffset ;
                    content = DanmakuUtil.wrapperOutDanmaku(sendDanmakuM7Vo);
                } else {
                    double stage1ltDouble = sendDanmakuM7Vo.getInDuration() *1000;
                    startTime = startTime + (int)stage1ltDouble - sendDanmakuM7Vo.getOverlapTime() ;
                    double sylStr = (lrcDanmaku.getSyl()- (int)stage1ltDouble +  sendDanmakuM7Vo.getOverlapTime()  - outOffset) * 0.001 ;
                    sendDanmakuM7Vo.setDuration(sylStr);
                    content = DanmakuUtil.wrapperDanmaku(sendDanmakuM7Vo);

                }


                color =  danmakuService.updateColor(color);   //微调颜色
                sendDanmakuM7Vo.setColor(color);
                //发送弹幕
                ResponseResult sendResponse = HttpUtil.dmPost(content, startTime, sendDanmakuM7Vo);
                if(sendResponse.getCode() == 0){
                    danmakuSum +=1;
                    logService.writePlus(writer,"{} 第{}次发送成功：{}\n",DateUtil.getTime(), danmakuSum, content);
                    Thread.sleep(sleepTime);
                } else  if(sendResponse.getCode() == 36703){
                    danmakuSum +=1;
                    logService.writePlus(writer,retryTem,DateUtil.getTime(), sendDanmakuM7Vo.getSendRetryInterval());
                    Thread.sleep(sendDanmakuM7Vo.getSendRetryInterval() * 60 * 1000); //设置暂停的时间
                    sendDanmakuM7Vo.setColor(danmakuService.updateColor(color)); //微调颜色
                    sendResponse = HttpUtil.dmPost(content, startTime, sendDanmakuM7Vo);
                    if(sendResponse.getCode() == 0){
                        logService.writePlus(writer,"{} 第{}次延时发送成功: {}\n",DateUtil.getTime(), danmakuSum, content);
                        Thread.sleep(sleepTime);
                    }else {
                        writer.append("延时发送失败");
                        Thread.sleep(sleepTime);
                    }
                } else {
                    logService.writePlus(writer,"错误码：{}，错误信息：{}\n",sendResponse.getCode(),sendResponse.getMsg());
                    logService.writePlus(writer,stopDivider);
                    return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
                }


            }

        }
        long endRecord = DateUtil.getNowDate().getTime();
        long diffTime = (endRecord - startRecord) / 1000 / (danmakuSum - 1);
        System.out.println(endRecord - startRecord);
        logService.writePlus(writer,finishDivider);
        logService.writePlus(writer,"发送{}条，总耗时{}。发送间隔{}秒，随机{}秒，重发间隔{}分\n平均每发送一条弹幕花费{}秒",
                danmakuSum,
                DateUtil.getDatePoorPlus(endRecord,startRecord),
                sendDanmakuM7Vo.getSendInterval(),
                randomTime,
                sendDanmakuM7Vo.getSendRetryInterval(),
                diffTime
        );
        return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
    }


    @ApiOperation("保存M7效果模板")
    @PostMapping("/saveEffectTemM7")
    public ResponseResult<Map<String,Object>> saveEffectTemM7(@RequestBody SendDanmakuM7Vo sendDanmakuM7Vo) throws Exception {
        String path = System.getProperty("user.dir") + "/effectTem.txt";
        FileWriter writer = new FileWriter(path);

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("bvid");
        filter.getExcludes().add("cookie");
        filter.getExcludes().add("csrf");
        filter.getExcludes().add("sendFirstDmTime");
        filter.getExcludes().add("sendMode");
        String jsonResult = JSONObject.toJSONString(sendDanmakuM7Vo,filter);
//        writer.append(sendDanmakuM7Vo.getEffectTem()+":\n");
        writer.append(jsonResult+"\n");
        System.out.println("保存成功");
        return Response.makeOKRsp(JSONObject.toJSONString(sendDanmakuM7Vo));
    }

    @ApiOperation("读取M7效果模板")
    @PostMapping("/getEffectTemM7")
    public ResponseResult getEffectTemM7(@RequestBody SendDanmakuM7Vo sendDanmakuM7Vo) throws Exception {
        String path = System.getProperty("user.dir") + "/effectTem.txt";
        FileReader reader = new FileReader(path);
        JSONObject rspJson = new JSONObject();
        rspJson.put("effectTem",reader.readString());
        System.out.println("读取效果模板成功");

        List<SendDanmakuM7Vo> m7VoList = new ArrayList<>();
        List<String> linesList = reader.readLines();
        for(String lines : linesList){
            JSONObject jsonObject1 = JSONObject.parseObject(lines);
            SendDanmakuM7Vo m7Vo = JSONObject.toJavaObject(jsonObject1, SendDanmakuM7Vo.class);
            m7VoList.add(m7Vo);
        }

        return Response.makeOKRsp(m7VoList);
    }

    @ApiOperation("读取缓存")
    @GetMapping("/getConfig")
    public ResponseResult getConfig() throws Exception {
        String path = System.getProperty("user.dir") + "/config.txt";
        FileReader reader = new FileReader(path);
        System.out.println("读取缓存成功");
        JSONObject rspJson = new JSONObject();
        List<String> linesList = reader.readLines();
        for(String lines : linesList){
            if(lines.indexOf("SESSDATA") != -1){
                String[] lineArray = lines.split(":");
                if(lineArray.length > 1){
                    rspJson.put("cookie",lineArray[1].trim());
                }
            }
            if(lines.indexOf("bili_jct") != -1){
                String[] lineArray = lines.split(":");
                if(lineArray.length > 1){
                    rspJson.put("csrf",lineArray[1].trim());
                }

            }
            if(lines.indexOf("bvid") != -1){
                String[] lineArray = lines.split(":");
                if(lineArray.length > 1){
                    rspJson.put("bvid",lineArray[1].trim());
                }
            }

        }

        return Response.makeOKRsp(rspJson);
    }


    @ApiOperation("发送弹幕XML")
    @PostMapping("/sendDanmakuXml")
    public ResponseResult<Map<String,Object>> sendDanmakuXml(MultipartFile file,SendDanmakuM1Vo sendDanmakuM1Vo) throws Exception{
        //1、处理文件
        String filePath = this.fileService.upload(file,sendDanmakuM1Vo.getRequestId());
//        String lrcResult = LrcUtil.createLrc(filePath);
        String originalFilename = file.getOriginalFilename();
        String prefix=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        String fileLogPath = this.fileService.createFileLog(originalFilename,sendDanmakuM1Vo.getRequestId());

        List<BaseDanmaku> danmakuList = new ArrayList<>();

        //2、解析文件
        if("xml".equals(prefix)){
            danmakuList = XmlUtil.analyseXml(filePath);
        }

        //3、初始化 todo check
//        sendDanmakuM1Vo.setMode(1);

        String color = sendDanmakuM1Vo.getColor();

        int sendMode = sendDanmakuM1Vo.getSendMode() ;
        int startRow = 1;//开始行
        int part = 1;
        int danmakuRetryTimes = 0; //错误计数
        int timesOffset = 0;
        int firstDmOffset = 0;//时间偏移量
        int danmakuSum=0;//发送弹幕统计数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String bvid = sendDanmakuM1Vo.getBvid();

        List<Ass> danmakuListRow = new ArrayList<>();


        // 根据bvid + pardId获取视频信息
        Map<String,Object> videoMap = new HashMap<>();
        String title = "无";
        String pageCid = "无";
        String pagePart = "无";
        if(sendDanmakuM1Vo.getBvid()!=null){
            videoMap =  danmakuService.getBiliVideoInfo(bvid);
            title = (String)videoMap.get("title");
            part =  Integer.valueOf(sendDanmakuM1Vo.getPart())-1;
            JSONArray jsonArray =(JSONArray)videoMap.get("page");
            JSONObject jo = jsonArray.getJSONObject(part);
            pageCid = jo.getString("cid");
            pagePart = jo.getString("part");
        }
        sendDanmakuM1Vo.setOid(pageCid);
        sendDanmakuM1Vo.setVideoTitle(title);
        sendDanmakuM1Vo.setPartTitle(pagePart);

        int xmlCount = danmakuList.size();//弹幕次数【通过修改lrcCount可只发送1条】

        FileWriter writer = new FileWriter(fileLogPath);
        writer.append(FileLogUtil.configLog(sendDanmakuM1Vo));

        for (int i = startRow ;i <= xmlCount;i++) {
            BaseDanmaku danmakuPreview = danmakuList.get(i-1);
            String previewLog = danmakuPreview.getContent() ;
            writer.append(previewLog);
        }
        if(sendMode == -1){
            writer.append( "\"─────────────────────结束────────────────────────");
            System.out.println("─────────────────────结束─────────────────────\"");
            return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
        }else if( sendMode == 0){
//            xmlCount = startRow + 3;   //弹幕次数【通过修改lrcCount可只发送3条】
        }
        writer.append( "\"──────────────────────发送──────────────────────\n");
        //输出看看歌词效果
        for (int j = startRow ;j <= xmlCount;j++) {
            Random random =new Random();
            int sleepTime = sendDanmakuM1Vo.getSendInterval()*1000 + random.nextInt(2000);
            BaseDanmaku xmlDanmaku = danmakuList.get(j-1);
            String content =xmlDanmaku.getContent();

            String start = String.valueOf(Math.round(Double.valueOf(xmlDanmaku.getStartTime())*1000));
            int startTime =  Integer.parseInt(start);
            sendDanmakuM1Vo.setColor(xmlDanmaku.getColor());
            sendDanmakuM1Vo.setMode(xmlDanmaku.getMode());
            sendDanmakuM1Vo.setFontSize(xmlDanmaku.getFontSize());
            sendDanmakuM1Vo.setContent(xmlDanmaku.getContent());

            if(danmakuSum > 2000){
                writer.append("--------发送弹幕已超2000条，发送终止--------\n");
                return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
            }

            if(danmakuService.checkStop(fileLogPath)){
                System.out.println("--------发送终止--------");
                return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
            }

            color =  danmakuService.updateColor(color);   //微调颜色
            sendDanmakuM1Vo.setColor(color);

            ResponseResult sendResponse = HttpUtil.dmPost(content, startTime, sendDanmakuM1Vo);
            if(sendResponse.getCode() == 0){
                danmakuSum +=1; //发送弹幕+1
                writer.append(sdf.format(new Date()) + " 第" + danmakuSum + "次" + "发送成功：" + content +"\n");
                System.out.println(sdf.format(new Date())  + " 第" + danmakuSum + "次" + "发送弹幕成功：" + content);
                Thread.sleep(sleepTime); //设置暂停的时间 10 秒
            } else  if(sendResponse.getCode() == 36703){
                danmakuSum +=1;
                danmakuRetryTimes += 1;
                writer.append(sdf.format(new Date())  + " 弹幕发送频率过快，将2min后重试");
                System.out.println(sdf.format(new Date())  + " 弹幕发送频率过快，将2min后重试");
                Thread.sleep(120000); //设置暂停的时间
                sendDanmakuM1Vo.setColor(danmakuService.updateColor(color)); //微调颜色
                sendResponse = HttpUtil.dmPost(content, startTime, sendDanmakuM1Vo);
                if(sendResponse.getCode() == 0){
                    writer.append( "第" + danmakuSum + "+次" + "延时发送弹幕成功:" + content);
                    Thread.sleep(sleepTime);
                }else {
                    writer.append("延时发送失败");
                    Thread.sleep(sleepTime);
                }

            } else {
                writer.append("错误信息：" + sendResponse.getMsg() +"\n");
                writer.append("─────────────────────停止发送─────────────────────");
                return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
            }
        }
        writer.append("──────────────────全部弹幕发送完毕───────────────────");
        System.out.println("──────────────────全部弹幕发送完毕─────────────────");
        return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
    }


    @ApiOperation("获取弹幕日志")
    @PostMapping("/getFileLog")
    public ResponseResult<Map<String,Object>> getFileLog(@RequestParam String fileName, @RequestParam String requestId) throws Exception {
        String fileLogPath = this.fileService.getFileLogPath(fileName,requestId);
        FileReader reader = new FileReader(fileLogPath);
        JSONObject rspJson = new JSONObject();
        rspJson.put("txtLog",reader.readString());
        return Response.makeOKRsp(rspJson);
    }

    @ApiOperation("停止发送弹幕")
    @PostMapping("/stopSendDanmaku")
    public ResponseResult<Map<String,Object>> stopSendDanmaku(@RequestParam String fileName, @RequestParam String requestId) throws Exception {
        String fileLogPath = this.fileService.getFileLogPath(fileName,requestId);
        FileWriter writer = new FileWriter(fileLogPath);
        writer.append("─────────────────────停止─────────────────────");
        return Response.makeOKRsp(fileService.getFileLog(fileLogPath));
    }


}
