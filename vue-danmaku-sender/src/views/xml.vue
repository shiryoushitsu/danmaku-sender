<template> 
  <div> 
    <el-row :gutter="20" style="margin-left:auto;margin-right:auto;margin-top:22px;background-color: rgb(248, 250, 251);"> 
     <!-- 配置区 --> 
     <el-col :span="12"> 
      <el-card style="width:600px;float:right;min-height:600px"> 
       <el-form ref="form" :model="form" label-width="80px"> 
        <el-row> 
         <el-col :span="12"> 
          <el-form-item label="账号缓存值1" prop="cookie" label-width="100px"> 
           <el-input v-model="form.cookie" placeholder="SESSDATA，相当于发送的弹幕的账号" /> 
          </el-form-item> 
         </el-col> 
         <el-col :span="12"> 
          <el-form-item label="视频BV号" prop="bvid" label-width="100px"> 
           <el-input v-model="form.bvid" placeholder="请输入视频BV号" maxlength="30" /> 
          </el-form-item> 
         </el-col> 
        </el-row> 
        <el-row> 
         <el-col :span="12"> 
          <el-form-item label="账号缓存值2" prop="csrf" label-width="100px"> 
           <el-input v-model="form.csrf" placeholder="bili_jct，用于发送接口账号校验" /> 
          </el-form-item> 
         </el-col> 
         <el-col :span="12"> 
          <el-form-item label="分P号" prop="part" label-width="100px"> 
           <el-input v-model="form.part" placeholder="默认1，分p时需填要发送弹幕所在的分p号" /> 
          </el-form-item> 
         </el-col> 
        </el-row> 
        <el-row> 
         <el-col :span="12"> 
          <el-form-item label="发送模式" prop="sendMode" label-width="100px"> 
           <el-select v-model="form.sendMode" placeholder="请选择发送模式"> 
            <el-option label="测试发送弹幕(发送前3条)" value="0"></el-option> 
            <el-option label="正式发送弹幕" value="1"></el-option> 
           </el-select> 
          </el-form-item> 
         </el-col> 
         <el-col :span="12"> 
          <el-form-item label="弹幕池" prop="pool" label-width="100px"> 
           <el-select v-model="form.pool" placeholder="请选择弹幕池"> 
            <el-option label="普通池" value="0"></el-option> 
            <el-option label="字幕池" value="1"></el-option> 
            <el-option label="特殊池" value="2"></el-option> 
           </el-select> 
          </el-form-item> 
         </el-col> 
        </el-row> 
        <el-row> 
         <el-col :span="12"> 
         </el-col> 
        </el-row> 
        <el-row> 
         <el-col :span="12"> 
          <el-form-item size="middle" label="上传xml文件" label-width="100px"> 
           <input name="file" type="file" @change="addFile($event, 1)" /> 
          </el-form-item> 
         </el-col> 
         <el-col :span="12"> 
         </el-col> 
        </el-row> 
        <el-collapse style=" display:none"> 
         <el-collapse-item title="更多配置" name="1"> 
          <el-row> 
           <el-col :span="12"> 
            <el-form-item label="字号" prop="fontSize" label-width="100px"> 
             <el-select v-model="form.fontSize" placeholder="请选择字号"> 
              <el-option label="小字号（18）" value="18"></el-option> 
              <el-option label="标准字号（25）" value="25"></el-option> 
             </el-select> 
            </el-form-item> 
           </el-col> 
           <el-col :span="12"> 
            <el-form-item label="发送间隔" prop="sendInterval" label-width="100px"> 
             <el-input v-model="form.sendInterval" placeholder="发送弹幕间隔时间" maxlength="30"> 
              <template slot="append">秒</template> 
             </el-input> 
            </el-form-item> 
           </el-col> 
          </el-row> 
          <el-row> 
           <el-col :span="12"> 
            <el-col :span="10"> 
             <el-form-item label="颜色" prop="color" label-width="100px"> 
              <el-input v-model="form.color" placeholder="" maxlength="30" /> 
             </el-form-item> 
            </el-col> 
            <el-col :span="2"> 
             <el-color-picker v-model="hexColor" style="float:right"></el-color-picker> 
            </el-col> 
           </el-col> 
           <el-col :span="12"> 
           </el-col> 
          </el-row> 
         </el-collapse-item> 
        </el-collapse> 
        <el-row style="margin-top:22px"> 
         <el-col :span="5"> 
          <el-button @click="submitForm('preview')">预览信息</el-button> 
         </el-col> 
         <el-col :span="5"> 
          <el-button @click="submitForm('send')">发送弹幕</el-button> 
         </el-col> 
         <el-col :span="5"> 
          <el-button @click="stopSendDanmaku()">停止发送</el-button> 
         </el-col> 
        </el-row> 
       </el-form> 
      </el-card> 
     </el-col> 
     <!-- 日志区 --> 
     <el-col :span="12"> 
      <el-card style="width:600px;float:left;min-height:600px"> 
       <span></span> 
       <el-input type="textarea" :rows="2" placeholder="发送日志显示区域" v-model="textarea" :autosize="{ minRows: 26, maxRows: 26}" style=""> 
       </el-input> 
      </el-card> 
     </el-col> 
    </el-row> 
  </div> 
</template> 


<script>
import qs from 'qs'
export default {
  name: 'Home',
  components: {
    
  },
  computed: {
    hexColor: {
      get:function(){
        if(!this.form.color.indexOf("#") >= 0){
          return "#" + this.form.color 
        }else{
           return this.form.color 
        }
      },
      set:function(value){
        if(value!=null){
          this.form.color = value.slice(1);
        }

      }
    }
  },
  created(){
    // this.initForm();
    this.getConfig()
  },
  data() {
    return {
      value1:'', 
      form:{
        cookie: null,
        csrf: null,
        bvid: null,
        part: '1',
        fontSize: '25',
        color: 'FFFFFF',
        pool: '0',
        sendPrefix: null,
        sendSuffix: null,
        sendFirstDmTime: '',
        sendFirstDmOffset: null,
        sendInterval: '10',
        sendMode: '1',
        sendStartDmRow: '1',
        sendPreview: '1',
      },
      textarea: '',
      txtFile: null,
      requestId: '',
      timerStatus: 0,
      timer: null //轮询器
    };
  },
  methods: {
    // 发送弹幕
    submitForm(type){
      this.textarea = ''
      let param = new FormData() // 创建form对象
      param.append('file', this.txtFile) // 通过append向form对象添加数据
      for(let key in this.form){
        if(this.form[key] != null){
          param.append(key,this.form[key])
        }
      }
      if(type == 'preview'){
        param.set("sendMode",-1) // 预览信息 sendMode为-1
      }
      this.createCode();
      param.append('requestId', this.requestId) // 每次请求创建一个随机Id
      let config = {
          headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
      }
      this.axios.post('/danmaku/sendDanmakuXml', param, config).then(res => {
        // let res = JSON.stringify(response);
        if(res.data.code == 200){
          //1、预览是通过接口返回日志数据
          // if(type == 'preview'){
            this.textarea = res.data.data.txtLog
            console.log(this.textarea);
          // }
          //4、关闭轮询
          this.stopSetInterval()
          this.timerStatus = 0;

        }
      }).catch(function (error) {
        this.stopSetInterval()
        console.log(this.timer)
        console.log(error);
      });
      console.log("创建获取弹幕日志轮询接口")
      this.createSetInterval()
    },

    // 获取弹幕日志
    getFileLog() {
      let paramLog = new FormData()
      paramLog.append('fileName', this.txtFile.name)
      paramLog.append('requestId', this.requestId)
      let config = {
          headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
      }
      //2、发送是通过获取日志接口返回日志数据
      this.axios.post('/danmaku/getFileLog', paramLog, config).then(res => {
        if(res.data.code == 200){
          this.textarea = res.data.data.txtLog
          // console.log(this.textarea);
        }
      }).catch(function (error) {
        this.stopSetInterval()
        console.log(error);
      });
    },

    // 停止发送弹幕
    stopSendDanmaku() {
      if(this.requestId != ''){
        let paramLog = new FormData()
          paramLog.append('fileName', this.txtFile.name)
          paramLog.append('requestId', this.requestId)
          let config = {
              headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
          }
          this.axios.post('/danmaku/stopSendDanmaku', paramLog, config).then(res => {
            if(res.data.code == 200){
              this.textarea = res.data.data.txtLog
            }
          }).catch(function (error) {
            this.stopSetInterval()
            console.log(error);
          });
      }
  
    },

    // 开启轮询  如果存在则先销毁定时器后重新开启
    createSetInterval() {
      this.stopSetInterval()
      let _this = this
      this.timer = setInterval(() => {
        _this.getFileLog()
      }, 5000)
    },

    // 关闭轮询
    stopSetInterval() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },

    // 初始化用户账号缓存信息
    getConfig(){
      // console.log(JSON.stringify(this.form))
      this.axios.get('/danmaku/getConfig').then(res => {
        if(res.data.code == 200){
          this.form.cookie = res.data.data.cookie
          this.form.csrf = res.data.data.csrf
          console.log(res.data)
        }
      }).catch(function (error) {
        console.log(error);
      });
    },

    // 生成唯一请求ID
    createCode() {
      let codeArr = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
      let length = 4;
      let code = "";
      for (let i = 0; i < length; i++) {
          let randomI = Math.floor(Math.random() * 26);
          code += codeArr[randomI];
      }
      let hh = new Date().getHours() < 10 ? '0' + new Date().getHours() : new Date().getHours()
      let mm = new Date().getMinutes() < 10 ? '0' + new Date().getMinutes() : new Date().getMinutes()
      code =""+ hh + mm + code;
      this.requestId = code ;
      console.log(this.requestId);
    },

    // 文件上传
    addFile (e, type) {
      let file = e.target.files[0]
      // this.form.file = file
      this.txtFile = file
      let param = new FormData() // 创建form对象
      param.append('file', file, file.name) // 通过append向form对象添加数据
    },

    handleChangeLi(file, fileList) {
      console.log(file)
      console.log(fileList)
      this.fileListAdd = fileList
    },
    handlePictureCardPreview(file) {
      console.log(file)
    }
  },
  
  // 离开页面销毁轮询器
  destroyed() {
    this.stopSetInterval()
  },
}
</script>
<style scoped>

</style>