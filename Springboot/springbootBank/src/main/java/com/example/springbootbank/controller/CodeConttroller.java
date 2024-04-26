package com.example.springbootbank.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.Code;
import com.example.springbootbank.entity.User;
import com.example.springbootbank.mapper.CodeMapper;
import com.example.springbootbank.mapper.UserMapper;
import com.example.springbootbank.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/Code")
public class CodeConttroller {
    @Resource

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private UserMapper userMapper;

    public CodeService codeService=new CodeService();

    @PostMapping("/setCode")
    public Result setCode(@RequestBody Map map){

        Integer uid=(Integer) map.get("uid");
        String phone=(String) map.get("phone");
        User user=userMapper.selectById(uid);
        if(!user.getPhone().equals(phone)){
            return Result.error("404","错误！请使用绑定的电话号码进行验证");
        }
        Code code=codeMapper.selectById(uid);
        String codenumber=codeService.createCode();
        code.setPhonecode(codenumber);
        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now=LocalDateTime.now();
        String time=df.format(now);
        now=LocalDateTime.parse(time,df);
        code.setGettime(now);

        codeMapper.updateById(code);
        return Result.success();
    }

    @PostMapping("/checkCode")
    public Result checkCode(@RequestBody Map map){
        Integer uid=(Integer) map.get("uid");
        String phoneCode=(String) map.get("phoneCode");
        Code code=codeMapper.selectOne(Wrappers.<Code>lambdaQuery().eq(Code::getUid,uid).eq(Code::getPhonecode,phoneCode));
        if(code==null){
            return Result.error("404","验证码错误！");
        }
        if(code.getGettime()!=null){
            Duration duration=Duration.between(code.getGettime(),LocalDateTime.now());
            float min=duration.toMillis()/1000;
            if(min>300){
                return Result.error("300","验证码失效！");
            }
        }
        return Result.success();
    }
}
