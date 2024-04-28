
package com.example.springbootbank.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootbank.common.CreateBankCards;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.BankCard;
import com.example.springbootbank.entity.CreditCard;
import com.example.springbootbank.entity.Detail;
import com.example.springbootbank.entity.User;
import com.example.springbootbank.mapper.BankCardMapper;
import com.example.springbootbank.mapper.CreditCardMapper;
import com.example.springbootbank.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/BankCard")
public class BankCardController {
    @Resource

    @Autowired
    BankCardMapper bankCardMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    CreditCardMapper creditCardMapper;

    //创建银行卡
    @PostMapping("/creatBankcards")
    public Result creatBankcards(@RequestBody Map map){
        System.out.println(map);
        Integer type=(Integer) map.get("type");
        Integer uid=(Integer) map.get("uid");
        String cardId="";
        while (true){
            CreateBankCards createBankCards=new CreateBankCards();
            cardId=createBankCards.creatBankcards(type);
            BankCard bankCard=bankCardMapper.selectOne(Wrappers.<BankCard>lambdaQuery().eq(BankCard::getCardnumber,cardId));
            if(bankCard==null){
                break;
            }
        }
        BankCard bankCard=new BankCard();
        bankCard.setUid(uid);
        bankCard.setType(type);
        bankCard.setCardnumber(cardId);
        bankCard.setDetail("[]");
        bankCardMapper.insert(bankCard);
        User user=userMapper.selectById(uid);
        //信用卡办理
        if(type==3){
            CreditCard card=new CreditCard();
            card.setCid(bankCard.getId());
            card.setStages("[]");
            card.setDebt("[]");
            card.setLimits(1000);
            card.setOverdue((float) 0.05);
            card.setRate((float) 0.0005);
            creditCardMapper.insert(card);
            bankCard.setBalance(card.getLimits());
            bankCardMapper.updateById(bankCard);
        }

        //更新用户信息
        JSONArray jsonArray =new JSONArray();
        jsonArray=JSONArray.parseArray(user.getBankcards());
        Map maps=new HashMap<>();
        maps.put("id",bankCard.getId());
        jsonArray.add(maps);
        user.setBankcards(jsonArray.toJSONString());
        userMapper.updateById(user);
        //
        return Result.success(cardId);
    }

    @PostMapping("/getCardsDetailbyids")//使用i账单id来获取账单信息
    public Result getCardsDetailbyids(@RequestBody Map map){
        Integer cid=(Integer) map.get("cid");
        List<String> ids=(List<String>) map.get("ids");
        System.out.println(ids);
        List<Detail>lists=new ArrayList<>();
        BankCard bankCard=bankCardMapper.selectById(cid);
        if(bankCard!=null){
            List<Detail> details=JSONArray.parseArray(bankCard.getDetail(),Detail.class);
            System.out.println(details);
            for(int i=0;i<ids.size();i++){
                for(int j=0;j<details.size();j++){
                    if(ids.get(i).equals(String.valueOf(details.get(j).getId()))){
                        lists.add(details.get(j));
                        break;
                    }
                }
            }
            if(lists.size()>0){
                return Result.success(lists);
            }
            return Result.error("400","暂无消费记录");
        }
        return Result.error("404","不存在银行卡");
    }
    @PostMapping("/getCards")//获取用户所有银行卡
    public Result getCards(@RequestBody Map map){
        Integer uid=(Integer) map.get("uid");
        User user=userMapper.selectById(uid);
        JSONArray jsonArray=JSONArray.parseArray(user.getBankcards());
        List <BankCard>cards=new ArrayList<BankCard>();
        if(jsonArray!=null){
            QueryWrapper<BankCard> queryWrapper=new QueryWrapper<>();
            queryWrapper.select("id","type","balance","cardnumber");
            cards=bankCardMapper.selectList(queryWrapper.eq("uid",uid));
            return Result.success(cards);
        }
        return Result.error("404","不存在银行卡");
    }

    @PostMapping("/getCard")//获取单张银行卡
    public Result getCard(@RequestBody Map map){
        Integer id=(Integer) map.get("cardid");
        QueryWrapper<BankCard> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("id","type","balance","cardnumber");
        BankCard card=bankCardMapper.selectOne(queryWrapper.eq("id",id));
        if(card!=null){
            return Result.success(card);
        }
        return Result.error("404","不存在银行卡");
    }

    @PostMapping("/getCardsDetail")//获取银行卡的detail数组
    public Result getCardsDetail(@RequestBody Map map){
        Integer id=(Integer) map.get("cardsid");
        BankCard bankCard=bankCardMapper.selectById(id);
        if(bankCard!=null){
            List<Detail> details=JSONArray.parseArray(bankCard.getDetail(),Detail.class);
            List<Detail> list=new ArrayList<Detail>();
           if(details.size()>0){
               long begintime=(long) map.get("begintime");
               long endtime=(long) map.get("endtime")+86400000;
               for(int i=0;i<details.size();i++){
                   LocalDateTime time=details.get(i).getPaytime();
                   long timestamp=time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
                   if(timestamp>=begintime&&timestamp<=endtime){
                       list.add(details.get(i));
                   }
               }
               return Result.success(list);
           }
           else {
               return Result.error("300","暂无数据");
           }
        }
        return Result.error("404","不存在银行卡");
    }

    @PostMapping("/getCardsDetail2")
    public Result getCardsDetail2(@RequestBody Map map){
        Integer id=(Integer) map.get("cardsid");
        BankCard bankCard=bankCardMapper.selectById(id);
        bankCard.setDetail("[]");
        return Result.success(bankCard);
    }

}
