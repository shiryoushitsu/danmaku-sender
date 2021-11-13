<template>
  <div >

<el-row :gutter="20" style="margin-left:auto;margin-right:auto;margin-top:20px;background-color: rgb(248, 250, 251);">
  <!-- 配置区 -->
  <el-col :span="12" >

    <el-card style="width:600px;float:right;min-height:616px;" body-style="padding-top:5px">
      <el-form ref="form" :model="form" label-width="80px" :rules="rules" :hide-required-asterisk="true" :validate-on-rule-change="false">
          <el-tabs v-model="activeName" >
            <el-tab-pane label="淡入部分" name="second">
                  <el-row>
                       <el-col :span="12">
                      <el-form-item size="small" label="生存时间" prop="inDuration" label-width="100px">
                          <el-input v-model="form.inDuration"  placeholder="" maxlength="10" >
                            <template slot="append">秒</template>
                          </el-input>

                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item size="small" label="运动耗时" prop="inMoveDuration" label-width="100px">
                          <el-input v-model="form.inMoveDuration"  placeholder="" maxlength="10" >
                            <template slot="append">毫秒</template>
                          </el-input>
                      </el-form-item>
                    </el-col>
               
                </el-row>

                <el-row>
                  <el-col :span="12">
                        <el-form-item   size="small" label="起始x坐标" prop="inStartX" label-width="100px">
                        <el-input v-model="form.inStartX"  placeholder="起始x坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="起始y坐标" prop="inStartY" label-width="100px">
                          <el-input v-model="form.inStartY"  placeholder="起始y坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                </el-row>

                <el-row>
                  <el-col :span="12">
                        <el-form-item   size="small" label="结束x坐标" prop="inEndX" label-width="100px">
                        <el-input v-model="form.inEndX"  placeholder="起始x坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="结束y坐标" prop="inEndY" label-width="100px">
                          <el-input v-model="form.inEndY"  placeholder="结束y坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                </el-row>

                  <el-row>
                    <el-col :span="12">
                        <el-form-item size="small" label="透明度变化" prop="inAlpha" label-width="100px">
                            <el-input v-model="form.inAlpha"  placeholder="格式：百分比-百分比" />
          
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
      
                    </el-col>
                </el-row>
            </el-tab-pane>
            <el-tab-pane label="基本配置" name="first">
                <el-row>
                    <el-col :span="12">
                        <el-form-item   size="small" label="账号缓存值1" prop="cookie" label-width="100px">
                              <el-input v-model="form.cookie"  placeholder="SESSDATA，相当于发送的弹幕的账号" />
                            </el-form-item>
                    </el-col>
                    <el-col :span="12">
                            <el-form-item   size="small" label="视频BV号" prop="bvid" label-width="100px"  >
                              <el-input v-model="form.bvid"  placeholder="请输入视频BV号" maxlength="30"/>
                            </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                  <el-col :span="12">
                          <el-form-item   size="small" label="账号缓存值2" prop="csrf" label-width="100px" >
                            <el-input v-model="form.csrf"  placeholder="bili_jct，用于发送接口账号校验" />
                          </el-form-item>
                  </el-col>
                  <el-col :span="12">
                          <el-form-item   size="small" label="分P号" prop="part" label-width="100px" >
                            <el-input v-model="form.part"  placeholder="默认1，分p时需填要发送弹幕所在的分p号" />
                          </el-form-item>

                  </el-col>
                </el-row>

                <el-row>
                  <el-col :span="12">
                      <el-form-item   size="small" label="首句时间" prop="sendFirstDmTime" label-width="100px">
                        <el-input v-model="form.sendFirstDmTime"  placeholder="非必填，首句时间，调整时间轴"  />
                        <!-- <el-time-picker  
                          v-model="form.sendFirstDmTime" 
                          :picker-options="{ selectableRange: '00:00:00 - 23:59:59' }" 
                          default-value='0' 
                          placeholder="可用于调整时间轴"
                          style="width:180px"
                          >
                        </el-time-picker> -->
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="开始句" prop="sendStartDmRow" label-width="100px">
                        <el-input v-model="form.sendStartDmRow"  placeholder="配合发送测试弹幕使用，2为发送第二句歌词" maxlength="30" />
                      </el-form-item>
                  </el-col>
                </el-row>

                <el-row>
                  <el-col :span="12">
                        <el-form-item   size="small" label="字体" prop="fontFamily" label-width="100px">
                        <el-input v-model="form.fontFamily"  placeholder="字体" maxlength="30" />
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="文字描边" prop="bord" label-width="100px">
                             <el-select v-model="form.bord">
                                <el-option label="无描边" :value=0></el-option>
                                <el-option label="有描边" :value=1></el-option>
                              </el-select>
                          <!-- <el-checkbox v-model="form.blod">备选项</el-checkbox> -->
                          <!-- <el-switch
                            v-model="form.blod">
                          </el-switch> -->
                      </el-form-item>
                  </el-col>
                </el-row>

               

                <el-row>
                    <el-col :span="12">
                            <el-form-item size="small" label="字号" prop="fontSize" label-width="100px">
                                <el-input v-model="form.fontSize"  placeholder="请输入字号" maxlength="3" />
               
                            </el-form-item>
                    </el-col>
       <el-col :span="10">
                    <el-form-item   size="small" label="颜色" prop="color" label-width="100px">
                        <el-input v-model="form.color"  placeholder="" maxlength="30" />
                    </el-form-item>
                  </el-col>
                    <el-col :span="2">
                      <el-color-picker v-model="hexColor" style="float:right"></el-color-picker>
                  </el-col>

                </el-row>

                 <el-row>
                    <el-col :span="12">
                            <el-form-item size="small" label="透明度变化" prop="alpha" label-width="100px">
                                <el-input v-model="form.alpha"  placeholder="格式：百分比-百分比" maxlength="3" />
               
                            </el-form-item>
                    </el-col>
                    <el-col :span="12">
                            <el-form-item   size="small" label="运动耗时" prop="moveDuration" label-width="100px">
                                <el-input v-model="form.moveDuration"  placeholder="运动耗时" maxlength="10" >
                            <template slot="append">毫秒</template>
                          </el-input>
                            </el-form-item>
                    </el-col>
                </el-row>


          
    
                <el-row>
                  <el-col :span="12">
            
                  </el-col>
                  <el-col :span="12">

                  </el-col>
                </el-row>

                <el-row>
                  <el-col :span="12">
                        <el-form-item   size="small" label="起始x坐标" prop="startX" label-width="100px">
                        <el-input v-model="form.startX"  placeholder="起始x坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="起始y坐标" prop="startY" label-width="100px">
                          <el-input v-model="form.startY"  placeholder="起始y坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                </el-row>

                <el-row>
                  <el-col :span="12">
                        <el-form-item   size="small" label="结束x坐标" prop="endX" label-width="100px">
                        <el-input v-model="form.endX"  placeholder="起始x坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="结束y坐标" prop="endY" label-width="100px">
                          <el-input v-model="form.endY"  placeholder="结束y坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                </el-row>

               <el-row>
                  <el-col :span="12">
                          <el-form-item   size="small" label="发送模式" prop="sendMode" label-width="100px">
                            <el-select v-model="form.sendMode" placeholder="请选择发送模式">
                              <el-option label="测试发送弹幕(发送前1条)" value="0"></el-option>
                              <el-option label="正式发送弹幕" value="1"></el-option>
                            </el-select>
                          </el-form-item>
                  </el-col>
           
                    <el-col :span="12">

                      
                            <el-form-item   size="small" label="弹幕池" prop="pool" label-width="100px">
                              <el-select v-model="form.pool" placeholder="请选择弹幕池">
                                <el-option label="普通池" :value=0></el-option>
                                <el-option label="字幕池" :value=1></el-option>
                              </el-select>
                            </el-form-item>
                    </el-col>
                </el-row>


                <el-row>
                  <el-col :span="12">
                    <el-form-item size="small" label="上传lrc或ass" label-width="100px">
                        <input name="uploadFile" type="file" @change="addFile($event, 1)" id='uploadFile'/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                  </el-col>
                </el-row>

                <el-row  style="margin-top:0px">
                  <el-col :span="5">
                  <el-button size="medium"   @click="submitPreviewForm()" >预览信息</el-button>
                  </el-col>
                  <el-col :span="5">
                  <el-button size="medium"   @click="submitForm()" >发送弹幕</el-button>

                  </el-col>
                    <el-col :span="5">
                  <el-button size="medium"  @click="stopSendDanmaku()">停止发送</el-button>

                  </el-col>
                </el-row>


            </el-tab-pane>
 
            <el-tab-pane label="淡出部分" name="third">
                <el-row>
                       <el-col :span="12">
                      <el-form-item size="small" label="生存时间" prop="outDuration" label-width="100px">
                          <el-input v-model="form.outDuration"  placeholder="" maxlength="10" >
                            <template slot="append">秒</template>
                          </el-input>

                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item size="small" label="运动耗时" prop="outMoveDuration" label-width="100px">
                          <el-input v-model="form.outMoveDuration"  placeholder="" maxlength="10" >
                            <template slot="append">毫秒</template>
                          </el-input>
                      </el-form-item>
                    </el-col>
               
                </el-row>
                
                <el-row>
                  <el-col :span="12">
                        <el-form-item   size="small" label="起始x坐标" prop="outStartX" label-width="100px">
                        <el-input v-model="form.outStartX"  placeholder="起始x坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="起始y坐标" prop="outStartY" label-width="100px">
                          <el-input v-model="form.outStartY"  placeholder="起始y坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                </el-row>

                <el-row>
                  <el-col :span="12">
                        <el-form-item   size="small" label="结束x坐标" prop="outEndX" label-width="100px">
                        <el-input v-model="form.outEndX"  placeholder="起始x坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="结束y坐标" prop="outEndY" label-width="100px">
                          <el-input v-model="form.outEndY"  placeholder="结束y坐标" maxlength="10" />
                      </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item size="small" label="透明度变化" prop="outAlpha" label-width="100px">
                            <el-input v-model="form.outAlpha"  placeholder="格式：百分比-百分比"/>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-tab-pane>
            <el-tab-pane label="更多配置" name="fourth">
               <el-row>
                  <el-col :span="12">
                        <el-form-item   size="small" label="Z轴旋转" prop="zRotate" label-width="100px">
                        <el-input v-model="form.zRotate"  placeholder="Z轴旋转" maxlength="10" />
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="Y轴旋转" prop="yRotate" label-width="100px">
                          <el-input v-model="form.yRotate"  placeholder="Y轴旋转" maxlength="10" />
                      </el-form-item>
                  </el-col>
                </el-row>

                <el-row>
           
                  <el-col :span="12">
                        <el-form-item size="small" label="lrc间隙时间" prop="lrcIntervalTime" label-width="100px">
                        <el-input v-model="form.lrcIntervalTime"  placeholder="Lrc歌词用于处理句与句之间间隙时间,250为结尾提前250结束" maxlength="10" >
                          <template slot="append">毫秒</template>
                        </el-input>
                      </el-form-item>
                  </el-col>
                         <el-col :span="12">
                        <el-form-item size="small" label="重叠时间" prop="overlapTime" label-width="100px">
                        <el-input v-model="form.overlapTime"  placeholder="淡入、中间、淡出弹幕之间重叠时间" maxlength="10" >
                          <template slot="append">毫秒</template>
                        </el-input>
                      </el-form-item>
                  </el-col>
                </el-row>

                <el-row>
                  <el-col :span="12">
                  </el-col>
                  <el-col :span="12">
                      <el-form-item   size="small" label="线性加速" prop="access" label-width="100px">
                        <el-select v-model="form.access" placeholder="请选择弹幕池">
                          <el-option label="无线性加速" :value=0></el-option>
                          <el-option label="有线性加速" :value=1></el-option>
                        </el-select>
                      </el-form-item>
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
                  <el-form-item size="middle" label="效果模板" prop="effectValue" label-width="100px">
                    <el-select v-model="effectValue" filterable placeholder="请选择"  @change="selectTrigger(effectValue)">
                      <el-option
                        v-for="item in effectOptions"
                        :key="item.effectTem"
                        :label="item.effectTem"
                        :value="item">
                      </el-option>
                    </el-select>
                  </el-form-item>
              </el-col>
              <el-col :span="12">
                  <el-form-item size="middle" label="失败等待" prop="sendRetryInterval" label-width="100px">
                    <el-input v-model="form.sendRetryInterval"  placeholder="失败等待" maxlength="30" >
                        <template slot="append">分</template>
                    </el-input>
                  </el-form-item>
              </el-col>
            </el-row>
           <el-row  style="margin-top:0px">
                  <el-col :span="5">
                  <el-button  @click="saveEffectTem()" >保存模板</el-button>
                  </el-col>
            <el-col :span="5">
                  <el-button  @click="getEffectTem()" >读取模板</el-button>
                  </el-col>

              
                </el-row>
            </el-tab-pane>
          </el-tabs>
        
      </el-form>
    </el-card>


  </el-col>
  <!-- 日志区 -->
  <el-col :span="12">
    <el-card style="width:600px;float:left;min-height:616px">
      <span></span>
      <el-input
        type="textarea"
        :rows="2"
        placeholder="发送日志显示区域"
        v-model="textarea"
        :autosize="{ minRows: 26, maxRows: 26}"

        >
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
    this.getEffectTem()
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
        fontSize: 22,
        color: 'FFFFFF',
        zRotate: 0,
        yRotate: 0,
        fontFamily: '黑体',
        pool: 0,
        mode: 7,
        sendPrefix: null,
        sendSuffix: null,
        sendFirstDmTime: '',
        sendFirstDmOffset: null,
        sendInterval: '5',
        sendMode: '1',
        sendStartDmRow: '1',
        sendPreview: '1',
        bord: 1,
        access: 0,
        alpha: '1-1',
        startX: 20,
        startY: 60,
        endX: 25,
        endY: 60,
        // duration: 4,
        moveDuration: 500,
        inAlpha: '0-1',
        inDuration: 0.3,
        inMoveDuration: 300,
        inStartX: 5,
        inStartY: 60,
        inEndX: 20,
        inEndY: 60,
        outAlpha: '1-0',
        outDuration: 0.3,
        outMoveDuration: 300,
        outStartX: 25,
        outStartY: 40,
        outEndX: 60,
        outEndY: 60,
        overlapTime: 50,
        outOffset: 250,
        sendRandomTime: 0,
        sendRetryInterval: 1,
        lrcIntervalTime: 250,
        effectTem: null
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
      effectOptions: [],
      effectValue:null
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
          let param = new FormData() // 创建form对象
          param.append('file', this.txtFile) // 通过append向form对象添加数据
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
          this.axios.post('/danmaku/sendDanmakuM7', param, config).then(res => {
            // let res = JSON.stringify(response);
            if(res.data.code == 200){
              //1、预览是通过接口返回日志数据
              // if(type == 'preview'){
                this.textarea = res.data.data.txtLog
                console.log(this.textarea);
              // }
              //4、关闭轮询
              _this.stopSetInterval()
              this.timerStatus = 0;

            }
          }).catch(error =>  {
            _this.stopSetInterval()
            
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
      this.axios.post('/danmaku/sendDanmakuM7', param, config).then(res => {
        
        if(res.data.code == 200){
            this.textarea = res.data.msg
        }
      }).catch(error =>  {
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
          // console.log(this.textarea);
        }
      }).catch(error =>  {
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
          }).catch(error =>  {
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
      this.txtFile = file
      let param = new FormData() // 创建form对象
      param.append('file', file, file.name) // 通过append向form对象添加数据
      var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      const iTy = testmsg === 'lrc'
      const iTy2 = testmsg === 'ass'
      const iTy3 = testmsg === 'm1'
      // console.log(testmsg)
      const isLt2M = file.size / 1024 / 1024 < 10
      // const len = file.name.length
      // 判断格式
      if (!iTy && !iTy2 && !iTy3) {
        this.$message({
          message: '上传文件只能是 lrc、ass格式!',
          type: 'warning'
        })
        this.txtFile = null 
        this.uploadName = '请上传lrc或ass文件'
        e.target.value = ''
      } else if (!isLt2M) { // 判断大小
        this.$message({
          message: '上传文件大小不能 10MB!',
          type: 'warning'
        })
        e.target.value = ''
        this.txtFile = null
        this.uploadName = '请上传lrc或ass文件'
      } 
      this.uploadName = file.name
    },

    /**
     * 文件发生改变时
     */
    handleChangeLi(file, fileList) {
      console.log(file)
      console.log(fileList)
      this.fileListAdd = fileList
    },
    handlePictureCardPreview(file) {
      console.log(file)
    },

    // 保存模板
    saveEffectTem(){
      this.$prompt('请输入保存模板名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
          this.form.effectTem = value
          // console.log(JSON.stringify(this.form))
          this.axios.post('/danmaku/saveEffectTemM7', this.form).then(res => {
            if(res.data.code == 200){
              console.log(res.data)
            }
          }).catch(error =>  {
            console.log(error);
          });
          this.$message({
            type: 'success',
            message: '保存模板成功'
          });
        }).catch(() => {     
      });
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
      }).catch(error =>  {
        console.log(error);
      });
    },


    // 初始化模板
    getEffectTem(){
        this.axios.post('/danmaku/getEffectTemM7', this.form).then(res => {
        if(res.data.code == 200){
          this.effectOptions = res.data.data
          console.log(res.data)
        }
      }).catch(error =>  {
        console.log(error);
      });
    },

    // 选择模板后
    selectTrigger(val) {
      this.form = Object.assign(this.form, val)
      console.log(this.form)
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