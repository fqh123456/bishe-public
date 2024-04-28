<template>
  <div style="font-size: 12px; line-height:60px; display: flex">
    <div style="flex: 1;font-size: 18px">
      <span :class="collapseBtnClass" style="cursor:pointer" @click="collapse"></span>

    </div>
<!--    <div style="text-align: right; font-size: 12px; width:200px">-->
    <div>
      <el-dropdown style="width: 70px; cursor: pointer">
        <span>{{ user.username }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>
            <router-link to="/person">个人信息</router-link>
          </el-dropdown-item>
          <el-dropdown-item>
            <span style="text-decoration: none" @click="logout">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

  </div>

</template>

<script>
export default {
  name: "Header",
  props:{
    collapseBtnClass:String,
    collapse:Boolean,
    user:Object
  },
  data(){
    return{
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")):{},
    }
  },
  methods:{
    // collapse(){
    //   this.$emit("asideCollapse")
    // },
    logout(){
      // this.$store.commit("logout")
      localStorage.removeItem("user")
      localStorage.removeItem("menus")
      this.$router.push("/login")
      this.$message.success("退出成功")
    },

  },
}
</script>

<style scoped>

</style>