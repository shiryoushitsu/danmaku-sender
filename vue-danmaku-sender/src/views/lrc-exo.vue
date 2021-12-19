<template>
  <div >
<el-row :gutter="20" style="margin-left:auto;margin-right:auto;margin-top:20px;background-color: rgb(248, 250, 251);">
  <!-- 配置区 -->
  <el-col :span="12" >
    <el-card style="width:600px;float:right;min-height:616px" body-style="padding-top:22px">
      <el-form ref="form" :model="form" label-width="80px" :rules="rules" :hide-required-asterisk="true" :validate-on-rule-change="false">
        <el-row>
          <el-col :span="12">
            <div class="el-form-item">
              <el-form-item size="middle" label="上传lrc或ass" label-width="100px">
                  <input name="uploadFile" type="file" @change="addFile($event, 1)" id='uploadFile'/>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
      <el-row  style="margin-top:0px">
        <el-col :span="6">
          <el-button  @click="submitForm('preview')" >预览EXO文件</el-button>
        </el-col>
        <el-col :span="6">
          <el-button  @click="submitForm('save')" >下载TXT文件</el-button>
        </el-col>
      </el-row>

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
        style="">
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
        cookie: null,
        csrf: null,
        bvid: null,
        part: '1',
        fontSize: '25',
        color: 'FFFFFF',
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
          let config = {
              headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
          }
          this.axios.post('/aul/lrc2exo', param, config).then(res => {
            if(res.data.code == 200){
                this.textarea = res.data.data.lrfExoStr
                // console.log(this.textarea);
                if( type == 'save'){
                  debugger
                  var data = res.data.data.lrfExoStr
                  let str = new Blob([data], {type: 'text/plain;charset=ANSI'});
                  let exoName = this.txtFile.name.substring(0, this.txtFile.name.lastIndexOf('.')) + `.txt`
                  saveAs(str, exoName);
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
      // let codeArr = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
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