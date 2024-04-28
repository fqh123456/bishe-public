<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff;width: 350px;height: 300px;padding: 20px;border-radius: 10px">
      <div style="margin: 20px 0;text-align: center;font-size: 24px"><b>登录</b></div>

      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0;text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
        </el-form-item>
      </el-form>

    </div>
  </div>
</template>

<script>
import {setRoutes} from "@/router";

export default {
  name: "Login",
  data(){
    return{
      user:{},
      rules:{
        username:[
          {required:true,message:"请输入用户名",trigger:'blur'},
          {min:1,max:10,message: "长度在1到10个字符",trigger: 'blur'}
        ],
        password: [
          {required:true,message:"请输入密码",trigger:'blur'},
          {min:1,max:15,message: "长度在1到15个字符",trigger: 'blur'}
        ],
      }
    }
  },
  methods:{
    login(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  //表单校验合法
          this.request.post("/Admin/login",this.user).then(res=>{
            if(res.code=='200'){
              // console.log(JSON.stringify(res.data.menus))
              localStorage.setItem("admin",JSON.stringify(res.data)) //存储用户信息到浏览器
              localStorage.setItem("menus",JSON.stringify(res.data.menus)) //存储用户信息到浏览器

              //动态设置路由
              setRoutes()
              if(res.data.role==='ROLE_ROOT'){
                this.$router.push("/")
              }else{
                this.$router.push("/front/home")
              }
              this.$message.success("登录成功")
            }else{
              this.$message.error(res.msg)
            }
          })
        }
      });

    }
  }
}
</script>

<style scoped>
.wrapper{
  height: 100vh;
  background-image: linear-gradient(to bottom right,#FC466B,#3F5EFB);
  overflow: hidden;
}
</style>