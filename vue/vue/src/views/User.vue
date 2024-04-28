<template>
  <div>
    <div>
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-upload action="http://localhost:9090/user/import"
                 style="display: inline-block"
                 :show-file-list="false"
                 :on-success="handleExcelImportSuccess"
      >
        <el-button type="primary" >导入</el-button>
      </el-upload>
      <el-button type="primary" @click="exp">导出</el-button>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="id"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="account" label="账号"></el-table-column>
        <el-table-column prop="age" label="年龄"></el-table-column>
        <el-table-column prop="job" label="工作"></el-table-column>
        <el-table-column prop="marital" label="婚姻状态"></el-table-column>
        <el-table-column prop="education" label="学历"></el-table-column>
        <el-table-column prop="phone" label="联系电话"></el-table-column>

        <el-table-column label="操作" width="200" align="center">

          <template slot-scope="scope">
            <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i> </el-button>
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
    </div>

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

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="form.account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="工作">
          <el-input v-model="form.job" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="婚姻状态">
          <el-select clearable v-model="form.marital" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in maritals" :key="item" :label="item" :value="item">
              <i :class="item" /> {{ item }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学历">
          <el-select clearable v-model="form.education" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in educations" :key="item" :label="item" :value="item">
              <i :class="item" /> {{ item }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog title="用户信息" :visible.sync="dialogFormVisible2" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="form.account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="工作">
          <el-input v-model="form.job" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="婚姻状态">
          <el-select clearable v-model="form.marital" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in maritals" :key="item" :label="item" :value="item">
              <i :class="item" /> {{ item }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学历">
          <el-select clearable v-model="form.education" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in educations" :key="item" :label="item" :value="item">
              <i :class="item" /> {{ item }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="update">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "User",
  data(){
    return{
      tableData:[],
      total:0,
      pageNum:1,
      pageSize:5,
      dialogFormVisible:false,
      dialogFormVisible2:false,
      form:{},
      roles:[],
      maritals:["单身","已婚","未婚","离异"],
      educations:["专科","本科","硕士","博士"]
    }
  },
  created() {
    //请求分页查询
    this.load()
  },
  methods:{
    load(){
      this.request.get("/User/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data.records
        this.total=res.data.total
      })

      this.request.get("/role").then(res=>{
        this.roles=res.data
      })

    },
    handleAdd(){
      this.dialogFormVisible=true
      this.form={}
    },
    handleEdit(row){
      this.form= Object.assign({},row)
      this.dialogFormVisible2=true
    },
    del(id){
      this.request.delete("/User/"+id).then(res =>{
        if (res.code === '200') {
          this.$message.success("删除成功")
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
    save(){
      this.request.post("/User",this.form).then(res=>{
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible=false
        }else{
          this.$message.error("保存失败")
        }
        this.load()
      })
    },
    update(){
      this.request.post("/User/update",this.form).then(res=>{
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible2=false
        }else{
          this.$message.error("保存失败")
        }
        this.load()
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
    exp(){
      window.open("http://localhost:9090/user/export")
    },
    handleExcelImportSuccess(){
      this.$message.success("文件上传成功")
      this.load()
    }
  },
}
</script>

<style scoped>

</style>