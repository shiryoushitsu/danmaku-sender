<template> 
  <div> 

  <div style="display: flex;justify-content: center;">
    <el-card style="width:1000px;min-height:60px;margin-top:22px;" > 
      <el-form ref="form" :model="form" label-width="80px" :rules="rules" :hide-required-asterisk="true" :validate-on-rule-change="false">
        
      <el-row :gutter="2">
          <el-col :span="9">
       <el-input v-model="form.content"  placeholder="文字" />
          </el-col>
          <el-col :span="4">
              <el-select v-model="form.removeSpace"  placeholder="去除空格" >
                <el-option
                  v-for="item in spaceOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
          </el-col>


          <el-col :span="4">
              <el-select v-model="form.isChar"  placeholder="换行" >
                <el-option
                  v-for="item in lineOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="form.font"  placeholder="请输入文字" >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="3">
                  <el-button @click="submitForm()">生成字符字</el-button>
        </el-col>
            
          </el-row>




      </el-form>

    </el-card> 
  </div>
  <div style="display: flex;justify-content: center;">
    <!-- 显示区 --> 
    <el-card style="min-width:1000px;min-height:400px;margin-top:22px;"> 
      <el-input class="output"  wrap="off" type="textarea" :rows="2" placeholder="显示区域" v-model="textarea" :autosize="{ minRows: 24, maxRows: 26}"> 
      </el-input> 
    </el-card> 
  </div>
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
  created(){
  },
  data() {
    return {
      value1:'', 
      value:'', 
      form:{
        content: '',
        isChar: 'true',
        removeSpace: 'true',
        font: '方正像素16',
      },
      options: [{
        value: '方正像素16',
        label: '方正像素16'
      }, {
        value: '微软雅黑',
        label: '微软雅黑'
      }, {
        value: '黑体',
        label: '黑体'
      }, 
      {
        value: 'JF Dot jiskan24',
        label: 'JF-Dot-jiskan24'
      },
      {
        value: 'DFDotDotR12',
        label: 'DF点々体12(日)'
      },
      {
        value: 'JF Dot K12',
        label: 'JF-Dot-K12'
      }, {
        value: 'JF Dot M+ 10',
        label: 'JF-Dot-MPlus10(日)'
      }, {
        value: 'JF Dot NagaMin 10',
        label: 'JF-Dot-Naga10(日)'
      }, {
        value: 'Zpix',
        label: 'Zpix'
      }],
      lineOptions: [{
        value: 'true',
        label: '逐字换行'
      }, {
        value: 'false',
        label: '一行文字'
      }
      ],
      spaceOptions: [{
        value: 'true',
        label: '去除四周空格'
      }, {
        value: 'false',
        label: '保留空格'
      }
      ],
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
          let config = {
              headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
          }
          this.axios.get('/tool/asciiText', {
            params: {content:this.form.content, font:this.form.font,isChar:this.form.isChar,removeSpace:this.form.removeSpace}
          }, config).then(res => {
            if(res.data.code == 200){
              this.textarea = res.data.data.asciiText
              console.log(this.textarea);
              // var data = res.data.data.asciiText
              // let str = new Blob([data], {type: 'text/plain;charset=utf-8'});
              // // 注意这里要手动写上文件的后缀名
              // saveAs(str, `文件一.txt`);
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
el-form-item{ margin-bottom: 0px;}



.el-textarea__inner{
  line-height: 1 !important;
}
</style>

<style >
.output .el-textarea__inner{
  line-height: 1 !important;
}
</style>