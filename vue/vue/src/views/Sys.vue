<template>
  <div>
    <el-button  @click="getdata">点击</el-button>
  </div>

</template>

<script>
export default {
  name: "Sys",
  data(){

  },
  methods: {
    getdata() {
      this.pyrequest.get("/get_data").then(res => {
        console.log(res)
      })
    }
  }
}
</script>

<style scoped>

</style>