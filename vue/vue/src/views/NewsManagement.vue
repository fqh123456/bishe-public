<template>
  <div style="padding: 20px">
    <div>
      <el-input v-model="search" placeholder="请输入新闻标题的关键字进行查询" style="width:250px;margin-left: 50px" clearable size="mini"></el-input>
<!--      <el-select v-model="sex" placeholder="用户性别" size="mini" style="width: 140px;margin-left: 5px">-->
<!--        <el-option label="不进行分类" value='0'></el-option>-->
<!--        <el-option label="男性" value=1></el-option>-->
<!--        <el-option label="女性" value=2></el-option>-->
<!--        <el-option label="保密" value=3></el-option>-->
<!--      </el-select>-->
      <el-button @click="load" size="mini" style="margin-left: 10px">查询</el-button>
      <el-button @click="add" size="mini" style="margin-left: 500px" type="primary" >新增</el-button>
    </div>
    <!--数据展示-->
    <div style="margin-left: 50px;margin-top: 20px" v-loading="loading">
      <el-table :data="tableData" stripe border size="mini"ref="multipleTable" tooltip-effect="dark" @selection-change="handleSelectionChange">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column prop="id" label="ID" fixed width="40" sortable></el-table-column>
        <el-table-column prop="title" label="新闻标题" show-overflow-tooltip width="100"></el-table-column>
        <el-table-column prop="content" label="新闻内容" show-overflow-tooltip style="width: 50px">
          <template v-slot="scope">
            <el-button @click="preview(scope.row.content)">查看内容</el-button>
          </template>
        </el-table-column>

        <el-table-column prop="img" label="配图" style="width: 80px;height: 80px">
          <template v-slot="scope">
            <div style="display: flex;align-items: center">
              <el-image style="width: 150px;height: 150px" v-if="scope.row.img"
              :src="scope.row.img" ></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="新闻分类" style="width: auto"></el-table-column>
        <el-table-column prop="type" label="类型" style="width: auto"></el-table-column>
        <el-table-column prop="time" label="发布时间" style="width: auto"></el-table-column>
        <el-table-column prop="adminId" label="发布人ID" style="width: auto"></el-table-column>
        <el-table-column prop="readCount" label="阅读次数" style="width: auto"></el-table-column>
        <!--        <el-table-column prop="permission" label="权限" style="width: auto"></el-table-column>-->
        <el-table-column
            fixed="right"
            label="操作"
            width="110">
          <template slot-scope="scope">
            <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
            <el-popconfirm title="确认删除该用户？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button type="text" style="color:orangered;margin-left: 10px" size="mini" >删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <!--分页-->
      <div style="display: flex;margin-top: 5px">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="5"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>

      <el-popconfirm title="确认删除这些用户吗？" @confirm="DeleteAll()" >
        <template #reference>
          <el-button type="text" style="color:orangered;margin-left: 20px" size="mini"  >批量删除</el-button>
        </template>
      </el-popconfirm>

    </div>
    <!--新增弹窗-->
    <div >
      <el-dialog title="新闻信息" :visible.sync="dialogVisible" width="65%" :close-on-click-modal="false" >
        <el-form :model="form" label-width="120px" size="mini"  v-loading="loading2"  >
          <el-form-item prop="title" label="新闻标题" style="flex: 2" >
            <el-input v-model="form.title" style="width: 70%;" ></el-input>
          </el-form-item>

          <el-form-item  prop="category" label="新闻分类" >
            <el-input v-model="form.category" style="width: 70%;" placeholder="新闻分类"></el-input>
          </el-form-item>

          <el-form-item prop="img" label="配图" >
            <el-upload
                action="http://localhost:9090/files/upload"
                list-type="picture"
                :on-success="handleImgSuccess"
            >
              <el-button type="primary">上传</el-button>
<!--              <img v-if="form.img" :src="form.img" class="avatar">-->
<!--              <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
            </el-upload>
          </el-form-item>

          <el-form-item  prop="content" label="新闻内容" >
            <div id="editor"></div>
          </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false" size="mini">取 消</el-button>
    <el-button type="primary" @click="save" size="mini">确 定</el-button>
      </span>
      </el-dialog>


      <el-dialog title="新闻内容" :visible.sync="formVisible1" width="50%" :close-on-click-modal="false" destroy-on-close>
        <el-card>
          <div class="w-e-text">
            <div v-html="content"></div>
          </div>
        </el-card>

        <div slot="footer" class="dialog-footer">
          <el-button @click="formVisible1=false">关闭</el-button>
        </div>
      </el-dialog>
    </div>



<!--    <div>-->
<!--      <el-form label-width="100px" size="small">-->
<!--        <el-upload-->
<!--            class="img-uploader"-->
<!--            action="http://localhost:9090/files/upload"-->
<!--            :show-file-list="false"-->
<!--            :on-success="handleImgSuccess"-->
<!--        >-->
<!--          <img v-if="form.img" :src="form.img" class="avatar">-->
<!--          <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--        </el-upload>-->
<!--      </el-form>-->
<!--    </div>-->
  </div>
</template>

<script>
import E from "wangeditor"
import request from "@/utils/request";
export default {
  name: "NewsManagement",
  data() {
    return {
      form: {},
      sex:"0",
      dialogVisible: false,
      search: '',
      currentPage: 1,//默认显示第一页
      pageSize: 5,
      total: 0,
      tableData: [],
      accountflag:0,
      codes:0,
      conflag:0,
      loading:false,
      loading2:false,
      multipleSelection:[],
      editor:null,
      content:'',
      formVisible1:false,
    }

  },
  //页面加载调用load()的方法
  created() {
    this.load()
  },
  methods: {
    preview(content){
      this.content=content
      this.formVisible1=true
    },
    //查询方法
    load() {
      this.searchNews()
    },
    searchNews(){//查询与显示（所有新闻）
      this.loading=true
      request.get("/news/search", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
        }
      }).then(res=> {
        this.tableData=res.data.records
        console.log(res.data.records)
        // for(let i=0;i<this.tableData.length;i++){
        //   if(this.tableData[i].sex==1){
        //     this.tableData[i].sex="男"
        //   }
        //   else if(this.tableData[i].sex==2){
        //     this.tableData[i].sex="女"
        //   }
        //   else if(this.tableData[i].sex==3){
        //     this.tableData[i].sex="保密"
        //   }
        // }
        this.total=res.data.total
        this.loading=false
      })
    },
    DeleteAll(){
      let deleteitem=[]
      for(let i=0;i<this.multipleSelection.length;i++){
        deleteitem[i]=this.multipleSelection[i].id
      }
      if(deleteitem.length===0){
        this.$message('请选择要删除的新闻！')
      }else{
        request.post("/news/deleteByBatch",deleteitem).then(res=>{
          if(res.code==='200'){
            this.$message({
              type:"success",
              message:'批量删除成功'
            })
            this.load()
          }else{
            this.$message({
              type:"error",
              message:'批量删除失败'
            })
          }
        })
      }
    },
    //打开新增弹窗
    add() {
      this.form={}
      this.setRichText('')
      this.dialogVisible=true
      setTimeout(()=>{
        this.editor.txt.html('')
      })
    },
    save() {
      this.form.content=this.editor.txt.html()
      request.post("/news/add",this.form).then(res=>{
        this.dialogVisible=false
        console.log(this.form.img)
        if(res.code=='200'){
          this.$message({
            type: "success",
            message: "保存成功！"
          })
          this.load()
        }else{
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.setRichText()
      setTimeout(()=>{
        this.editor.txt.html(row.content)
      })
    },
    handleDelete(id) {
      request.delete("/news/"+id).then(res=> {
        if(res.code === '200') {
          this.$message({
            type: "success",
            message: "删除成功！"
          })
        }else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        if(this.tableData.length==0||this.tableData.length==1){
          this.currentPage--;
        }
        this.load()//删除之后重新加载表格的数据

      })
    },
    handleSizeChange(pageSize) {//改变当前每页的个数触发
      this.pageSize=pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {//改变当前页面触发
      this.currentPage=pageNum
      this.load()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleImgSuccess(res){
      this.form.img=res
    },
    // handleImgSuccess(response,file,fileList){
    //   this.form.img=response.data
    // }
    setRichText(html){
      this.$nextTick(()=>{
        this.editor=new E('#editor')
        this.editor.config.uploadImgServer='http://localhost:9090/files/editor/upload'
        this.editor.config.uploadFileName='file'
        this.editor.config.uploadImgParams={
          type:'img',
        }
        this.editor.config.uploadVideoServer='http://localhost:9090/files/editor/upload'
        this.editor.config.uploadVideoName='file'
        this.editor.config.uploadVideoParams={
          type:'video',
        }
        this.editor.config.zIndex=0
        this.editor.create()
        this.editor.txt.html(html)
      })
    }

  }
}
</script>
<style>
.img-uploader{
  text-align: center
}
.img-uploader .el-upload{
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader-icon{
  width: 150px;
  height: 150px;
  text-align: center;
}
.avatar{
  width: 150px;
  height: 150px;
  display: block;
}
</style>