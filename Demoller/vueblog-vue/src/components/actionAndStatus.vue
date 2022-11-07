<template>
  <el-scrollbar class="mind-cen" id="actionAndStatusDiv">
    <div class="common-layout">
    <el-container>
      <el-header>
        <el-select v-model="value" clearable placeholder="请选择">
          <el-option
              v-for="item in nodeList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-header>
      <el-main>Main</el-main>
    </el-container>
    </div>
  </el-scrollbar>
</template>

<script>
export default {
  name: "actionAndStatus",
  data() {
    return {
      value: ''
    }
  }
}
</script>

<style scoped>

</style>