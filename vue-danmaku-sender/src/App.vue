<template>
  <div id="app" style="background-color:#F8FAFB;">
    <div>
      <TopNavMenu/>
    </div>
<!-- 
    <div id="nav">
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>
    </div> -->
    <router-view/>
    <footer style="height:30px;background-color: rgb(248, 250, 251);">  </footer>
  </div>

</template>
<script>
import TopNavMenu from "@/components/TopNavMenu";
export default {
  components: {
    TopNavMenu
  }
}
</script>
<style>

/* * {font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;} */

body {
  margin: 0px;
  padding: 0px;
  background-color: #F8FAFB;
}
#app{
  height:100vh;
}


#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
