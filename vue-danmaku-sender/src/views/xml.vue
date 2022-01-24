<template> 
  <div> 
    <el-row :gutter="20" style="margin-left:auto;margin-right:auto;margin-top:22px;background-color: rgb(248, 250, 251);"> 
     <!-- 配置区 --> 
     <el-col :span="12"> 
      <el-card style="width:600px;float:right;min-height:600px"  body-style="padding-top:5px"> 
       <el-form ref="form" :model="form" label-width="80px"> 
  <el-tabs v-model="activeName" >
    <el-tab-pane label="基本配置" name="first">
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


        <el-row style="margin-top:22px"> 
         <el-col :span="5"> 
          <el-button @click="submitPreviewForm()">预览信息</el-button> 
         </el-col> 
         <el-col :span="5"> 
          <el-button @click="submitForm()">发送弹幕</el-button> 
         </el-col> 
         <el-col :span="5"> 
          <el-button @click="stopSendDanmaku()">停止发送</el-button> 
         </el-col> 
        </el-row> 

    </el-tab-pane>
    <el-tab-pane label="更多配置" name="second">

        <el-row>
          <el-col :span="12" style="display:none">
              <el-form-item size="middle" label="首句时间" prop="sendFirstDmTime" label-width="100px" >
                <!-- <el-input v-model="form.sendFirstDmTime"  placeholder="非必填，首句时间，调整时间轴"  /> -->
                <el-time-picker  
                  v-model="form.sendFirstDmTime" 
                  :picker-options="{ selectableRange: '00:00:00 - 23:59:59' }" 
                  :default-value="new Date(2021, 11, 0, 0, 0)"
                  value-format="HH:mm:ss"
                  placeholder="可用于调整时间轴"
                  style="width:180px"
                  >
                </el-time-picker>
              </el-form-item>
          </el-col>
          <el-col :span="12"  style="display:none">
              <el-form-item size="middle" label="开始句" prop="sendStartDmRow" label-width="100px">
                <el-input v-model="form.sendStartDmRow"  placeholder="配合发送测试弹幕使用，2为发送第二句歌词" maxlength="30" />
              </el-form-item>
          </el-col>
        </el-row>

   
        <el-row>
          <el-col :span="12"  style="display:none">
            <el-form-item size="middle" label="时间补偿值" prop="sendFirstDmOffset" label-width="100px">
              <el-input v-model="form.sendFirstDmOffset"  placeholder="可正负数" maxlength="30" >
                  <template slot="append">毫秒</template>
              </el-input>
            </el-form-item>

          </el-col>
          <el-col :span="12">
          
          </el-col>
        </el-row>
   

     <el-row>
          <el-col :span="12"  style="display:none">
                  <el-form-item size="middle" label="字号" prop="fontSize" label-width="100px">
                      <el-select v-model="form.fontSize" placeholder="请选择字号">
                      <el-option label="小字号（18）" value="18"></el-option>
                      <el-option label="标准字号（25）" value="25"></el-option>
                    </el-select>
                  </el-form-item>
          </el-col>

            <el-col :span="10"  style="display:none">
                  <el-form-item size="middle" label="颜色" prop="color" label-width="100px">
                    <el-input v-model="form.color"  placeholder="" maxlength="30" />
                  </el-form-item>
          </el-col>
            <el-col :span="2"  style="display:none">
                  <el-color-picker v-model="hexColor" style="float:right"></el-color-picker>
          </el-col>
        </el-row>


            <el-row>
   
              <el-col :span="12">

                      <el-form-item size="middle" label="发送间隔" prop="sendInterval" label-width="100px">
                        <el-input v-model="form.sendInterval"  placeholder="发送弹幕间隔时间" maxlength="30" >
                           <template slot="append">秒</template>
                        </el-input>
                      </el-form-item>

              </el-col>
                         <el-col :span="12">

                <el-form-item size="middle" label="附加时间" prop="sendRandomTime" label-width="100px">
                    <el-input v-model="form.sendRandomTime"  placeholder="附加随机时间" >
                      <template slot="append">秒</template>
                      </el-input>
                </el-form-item>

              </el-col>
            </el-row>
              <el-row>
                <el-col :span="12">
                </el-col>
                <el-col :span="12">

                      <el-form-item size="middle" label="重发间隔" prop="sendRetryInterval" label-width="100px">
                        <el-input v-model="form.sendRetryInterval"  placeholder="重发间隔" maxlength="30" >
                           <template slot="append">分</template>
                        </el-input>
                      </el-form-item>
                        
                </el-col>
              </el-row>

    </el-tab-pane>
  </el-tabs>

       </el-form> 
      </el-card> 
     </el-col> 
     <!-- 日志区 --> 
     <el-col :span="12"> 
      <el-card style="width:600px;float:left;min-height:600px"> 
       <span></span> 
       <el-input type="textarea" wrap="off" :rows="2" placeholder="发送日志显示区域" v-model="textarea" :autosize="{ minRows: 26, maxRows: 26}" style=""> 
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
        sendFirstDmTime: null,
        sendFirstDmOffset: null,
        sendInterval: '20',
        sendMode: '1',
        sendStartDmRow: '1',
        sendPreview: '1',
        sendRandomTime: '0',
        sendRetryInterval: '1',
        
      },
      rules: {     // 表单校验
        // cookie: [
        //   { required: true, message: "发送时cookie不能为空" }
        // ],
        // csrf: [
        //   { required: true, message: "发送时csrf不能为空"}
        // ],
        // bvid: [
        //   { required: true, message: "发送时bvid不能为空" }
        // ],
      },
      textarea: '',
      txtFile: null,
      requestId: '',
      timerStatus: 0,
      timer: null, //轮询器
      uploadName: '请上传lrc或ass文件',
      activeName: 'first',
    };
  },
  methods: {
    // 发送弹幕
    submitForm(){
      if (this.txtFile == null) {
        this.$message({
          message: '请先上传文件!',
          type: 'warning'
        })
        return
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          let _this = this;
          this.textarea = ''
          let param = new FormData()
          param.append('file', this.txtFile)
          for(let key in this.form){
            if(this.form[key] != null){
              param.append(key,this.form[key])
            }
          }

          this.createCode();
          param.append('requestId', this.requestId) // 每次请求创建一个随机Id
          let config = {
              headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
          }
          this.axios.post('/danmaku/sendDanmakuXml', param, config).then(res => {
            if(res.data.code == 200){
              this.textarea = res.data.data.txtLog
              console.log(this.textarea);
              _this.stopSetInterval()
              this.timerStatus = 0;
            }
          }).catch(function (error) {
            console.log(error);
          });
          console.log("轮询弹幕日志接口")
          this.createSetInterval()
        }
      });
    },

    // 预览信息
    submitPreviewForm(){
       if (this.txtFile == null) {
        this.$message({
          message: '请先上传文件!',
          type: 'warning'
        })
        return
      }
      this.textarea = ''
      let param = new FormData()
      param.append('file', this.txtFile)
      for(let key in this.form){
        if(this.form[key] != null){
          param.append(key,this.form[key])
        }
      }
      param.set("sendMode",-1) // 预览信息
      this.createCode();
      param.append('requestId',"p" + this.requestId) // 每次请求创建一个随机Id
      let config = {
          headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
      }
      this.axios.post('/danmaku/sendDanmakuXml', param, config).then(res => {
        debugger
        if(res.data.code == 200){
            this.textarea = res.data.msg
        }
      }).catch(function (error) {
        console.log(error);
      });
    },

    // 获取弹幕日志
    getFileLog() {
      let paramLog = new FormData()
      paramLog.append('fileName', this.txtFile.name)
      paramLog.append('requestId', this.requestId)
      let config = {
          headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
      }
      //2、发送是通过获取日志接口返回日志数据
      this.axios.post('/danmaku/getFileLog', paramLog, config).then(res => {
        if(res.data.code == 200){
          this.textarea = res.data.data.txtLog
          if(res.data.data.txtLog.indexOf("─全部弹幕发送完毕─")>0){
              console.log("关闭轮询")
              this.stopSetInterval()
          }
        }
      }).catch(error =>  {
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
          }).catch(error =>  {
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
        console.log("关闭查询日志轮询")
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