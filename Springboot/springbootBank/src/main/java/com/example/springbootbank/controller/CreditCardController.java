package com.example.springbootbank.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.Code;
import com.example.springbootbank.entity.CreditCard;
import com.example.springbootbank.entity.Debt;
import com.example.springbootbank.entity.User;
import com.example.springbootbank.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/CreditCard")
public class CreditCardController {
    @Resource
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private BankCardMapper bankCardMapper;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    UserProductMapper userProductMapper;

    @Autowired
    CreditCardMapper creditCardMapper;

    @PostMapping("/getBills")//获取当前信用卡的每月账单
    public Result setCode(@RequestBody Map map){
        Integer cid=(Integer) map.get("cid");
        CreditCard creditCard=creditCardMapper.selectOne(Wrappers.<CreditCard>lambdaQuery().eq(CreditCard::getCid,cid));
        List<Debt> debts= JSONArray.parseArray(creditCard.getDebt(),Debt.class);
        List<Map>maps=new ArrayList<>();
        for(int i=0;i<debts.size();i++){
            Map map1=new HashMap<>();
            List<String> stringList=(debts.get(i).getBills()).stream().map(Object::toString).collect(Collectors.toList());
            debts.get(i).setBills(null);
            map1.put("id",String.valueOf(debts.get(i).getId()));
            map1.put("debt",debts.get(i));
            map1.put("bills",stringList);

            maps.add(map1);
        }
        return Result.success(maps);
    }
    @PostMapping("/getBill")//获取当前信用卡的每月账单
    public Result getBill(@RequestBody Map map){
        Integer cid=(Integer) map.get("cid");
        String id=(String) map.get("id");
        CreditCard creditCard=creditCardMapper.selectOne(Wrappers.<CreditCard>lambdaQuery().eq(CreditCard::getCid,cid));
        if(creditCard!=null){
            List<Debt> debts= JSONArray.parseArray(creditCard.getDebt(),Debt.class);
            for(int i=0;i<debts.size();i++){
                if(id.equals(String.valueOf(debts.get(i).getId()))){
                    Map map1=new HashMap<>();
                    List<String> stringList=(debts.get(i).getBills()).stream().map(Object::toString).collect(Collectors.toList());
                    debts.get(i).setBills(null);
                    map1.put("id",String.valueOf(debts.get(i).getId()));
                    map1.put("debt",debts.get(i));
                    map1.put("bills",stringList);
                    return Result.success(map1);
                }
            }
        }
        return Result.error("404","该信用卡不存在");

    }
}
