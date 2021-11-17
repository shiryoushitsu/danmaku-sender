<template> 
  <div> 

  <div style="display: flex;justify-content: center;">
    <el-card style="width:1000px;min-height:100px;margin-top:22px;" > 
      <el-form ref="form" :model="form" label-width="80px" :rules="rules" :hide-required-asterisk="true" :validate-on-rule-change="false">
        
      <el-row>
          <el-col :span="21">
          <el-form-item size="middle" label="" prop="content" label-width="0px">
          <el-input v-model="form.content"  placeholder="文字"  />
        </el-form-item>
          </el-col>
          <el-col :span="3">
                  <el-button @click="submitForm()">生成字符字</el-button>
        </el-col>
            
          </el-row>




      </el-form>

    </el-card> 
  </div>
  <div style="display: flex;justify-content: center;">
    <!-- 日志区 --> 
    <el-card style="width:1000px;min-height:400px;margin-top:22px;"> 
      <el-input class="output" type="textarea" :rows="2" placeholder="显示区域" v-model="textarea" :autosize="{ minRows: 18, maxRows: 20}"> 
      </el-input> 
    </el-card> 
  </div>
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
  },
  data() {
    return {
      value1:'', 
      form:{
        content: '',
        font: '25',
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
      this.$refs["form"].validate(valid => {
        if (valid) {
          let _this = this;
          this.textarea = ''
          let params = new FormData()
          for(let key in this.form){
            if(this.form[key] != null){
              params.append(key,this.form[key])
            }
          }
          let config = {
              headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
          }
          this.axios.get('/tool/asciiText', {
            params:
          {content:this.form.content,
          font:this.form.font,
          }
          
          }, config).then(res => {
            if(res.data.code == 200){
                this.textarea = res.data.data.asciiText
                console.log(this.textarea);
              this.timerStatus = 0;

            }
          }).catch(function (error) {
            console.log(error);
          });
        }
      });
    },

  },
  
}
</script>
<style scoped  lang="css">
.container {
  margin: 0 auto;
}

textarea {
  font-family: SimHei !important;

}

.el-textarea__inner{
  line-height: 1 !important;
}
</style>

<style >
.output .el-textarea__inner{
  line-height: 1 !important;
}
</style>