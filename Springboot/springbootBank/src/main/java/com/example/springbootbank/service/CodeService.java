
package com.example.springbootbank.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootbank.entity.Code;
import com.example.springbootbank.entity.User;
import com.example.springbootbank.mapper.CodeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class CodeService {
    @Resource
    @Autowired
    public CodeMapper codeMapper;
    public String codes="";
    public String createCode(){
        this.codes="";
        Random random=new Random();
        for(int i=0;i<6;i++){
            int number=random.nextInt(9);
            codes=codes+number;
        }
        return codes;
    }
}
