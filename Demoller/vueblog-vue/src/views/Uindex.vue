<template>
  <div class="container">
    <el-container style="height: 100%" direction="vertical">
      <UbmlHeader></UbmlHeader>
      <el-container>
        <el-main>
          <!--路由占位符-->
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import UbmlHeader from "../components/UbmlHeader";
import aside_index from "../components/aside_index";

export default {
  name: "Uindex",
  components: {UbmlHeader, aside_index},
}
</script>

<style scoped>
.el-main {
  background-color: #eaedf1;
  color: #333;
  text-align: center;
  line-height: 50px;
}
.container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>
