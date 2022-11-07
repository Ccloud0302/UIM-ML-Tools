<template>
  <el-header height="80px">
    <div>
      <img src="/img2.png" height="85" width="224" alt="">
      <span>面向智能制造的领域建模平台</span>
<!--      <span>Modelling System For Manufacturing</span>-->
    </div>
    <div>
      <el-button type="info" @click="search">搜索</el-button>
      <el-button type="info" @click="toIndex">主页</el-button>
    </div>
  </el-header>
</template>

<script>

export default {
  name: "UbmlHeader",
  methods:{
    search(){
      this.$router.push({name: 'search'})
    },
    toIndex() {
      this.$router.replace('/')
    },
  }
}
</script>

<style scoped>
.el-header {
  background-color: #e9eff4;
  color: #050606;
  display: flex;
  justify-content: space-between;
  padding-left: 0; /*靠左*/
  align-items: center; /*按钮不再贴边*/
  /*字体大小*/
  font-size: 20px;

}

.el-header > div {
  display: flex;
  align-items: center;
}

.m_content {
  height: 20%;

  top: 0px;
  left: 0px;
  width: 100%;
}

.ziti {
  color: #FFFFFF;
  font-weight: bold;
  font-size: 32px;
  text-align: left;

}

.el-avatar {
  position: absolute;
  margin-top: 10px;
  margin-left: 500px;
}

</style>
