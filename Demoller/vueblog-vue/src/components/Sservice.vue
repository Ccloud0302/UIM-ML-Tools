<template>
  <div>
    <!--面包屑导航区-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>领域管理</el-breadcrumb-item>
      <el-breadcrumb-item>领域</el-breadcrumb-item>
    </el-breadcrumb>

    <!--卡片视图区域-->
    <el-card>
      <el-row>
        <el-col :span="7">
          <!--搜索 双向绑定  -->
          <el-input placeholder="请输入内容" v-model="name" clearable @clear="getSserviceList">
            <el-button slot="append" icon="el-icon-search" @click="getSserviceList"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogVisible=true">添加领域</el-button>
        </el-col>
      </el-row>
      <!--微服务列表区域-->
      <el-table :data="sservices" border stripe>
        <el-table-column label="#" type="index"></el-table-column>
        <el-table-column label="名称" prop="name" width="100"></el-table-column>
        <el-table-column label="操作">
          <!--不行的话弹幕上说v-slot-->
          <template slot-scope="scope">
            <!--查看按钮-->
            <el-button type="primary" icon="el-icon-view" @click="gotoBusiness"></el-button>
            <!--修改按钮-->
            <el-button type="warning" icon="el-icon-edit" @click="showEditDialog(scope.row.id)"></el-button>
            <!--删除按钮-->
            <el-button type="danger" icon="el-icon-delete" @click="removeSservice(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--分页区域-->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[2, 4, 6, 8]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-card>

    <!--添加微服务的对话框   :visible.sync: 当:visible.sync的值为ture的时候，弹框显示，当为false的时候，弹框隐藏-->
    <el-dialog title="添加领域" :visible.sync="addDialogVisible" width="40% " @close="addDialogClosed">
      <!--内容主体区-->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px" prop="sname">
        <el-form-item label="领域名称" prop="name">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
      </el-form>
      <!--      <el-input :value="addForm.name"></el-input>-->
      <!--底部区域-->
      <span slot="footer" class="dialog-footer">
      <el-button @click="addDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="addSservice('addFormRef')">确 定</el-button>
      </span>
    </el-dialog>

    <!--        修改的对话框-->
    <el-dialog title="修改领域名称" :visible.sync="editDialogVisible" width="40%">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px" prop="editFormname">
        <el-form-item label="领域名称" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="editDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="editSservice('editFormRef')">确 定</el-button>
      </span>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "Sservice",
  data() {
    return {
      sservices: [],
      name: '',
      currentPage: 1,
      total: 0,
      pageSize: 4,

      addDialogVisible: false,
      //添加微服务的表单数据
      addForm: {
        name: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        name: [
          {required: true, message: '请输入微服务名称', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
        ]
      },

      editDialogVisible: false,
      editForm: {
        id: 0,
        name: ''
      },
      //添加表单的验证规则对象
      editFormRules: {
        name: [
          {required: true, message: '请输入微服务名称', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.getSserviceList()
  },
  methods: {
    //查看某个微服务的业务
    gotoBusiness() {
      this.$router.replace('/boIndex')
    },
    getSserviceList() {
      const _this = this
      _this.$axios.get("/sservices?currentPage=" + this.currentPage + "&pageSize=" + this.pageSize + "&name=" + this.name).then(res => {
        // console.log(res)
        _this.sservices = res.data.data.records
        _this.currentPage = res.data.data.current
        _this.total = res.data.data.total
        _this.pageSize = res.data.data.size

      })
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
    //监听添加用户对话框的关闭事件
    addDialogClosed() {
      this.addForm.name = ''
    },
    //添加微服务
    addSservice(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this
          this.$axios.post('/sservices?userId=' + this.$store.getters.getUser.id + '&name=' + this.addForm.name, '', {
            headers: {
              "Authorization": localStorage.getItem("token")
            }
          }).then(res => {
            this.$message.success('添加成功！')
            this.addDialogVisible = false
            //重新获取用户列表数据
            this.getSserviceList()
          })


        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    //展示修改微服务对话框
    showEditDialog(id) {
      this.editForm.id = id
      const _this = this
      _this.$axios.get("/sservices/" + id).then(res => {
        // console.log(res)
        _this.editForm.name = res.data.data.name
        // _this.editForm.id = res.data.data.id
        // console.log(this.editForm.name)
      })
      this.editDialogVisible = true
    },
    //修改微服务并提交
    editSservice(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this
          this.$axios.put('/sservices', this.editForm, {
            headers: {
              "Authorization": localStorage.getItem("token")
            }
          }).then(res => {
            this.$message.success('修改成功！')
            this.editDialogVisible = false
            //重新获取用户列表数据
            this.getSserviceList()
          })

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    //根据id删除对应用户信息
    async removeSservice(id) {
      //询问是否删除
      const confirmResult = await this.$confirm('此操作将永久删除该项, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => {
        return err
      })
      //如果用户确认删除，返回字符串  confirm   如果取消，返回字符串  cancel
      //  console.log(confirmResult)
      if (confirmResult != 'confirm') {
        return this.$message.info('取消删除')
      }
      this.$axios.delete('/sservices?id=' + id, {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        this.$message.success('删除成功！')
        //重新获取用户列表数据
        this.getSserviceList()
      })

    }
  }
}
</script>

<style scoped>
.el-breadcrumb {
  margin-bottom: 15px;
}

.el-card {
  /*box-shadow: 0 1px 1px rgba(0,0,0,0.15) ;*/
  /*改完这个表头就不会太高了*/
  line-height: 20px !important;
}

.el-table {
  margin-top: 15px;
  font-size: 12px;
}

.el-row {
  display: flex;
  height: 30px;
  align-items: center;

}

</style>