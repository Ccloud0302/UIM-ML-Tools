<template>
  <div class="listBox" id="mainArea" style="padding:15px;overflow-y: scroll;">
      <el-row style="margin-bottom: 20px">
        <el-col :span="1">
          <el-button @click="addDialogVisible=true">添加领域</el-button>
        </el-col>
      </el-row>
    <!-- 列表区域  -->

      <el-row>
        <el-col :span="4" v-for="(item,index) in sservicesList" :key="index" :offset="index === 0 ? 0 : (index%5 === 0 ? 0 : 1)"
                style="margin-bottom:25px;'">
          <el-tooltip placement="top" effect="light">
            <template #content>
              双击进入{{ item.name }}
            </template>
            <el-card :body-style="{ padding: '0px' }" @dblclick.native="gotoBusiness(item.id)" style="cursor: pointer">
              <div style="padding: 14px;">
                <div class="sub-title">领域</div>
                <div class="title"><h1>{{ item.name }}</h1></div>
                <div class="bottom clearfix">
                  <el-button type="text" class="button" @click="deleteSservice(item.id)">删除</el-button>
<!--                  <el-button type="text" class="button" @click="gotoBusiness(item.id)">查看</el-button>-->
<!--                  <el-button type="text" class="button" @click="gotoMxgraph(item.id)">查看工艺流程</el-button>-->
                </div>
              </div>
            </el-card>
          </el-tooltip>
        </el-col>
      </el-row>

    <!--分页区域-->
    <div id="pager" class="pager01">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[2, 4, 6, 8]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!--添加微服务的对话框   :visible.sync: 当:visible.sync的值为ture的时候，弹框显示，当为false的时候，弹框隐藏-->
    <el-dialog title="添加领域" :visible.sync="addDialogVisible" width="40% ">
      <!--内容主体区-->
      <el-form>
        <el-form-item label="领域名称">
          <el-input style="width: 60%" title="type" v-model="domainName"></el-input>
        </el-form-item>
      </el-form>
      <!--      <el-input :value="addForm.name"></el-input>-->
      <!--底部区域-->
      <span slot="footer" class="dialog-footer">
      <el-button @click="addDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="addSservice()">确 定</el-button>
      </span>
    </el-dialog>
  </div>


</template>

<script>

import $ from "jquery";


let protocol = window.location.protocol; //协议
let host = window.location.host; //主机
let reg = /^localhost+/;
let host_l = host.split(":")
let contextRoot = protocol + "//" + host_l[0]  +":8081/";

export default {
  name: "Welcome",
  created() {
    this.getAllSservice();
  },
  data() {
    return {
      sservicesList: [],
      currentPage: 1,
      total: 0,
      pageSize: 15,
      name: '',
      addDialogVisible: false,
      domainName: '',
    }
  },
  methods: {
    addSservice(){
      var _this = this;
      var data = {name: _this.domainName};
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        url: contextRoot + "addSservice",
        contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.getAllSservice();
            _this.addDialogVisible = false;

          } else {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'warning'
            });
          }
        }
      });
    },
    deleteSservice(id){
      var _this = this;
      var data = {id: id};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "deleteSservice",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.getAllSservice();
          } else {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'warning'
            });
          }
        }
      });
    },
    //监听pagesize改变的事件
    handleSizeChange(newSize) {
      // console.log(newSize)
      this.pageSize = newSize
      this.getSserviceList()
    },
    //监听页码值改变的事件
    handleCurrentChange(newPage) {
      // console.log(newPage)
      this.currentPage = newPage
      this.getSserviceList()
    },
    deleteCount() {
    },
    async getAllSservice() {
      var _this = this;
      await _this.$axios.get("/sservices?currentPage=" + this.currentPage + "&pageSize=" + this.pageSize + "&name=" + this.name).then(res => {
        _this.sservicesList = res.data.data.records;
        _this.currentPage = res.data.data.current;
        _this.total = res.data.data.total;
        _this.pageSize = res.data.data.size;
      })
      console.log(_this.sservicesList);
    },
    click(x) {
      var _this = this;
      console.log(x);
      // _this.$router.replace('/boIndex')
    },
    gotoBusiness(id){
      // this.$router.replace('/boIndex');
      this.$router.push({
        name: 'boIndex',
        query: {
          sserviceId: id,
        }
      })
    },
    // gotoMxgraph(id){
    //   this.$router.push({
    //     name: 'MxgraphContainer',
    //     query: {
    //       sserviceId: id,
    //     }
    //   })
    // }
  }
}
</script>

<style scoped>

/* 中间列表 */
.page_l { /***关键点3：设置列表部分overflow样式!***/
  float: left;
  margin-left: 1%;
  overflow: scroll;
  overflow-x: hidden;
  overflow-y: auto;
}

/* 底部分页样式 */
.pager01 {
  left: 0;
  bottom: 0;
  width: 100%;
  height: 50px;
  position: fixed;
  text-align: center;
  list-style: none;
  padding-top: 8px;
}
</style>
