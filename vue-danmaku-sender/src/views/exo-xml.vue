<template>
  <div >
<el-row :gutter="20" style="margin-left:auto;margin-right:auto;margin-top:20px;background-color: rgb(248, 250, 251);">
  <!-- 配置区 -->
  <el-col :span="12" >
    <el-card style="width:600px;float:right;min-height:616px" body-style="padding-top:22px">
      <el-form ref="form" :model="form" label-width="80px" :rules="rules" :hide-required-asterisk="true" :validate-on-rule-change="false">
        <el-row>
          <el-col :span="12">
            <el-form-item size="middle" label="坐标相关" prop="isPercentage" label-width="100px" >
                <el-checkbox v-model="form.isPercentage">是否百分比坐标</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12">
           <el-form-item size="middle" label="层级效果" prop="isLayer" label-width="100px" >
                <el-checkbox v-model="form.isLayer">是否用z轴来增加时间</el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
              <el-form-item size="middle" label="描边相关" prop="isOutline" label-width="100px">
                  <el-checkbox v-model="form.isOutline">是否全部弹幕描边</el-checkbox>
              </el-form-item>
          </el-col>
          <el-col :span="12">
              <el-form-item size="middle" label="线性加速" prop="isLinearSpeedup" label-width="100px">
                  <el-checkbox v-model="form.isLinearSpeedup">是否线性加速</el-checkbox>
              </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-row>
          <el-col :span="12">
              <el-form-item size="middle" label="视频宽度" prop="cookie" label-width="100px">
                    <el-input v-model="form.screenWidth"   placeholder="视频宽度"  />
                  </el-form-item>
          </el-col>
          <el-col :span="12">
                  <el-form-item size="middle" label="视频长度" prop="bvid" label-width="100px">
                    <el-input v-model="form.screenHeight"  placeholder="请输入视频BV号"  />
                  </el-form-item>
          </el-col>
        </el-row> -->




        <el-row>
          <el-col :span="12">

                  <div class="el-form-item">
                  <el-form-item size="middle" label="上传exo文件" label-width="100px">
                      <input name="uploadFile" type="file" @change="addFile($event, 1)" id='uploadFile'/>

                  </el-form-item>
                    <!-- <span class="el-form-item__label" style="wigth:100px">提交Lrc文件</span> -->
                  </div>
          </el-col>
            <el-col :span="12">

            </el-col>
        </el-row>

      <el-row  style="margin-top:0px">
        <el-col :span="6">
          <el-button  @click="submitForm('preview')" >预览XML文件</el-button>
        </el-col>
        <el-col :span="6">
          <el-button  @click="submitForm('save')" >下载XML文件</el-button>
        </el-col>
      </el-row>




      </el-form>
    </el-card>
  </el-col>
  <!-- 日志区 -->
  <el-col :span="12">
    <el-card style="width:600px;float:left;min-height:616px">
      <el-input
        type="textarea"
        :rows="2"
        placeholder="XML显示区域"
        v-model="textarea"
        :autosize="{ minRows: 22, maxRows: 22}"
        wrap="off" 
        style="">
      </el-input>
      <el-input
        type="textarea"
        placeholder="预览命令显示区域"
        v-model="preTextarea"
        :rows="2"
        wrap="off" 
        :autosize="{ minRows: 4, maxRows: 4}"
        style="margin-top:10px">
      </el-input>

    </el-card>
  </el-col>
</el-row>

  </div>
</template>

<script>
import qs from 'qs'
import {saveAs} from 'file-saver'
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
  data() {
    return {
      value1:'', 
      form:{
        screenWidth: 850,
        screenHeight: 560,
        isPercentage: true,
        isLayer: true,
        isOutline: false,
        isLinearSpeedup: false,
        pool: '0',
        mode: '4',
        sendPrefix: null,
        sendSuffix: null,
        sendFirstDmTime: null,
        sendFirstDmOffset: null,
        sendInterval: '6',
        sendMode: '1',
        sendStartDmRow: '1',
        sendPreview: '1',
        sendRandomTime: '0',
        sendRetryInterval: '1',
        
      },
      rules: {  
      },
      textarea: '',
      preTextarea: '',
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
    submitForm(type){
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
          debugger
          let config = {
              headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
          }
          this.axios.post('/aul/exo2xml', param, config).then(res => {
            if(res.data.code == 200){
                this.textarea = res.data.data.xmlStr
                this.preTextarea = res.data.data.preStr
                if( type == 'save'){
                  var data = res.data.data.xmlStr
                  let str = new Blob([data], {type: 'text/plain;charset=utf-8'});
                  let xmlName = this.txtFile.name.substring(0, this.txtFile.name.lastIndexOf('.')) + `.xml`
                  saveAs(str, xmlName);
                }
            }
          }).catch(function (error) {
            console.log(error);
          });

        }
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
      this.txtFile = file
      let param = new FormData() // 创建form对象
      param.append('file', file, file.name) // 通过append向form对象添加数据
      var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      const iTy = testmsg === 'exo'
      const isLt2M = file.size / 1024 / 1024 < 10
      if (!iTy) {
        this.$message({
          message: '上传文件只能是exo格式!',
          type: 'warning'
        })
        this.txtFile = null 
        this.uploadName = '上传文件只能是exo格式'
        e.target.value = ''
      } 
      this.uploadName = file.name
    },


  },
  

}
</script>
<style scoped>
/*a  upload */
.a-upload {
    padding: 4px 10px;
    height: 20px;
    line-height: 20px;
    position: relative;
    cursor: pointer;
    color: #888;
    background: #fafafa;
    border: 1px solid #ddd;
    border-radius: 4px;
    overflow: hidden;
    display: inline-block;
    *display: inline;
    *zoom: 1
}

.a-upload  input {
    position: absolute;
    font-size: 100px;
    right: 0;
    top: 0;
    opacity: 0;
    filter: alpha(opacity=0);
    cursor: pointer
}

.a-upload:hover {
    color: #444;
    background: #eee;
    border-color: #ccc;
    text-decoration: none
}

</style>