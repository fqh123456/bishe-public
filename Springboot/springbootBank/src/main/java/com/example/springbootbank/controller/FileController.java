package com.example.springbootbank.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.Files;
import com.example.springbootbank.mapper.FilesMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUpLoadPath;

    @Resource
    private FilesMapper filesMapper;

    private static final String ROOT_PATH=System.getProperty("user.dir")+File.separator+"files";

//    @Value("${server.port}")
//    private String port;

//    @Value("${ip:localhost}")
//    private String ip;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
//        String originalFilename=file.getOriginalFilename();
//        String mainName=FileUtil.mainName(originalFilename);
//        String extName=FileUtil.extName(originalFilename);
//        if(!FileUtil.exist(ROOT_PATH)){
//            FileUtil.mkdir(ROOT_PATH);
//        }
//        if(FileUtil.exist(ROOT_PATH+File.separator+originalFilename)){
//            originalFilename=System.currentTimeMillis()+"_"+mainName+"."+extName;
//        }
//        File saveFile=new File(ROOT_PATH+File.separator+originalFilename);
//        file.transferTo(saveFile);
//        String url="http://localhost:9090/files/download/"+originalFilename;
//        return Result.success(url);


        String originalFilename= file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        //判断文件路径是否存在，若不存在新建一个目录

        String uuid= IdUtil.fastSimpleUUID();
        String fileUUid=uuid+ StrUtil.DOT+type;
        File uploadFile=new File(fileUpLoadPath+fileUUid);
        File parentFile=uploadFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }


        String md5;
        String url;
        file.transferTo(uploadFile);
        md5= SecureUtil.md5(uploadFile);
        Files dbfiles=getFileByMd5(md5);
        if(dbfiles!=null){
            url=dbfiles.getUrl();
            uploadFile.delete();
        }else{
            url="http://localhost:9090/files/download/"+fileUUid;
        }
        Files saveFile=new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        filesMapper.insert(saveFile);
        return url;
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {

        response.addHeader("Content-Disposition","inline;filename="+ URLEncoder.encode(fileName,"UTF-8"));
        String filePath=ROOT_PATH+File.separator+fileName;
        if(!FileUtil.exist(filePath)){
            return ;
        }
        byte[] bytes=FileUtil.readBytes(filePath);
        ServletOutputStream outputStream=response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    public Files getFileByMd5(String md5){
        QueryWrapper<Files> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> filesList=filesMapper.selectList(queryWrapper);
        return filesList.size()==0 ? null:filesList.get(0);
    }

    @PostMapping("/editor/upload")
    public Dict editorUpload(MultipartFile file) throws IOException {

        String originalFilename=file.getOriginalFilename();
        String mainName=FileUtil.mainName(originalFilename);
        String extName=FileUtil.extName(originalFilename);
        if(!FileUtil.exist(ROOT_PATH)){
            FileUtil.mkdir(ROOT_PATH);
        }
        if(FileUtil.exist(ROOT_PATH+File.separator+originalFilename)){
            originalFilename=System.currentTimeMillis()+"_"+mainName+"."+extName;
        }
        File saveFile=new File(ROOT_PATH+File.separator+originalFilename);
        file.transferTo(saveFile);
        String url="http://localhost:9090/files/download/"+originalFilename;
        return Dict.create().set("errno",0).set("data",CollUtil.newArrayList(Dict.create().set("url",url)));

//        System.out.println("接口开始运行");
//        String flag;
//        synchronized (FileController.class){
//            flag=System.currentTimeMillis()+"";
//            ThreadUtil.sleep(1L);
//        }
//        String fileName=file.getOriginalFilename();
//        try{
//            if(!FileUtil.isDirectory(filePath)){
//                FileUtil.mkdir(filePath);
//            }
//            FileUtil.writeBytes(file.getBytes(),filePath+flag+"-"+fileName);
//            System.out.println(fileName+"--上传成功");
//        } catch (IOException e) {
//            System.err.println(fileName+"--文件上传失败");;
//        }
//        String http="http://"+ip+":"+port+"/files/";
//        System.out.println("接口结束运行");
//        return Dict.create().set("errno",0).set("data", CollUtil.newArrayList(Dict.create().set("url",http+flag+"-"+fileName)));
    }

}
