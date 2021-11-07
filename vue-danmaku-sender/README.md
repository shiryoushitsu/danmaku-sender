# vue-danmaku-sender
采用vue + elementui作为前端页面，至于为啥那么麻烦还分前后端是因为把它当练手项目..

### 安装依赖
npm install --registry=https://registry.npm.taobao.org

### 开发
npm run dev

### 打包
npm run build

需要提前注释掉main.js中的axios.defaults.baseURL = '/api'
执行命令将会打包到springboot后端静态资源static位置
之后可对springboot后端打包生成完整的jar包
