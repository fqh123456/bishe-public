
package com.example.springbootbank.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootbank.common.IdGeneratorSnowlake;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.*;
import com.example.springbootbank.mapper.BankCardMapper;
import com.example.springbootbank.mapper.CreditCardMapper;
import com.example.springbootbank.mapper.ProductMapper;
import com.example.springbootbank.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Pay")
public class PayCostController {
    @Resource

    @Autowired
    BankCardMapper bankCardMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CreditCardMapper creditCardMapper;

    @PostMapping("/out")//取钱
    public Result out(@RequestBody Map map){
        Integer id=(Integer) map.get("cardid") ;
        float cost=Float.valueOf((String)map.get("cost"));
        BankCard bankCard=bankCardMapper.selectById(id);
        if(bankCard!=null){
            Detail detail=new Detail();
            //
            detail.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
            detail.setCost(cost);
            detail.setType("取出");
            detail.setPlace("银行");
            DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now=LocalDateTime.now();
            String time=df.format(now);
            now=LocalDateTime.parse(time,df);
            detail.setPaytime(now);
            //
            List<Detail> details= JSONArray.parseArray(bankCard.getDetail(),Detail.class);
            details.add(detail);
            String dd=JSONArray.toJSONString(details);
            bankCard.setDetail(dd);
            bankCard.setBalance((bankCard.getBalance()-cost));
            if(bankCardMapper.updateById(bankCard)==1){
                return Result.success();
            }
            return Result.error("500","错误！取钱失败");
        }
        return Result.error("404","错误，获取银行卡失败！请联系客服");
    }

    @PostMapping("/save")//取钱
    public Result save(@RequestBody Map map){
        Integer id=(Integer) map.get("cardid") ;
        float cost=Float.valueOf((String)map.get("cost"));
        BankCard bankCard=bankCardMapper.selectById(id);
        if(bankCard!=null){
            Detail detail=new Detail();
            //
            detail.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
            detail.setCost(cost);
            detail.setType("存入");
            detail.setPlace("银行");
            DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now=LocalDateTime.now();
            String time=df.format(now);
            now=LocalDateTime.parse(time,df);
            detail.setPaytime(now);
            //
            List<Detail> details= JSONArray.parseArray(bankCard.getDetail(),Detail.class);
            details.add(detail);
            String dd=JSONArray.toJSONString(details);
            bankCard.setDetail(dd);
            bankCard.setBalance((bankCard.getBalance()+cost));
            if(bankCardMapper.updateById(bankCard)==1){
                return Result.success();
            }
            return Result.error("500","错误！取钱失败");
        }
        return Result.error("404","错误，获取银行卡失败！请联系客服");
    }

    @PostMapping("/transfer")//转账
    public Result transfer(@RequestBody Map map){
        String cardnumber=(String)map.get("paycard");//接收方账号
        String name=(String)map.get("payname");//接收方姓名
        BankCard bankCard=bankCardMapper.selectOne(Wrappers.<BankCard>lambdaQuery().eq(BankCard::getCardnumber,cardnumber));
        if(bankCard!=null){
            User user=userMapper.selectById(bankCard.getUid());
            if(user.getName().equals(name)){
                Integer id=(Integer) map.get("cardid") ;//银行卡id
                float cost=Float.valueOf((String)map.get("cost"));//金额
                String describe=(String)map.get("describe") ;//用途
                if(maketans(id,bankCard.getId(),cost,describe,1,0)==1){
                    return Result.success();
                }
                return Result.error("500","错误！转账过程中服务器出错，请联系客服");
            }
            return Result.error("500","错误！接收方姓名错误，请重新填写");
        }
        return Result.error("404","错误！接收方账户不存在，请重新填写");
    }

    @PostMapping("/consume")//消费
    public Result consume(@RequestBody Map map){
        Integer cid=(Integer) map.get("cid");
        float cost=Float.valueOf((String)map.get("cost"));//金额
        String describe=(String) map.get("describe");
        BankCard bankCard=bankCardMapper.selectById(cid);
        if(bankCard!=null){//账户存在
            if(cost>bankCard.getBalance()){
                return Result.error("500","余额不足！支付失败");
            }
            else{
                if(Consume(bankCard,cost,describe)==1){
                    return Result.success();
                }
                return Result.error("500","操作失败！");
            }
        }
        return Result.error("404","错误！账户不存在，请重新选择");
    }

    @PostMapping("/returnCredit")//还款银行卡
    public Result returnCredit(@RequestBody Map map){
        Integer pcid=(Integer) map.get("pcid");//支付账号
        Integer ccid=(Integer) map.get("ccid");//信用卡账号
        String id=(String) map.get("id");//账单id
        float cost=Float.valueOf(String.valueOf (map.get("cost")));//金额
        float reallcost=cost;
        BankCard pbankCard=bankCardMapper.selectById(pcid);
        if(cost<=pbankCard.getBalance()){//金额足够还款
            CreditCard creditCard=creditCardMapper.selectOne(Wrappers.<CreditCard>lambdaQuery().eq(CreditCard::getCid,ccid));
            List<Debt> debts= JSONArray.parseArray(creditCard.getDebt(),Debt.class);
            //先进行账单操作
            for(int i=0;i<debts.size();i++){//遍历账单
                if(id.equals(String.valueOf(debts.get(i).getId()))){//找到了对应账单
                    debts.get(i).setReturnmoney(debts.get(i).getReturnmoney()+cost);//先设置还款金额
                    if(debts.get(i).getInterest()<cost){//还款额度大于利息
                        cost=cost-debts.get(i).getInterest();//还款金额减去利息，剩下的还本金
                        debts.get(i).setInterest(0);//先还利息
                        if((debts.get(i).getNeedreturn()-cost)<=0.01){//后还本金
                            debts.get(i).setNeedreturn(0);
                        }
                        else {
                            debts.get(i).setNeedreturn((debts.get(i).getNeedreturn()-cost));
                        }
                    }
                    else {//还款额度小于等于利息
                        debts.get(i).setInterest((debts.get(i).getInterest()-cost));
                        cost=0;//设置还本金的还款金额为0
                    }
                    break;
                }
            }
            if(maketans(pcid,ccid,reallcost,"",2,cost)==1){
                creditCard.setDebt(JSONArray.toJSONString(debts));//设置账单信息
                if(creditCardMapper.updateById(creditCard)==1){
                    return Result.success();
                }
                return Result.error("500","还款失败");
            }
            //对信用卡的账单进行操作
            return Result.error("404","错误！账户不存在，请重新选择");
        }
        return Result.error("500","错误！当前账户余额不足");
    }
    //信用卡还款操作
    //转账操作
    public int maketans(int payid,int payeeid,float cost,String describe,int type,float reallcost){//转账方id，收款方id,转账金额,转账用途
        BankCard bankCardpay=bankCardMapper.selectById(payid);//付款方
        BankCard bankCardpayee=bankCardMapper.selectById(payeeid);//收款方
        Detail detailpay=new Detail();//转账方细节
        Detail detailpayee=new Detail();//收款方细节
        List<Detail> detailspay= JSONArray.parseArray(bankCardpay.getDetail(),Detail.class);
        List<Detail> detailspayee= JSONArray.parseArray(bankCardpayee.getDetail(),Detail.class);
        //设置订单编号
        detailpay.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
        detailpayee.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
        //设置对方账户
        detailpay.setReciaccount(bankCardpayee.getCardnumber());
        detailpayee.setReciaccount(bankCardpay.getCardnumber());
        //设置交易时间
        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now=LocalDateTime.now();
        String time=df.format(now);
        now=LocalDateTime.parse(time,df);
        detailpay.setPaytime(now);
        detailpayee.setPaytime(now);
        //设置金额
        detailpayee.setCost(cost);
        detailpay.setCost(cost);
        bankCardpay.setBalance((bankCardpay.getBalance()-cost));
        if(type==2){
            bankCardpayee.setBalance((bankCardpayee.getBalance()+reallcost));
        } else if (type==1) {
            bankCardpayee.setBalance((bankCardpayee.getBalance()+cost));
        }
        //设置转账类型

        //设置交易地点
        if(type==1){//转账
            detailpay.setType("转账转出");
            detailpayee.setType("转账接收");
        }
        else if(type==2){//还款银行卡
            detailpay.setType("还款转出");
            detailpayee.setType("还款接收");
        }
        detailpay.setPlace("网上银行");
        detailpayee.setPlace("网上银行");
        //设置用途
        detailpay.setDescribe(describe);
        detailpayee.setDescribe(describe);
        //设置完成
        detailspay.add(detailpay);
        detailspayee.add(detailpayee);
        String Strdetailspay=JSONArray.toJSONString(detailspay);
        String Strdetailspayee=JSONArray.toJSONString(detailspayee);
        bankCardpay.setDetail(Strdetailspay);
        bankCardpayee.setDetail(Strdetailspayee);
        //
        if(bankCardMapper.updateById(bankCardpayee)==1&&bankCardMapper.updateById(bankCardpay)==1){
            return 1;
        }
        return 0;
    }
    //消费操作
    public int Consume(BankCard bankCard,float cost,String describe){
            //通用操作
        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now=LocalDateTime.now();
        String time=df.format(now);
        now=LocalDateTime.parse(time,df);
        List<Detail> details= JSONArray.parseArray(bankCard.getDetail(),Detail.class);
        Detail detail=new Detail();
        detail.setPaytime(now);
        detail.setCost(cost);
        detail.setPlace("网上银行");
        detail.setType("消费");
        detail.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
        detail.setDescribe(describe);
        details.add(detail);
        bankCard.setBalance((bankCard.getBalance()-cost));
        bankCard.setDetail(JSONArray.toJSONString(details));
        if(bankCardMapper.updateById(bankCard)==1){
            return 1;
        }
        return 0;
    }

    //购买商品操作(设置银行卡信息和账单信息)
    public int buyproductandsetcard(int uid, UserProductDetails userProductDetails){
        BankCard bankCard=bankCardMapper.selectById(userProductDetails.getCardid());
        if(bankCard!=null){
            List<Detail> details= JSONArray.parseArray(bankCard.getDetail(),Detail.class);
            bankCard.setBalance((bankCard.getBalance()-userProductDetails.getCost()));//设置当前余额
            Detail detail=new Detail();
            detail.setId(userProductDetails.getId());
            detail.setPaytime(userProductDetails.getPaytime());
            detail.setCost(userProductDetails.getCost());
            detail.setPlace("网上银行");
            detail.setType("理财");
            String describe="用于购买理财产品，产品名称：";
            Product product=productMapper.selectById(userProductDetails.getProductid());
            describe=describe+product.getName();
            detail.setDescribe(describe);
            details.add(detail);
            bankCard.setDetail(details.toString());
            bankCardMapper.updateById(bankCard);
            return 1;
        }
        return 0;
    }

}
