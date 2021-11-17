<template>
  <div>
    <el-menu :default-active="$route.path"  router class="el-menu-demo" mode="horizontal" @select="handleSelect" >
      <el-menu-item index="/">发送普权弹幕歌词</el-menu-item>
      <el-menu-item index="m7">发送高级弹幕歌词</el-menu-item>
      <el-menu-item index="xml">发送XML弹幕文件</el-menu-item>
      <el-menu-item index="ascii-text">生成字符字</el-menu-item>
      <el-menu-item index=""><a href="http://doc.danmaku.fans" target="_blank" style="text-decoration:none">使用指南</a></el-menu-item>
      <!-- <el-menu-item index="5"/>其他工具</el-menu-item> -->
      
      <!-- <el-menu-item index="6">返回博客</el-menu-item>   -->
      <!-- <el-submenu index="2">
        <template slot="title">我的工作</template>
        <el-menu-item index="2-1">选项1</el-menu-item>
        <el-menu-item index="2-2">选项2</el-menu-item>
        <el-menu-item index="2-3">选项3</el-menu-item>
        <el-submenu index="2-4">
          <template slot="title">选项4</template>
          <el-menu-item index="2-4-1">选项1</el-menu-item>
          <el-menu-item index="2-4-2">选项2</el-menu-item>
          <el-menu-item index="2-4-3">选项3</el-menu-item>
        </el-submenu>
      </el-submenu>
      <el-menu-item index="3" disabled>消息中心</el-menu-item>
      <el-menu-item index="4"><a href="https://www.ele.me" target="_blank">订单管理</a></el-menu-item> -->
    </el-menu>
  </div>
</template>

<script>
export default {
  name: 'TopNavMenu',
  props: {
    msg: String
  },
  data() {
    return {
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.el-menu-demo{
  padding-left: 170px;
  padding-right: 100px;
}
</style>
