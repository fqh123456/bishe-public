package com.example.springbootbank.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.User;
import com.example.springbootbank.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/User")
public class UserController {
    @Resource

    @Autowired
    private UserMapper userMapper;


    @PostMapping ("/login")
    public Result login(@RequestBody Map map){
        String account=(String)map.get("account") ;
        String password=(String) map.get("password") ;
        User user=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getAccount,account));
        if(user!=null){
            if(password.equals(String.valueOf(user.getPassword())) ){

                return Result.success(user);
            }
            return Result.error("500","密码错误！");
        }
        return Result.error("404","账户不存在！");
    }

    @PostMapping ("/setLoginTime")
    public Result setLoginTime(@RequestBody Map map){
        Integer id=(Integer)map.get("id") ;
        User user=userMapper.selectById(id);
        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now=LocalDateTime.now();
        String time=df.format(now);
        now=LocalDateTime.parse(time,df);
        user.setLasttime(now);
        userMapper.updateById(user);
       return Result.success();
    }

    @PostMapping ("/changePassword")
    public Result changePassword(@RequestBody Map map){
        Integer uid=(Integer)map.get("uid") ;
        Integer password= Integer.valueOf( (String)map.get("newpassword"));
        System.out.println("uid:"+uid+"  password:"+password);
        User user=userMapper.selectById(uid);
        user.setPassword(password);
        Integer flage=userMapper.updateById(user);
        if(flage==1){
            return Result.success();
        }
        return Result.error("500","密码更新失败！");
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name",name);
//        Page<User> page = new Page<>(pageNum, pageSize);
        return Result.success(userMapper.selectPage(new Page<>(pageNum, pageSize),queryWrapper));
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(userMapper.deleteById(id));
    }

    @PostMapping
    public Result save(@RequestBody User user){
        return Result.success(userMapper.insert(user));
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return Result.success(userMapper.updateById(user));
    }


}
