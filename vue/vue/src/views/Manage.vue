<template>

  <div class="home">
    <el-container style="min-height: 100vh;">
      <el-aside :width="sideWith+'px'" style="background-color: rgb(238, 241, 246) ; min-height:100%">
        <Aside :isCollapse="isCollapse"/>
      </el-aside>

      <el-container>
        <el-header style="border-bottom: 1px solid #ccc; ">
          <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"/>
        </el-header>

        <el-main>
<!--          表示当前页面的子路由会在<router-view/>中展示-->
          <router-view   @refreshUser="getUser"/>
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import Aside from "@/components/Aside";
import Header from "@/components/Header";
// import {request} from "axios";
// import request from "@/utils/request";

export default {
  name: 'HomeView',
  components: {
    Header,
    HelloWorld,
    Aside
  },
  data(){
    return{
      collapseBtnClass:'el-icon-s-fold',
      isCollapse:false,
      sideWith:200,
      user:{},
    }
  },
  created() {
    // 从后台获取最新的User数据
    this.getUser()
  },
  methods: {
    collapse() { //点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {
        this.sideWith = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
      } else {
        this.sideWith = 200
        this.collapseBtnClass = 'el-icon-s-fold'
      }
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // 从后台获取User数据
        this.request.get("/user/username/" + username).then(res => {
          // 重新赋值后台的最新User数据
          this.user = res.data
          console.log(this.user)
        })
      }
    }

  },
}
</script>
