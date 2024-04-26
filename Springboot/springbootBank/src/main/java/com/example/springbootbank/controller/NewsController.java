package com.example.springbootbank.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.News;
import com.example.springbootbank.mapper.NewsMapper;
import com.example.springbootbank.service.INewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    private INewsService newsService;

    @Resource
    private NewsMapper newsMapper;

//    private Result returnObj=null;

    @PostMapping("/add")
    public Result save(@RequestBody News news){
        System.out.println("new/add/接口运行");
        System.out.println(news.getImg());
        news.setTime(DateUtil.now());
//        boolean flag=false;
//        Integer flag=0;
//        if(id==null){
//            flag=newsMapper.insert(news);
//        }else{
//            flag=newsMapper.updateById(news);
//        }
        boolean flag=newsService.saveOrUpdate(news);

        if(flag==true){
            System.out.println("new/add/接口成功运行结束");
            return Result.success();
        }
        System.out.println("new/add/接口异常运行结束");
        return Result.error();
//        returnObj=new Result("",newsService.saveOrUpdate(news),"200");
//        return returnObj;
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        boolean flag = newsService.removeById(id);
        if(flag==true){
            return Result.success();
        }
        return Result.error();
//        return returnObj;
    }

    @PostMapping("/deleteByBatch")
    public Result deleteByIds(@RequestBody List<Integer> ids){
        System.out.println(ids);
        Integer flag=newsMapper.deleteBatchIds(ids);
        System.out.println("flag="+flag);
        if(flag!=0){
            return Result.success();
        }
        return Result.error();
    }

    @GetMapping("/search")//搜索用户
    public Result findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search
                              ) {

        LambdaQueryWrapper<News> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(News::getTitle, search);
        }
        Page<News> userPage = newsMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
//        returnObj=new ReturnObj("",userPage,"200");
//        return returnObj;
    }
}
