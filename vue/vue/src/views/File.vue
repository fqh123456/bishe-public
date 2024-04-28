<template>
  <div>
    <div>
      <el-upload action="http://localhost:9090/file/upload"
                 style="display: inline-block"
                 :show-file-list="false"
                 :on-success="handleFileUploadSuccess"
      >
        <el-button type="primary">上传文件<i class="el-icon-top"></i></el-button>
      </el-upload>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="文件名"></el-table-column>
      <el-table-column prop="type" label="文件类型"></el-table-column>
      <el-table-column prop="size" label="文件大小(kb)"></el-table-column>

      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable"
                     active-color="#13ce66"
                     inactive-color="#ccc"
                     @change="changeEnable(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="下载">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">

        <template slot-scope="scope">
          <el-popconfirm
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="这是一段内容确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i> </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[3, 5, 10]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
export default {
  name: "File",
  data(){
    return{
      tableData:[],
      pageSize:5,
      pageNum:1,
      total:0,
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      this.request.get("/file/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data.records
        this.total=res.data.total
      })
    },
    changeEnable(row){
      this.request.post("/file/update",row).then(res=>{
        if(res.code==='200'){
          this.$message.success("操作成功")
        }
      })
    },
    del(id){
      this.request.delete("/file/"+id).then(res =>{
        if(res.code==='200'){
          this.$message.success("删除成功")

          //处理删除每页最后一条数据时跳转页面
          const totalPage = Math.ceil((this.total - 1) / this.pageSize)
          //记录当前页码
          //记住删除最后一条数据时当前码是最后一页
          const pagenum = this.pageNum > totalPage ? totalPage : this.pageNum
          //实现跳转
          this.pageNum = pagenum < 1 ? 1 : pagenum

          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    handleSizeChange(pageSize) {
      console.log(`每页 ${pageSize} 条`);
      this.pageSize=pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(`当前页: ${pageNum}`);
      this.pageNum=pageNum
      this.load()
    },

    handleFileUploadSuccess(res){
      console.log(res)
      this.load()
    },
    download(url){
      window.open(url)
    },
  },
}
</script>

<style scoped>

</style>