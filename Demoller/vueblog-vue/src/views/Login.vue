<template>
    <div>
        <el-container>
            <el-header>
               <img class="mlogo" src='/img2.png' alt="">
            </el-header>
            <el-main>
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="ruleForm.username"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input type="password" v-model="ruleForm.password"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-main>
        </el-container>
    </div>

</template>

<script>
    export default {
        name: "Login.vue",
        data() {
            return {
                ruleForm: {
                    username: 'markerhub',
                    password: '111111'
                },
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'change' },
                        { min: 6, max: 18, message: '长度在 6 到 16 个字符', trigger: 'blur' }
                    ]
                }
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const _this = this
                        this.$axios.post("http://localhost:8081/login", this.ruleForm).then(res =>{

                                const jwt = res.headers['authorization']
                                const userInfo = res.data.data
                                //console.log(userInfo)

                                //把数据共享出去
                                _this.$store.commit("SET_TOKEN",jwt)
                                _this.$store.commit("SET_USERINFO",userInfo)

                                //获取
                                //console.log(_this.$store.getters.getUser)

                                //跳转
                                _this.$router.push("/index")
                            }
                        )
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style scoped>

    .el-header, .el-footer {
        background-color: #B3C0D1;
        color: #B3C0D1;
        text-align: center;
        line-height: 10px;
    }

    .el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        line-height: 2px;
    }

    .el-main {
        /*background-color: #E9EEF3;*/
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
        color: #333;
        text-align: center;
        line-height: 160px;
    }

    body > .el-container {
        margin-bottom: 40px;
    }

    .el-container:nth-child(5) .el-aside,
    .el-container:nth-child(6) .el-aside {
        line-height: 260px;
    }

    .el-container:nth-child(7) .el-aside {
        line-height: 320px;
    }
    .mlogo {
        height: 80%;
        margin-top: 5px;
    }
    .demo-ruleForm{
        max-width: 500px;
        margin: 0 auto;
    }
</style>
