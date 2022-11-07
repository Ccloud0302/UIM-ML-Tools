<template>
  <el-aside :width="isCollapse ? '64px' : '150px'">
    <div class="toggle-button" @click="toggleCollapse">
      |||
    </div>
    <el-menu background-color="#333744" text-color="#fff" active-text-color="#409eff" unique-opened
             :collapse="isCollapse" :collapse-transition="false" :router="true">
      <!--:router="true"开启路由，由index实现-->
      <!--一级菜单-->
      <el-submenu index="1">
        <!--一级菜单的模板区-->
        <template slot="title">
          <!--图标-->
          <i class="el-icon-location"></i>
          <!--文本-->
          <span>领域管理</span>
        </template>
        <!--二级菜单-->
        <el-menu-item index="/sservice">
          <template slot="title">
            <!--                                    图标-->
            <i class="el-icon-menu"></i>
            <!--                                    文本-->
            <span>领域</span>
          </template>
        </el-menu-item>
      </el-submenu>
      <!--一级菜单-->
      <el-submenu index="2">
        <!--一级菜单的模板区-->
        <template slot="title">
          <!--图标-->
          <i class="el-icon-location"></i>
          <!--文本-->
          <span>其他</span>
        </template>
        <!--二级菜单-->
        <el-menu-item index="/form">
          <template slot="title">
            <!--图标-->
            <i class="el-icon-menu"></i>
            <!--文本-->
            <span>其他</span>
          </template>
        </el-menu-item>
      </el-submenu>
    </el-menu>
  </el-aside>
</template>

<script>
export default {
  name: "aside_bo",
  data() {
    return {
      service: {},
      ruleForm: {
        name: ''
      },
      //是否折叠菜单
      isCollapse: false
    }
  },
  methods: {

    //折叠菜单
    toggleCollapse() {
      this.isCollapse = !this.isCollapse
    }
  }
}
</script>

<style scoped>
.el-aside {
  background-color: #333744;
  color: #333;
  padding-left: 0; /*靠左*/
  text-align: left;
  line-height: 200px;
}

.el-menu {
  border-right: none;
}

.toggle-button {
  background-color: #4A5064;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2em;
  /*鼠标放上去变成小手*/
  cursor: pointer;
}


</style>