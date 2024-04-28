package com.example.springbootbank.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootbank.common.IdGeneratorSnowlake;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.*;
import com.example.springbootbank.mapper.BankCardMapper;
import com.example.springbootbank.mapper.EnterpriseMapper;
import com.example.springbootbank.mapper.ProductMapper;
import com.example.springbootbank.mapper.UserProductMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/UserProduct")
public class UserProductController {
    @Resource

    @Autowired
    BankCardMapper bankCardMapper;

    @Autowired
    ProductMapper productMapper;
    @Autowired
    UserProductMapper userproductMapper;

    @Autowired
    EnterpriseMapper enterpriseMapper;
    @PostMapping("/getUserProduct")//获取该用户该产品详情(获取一个)
    public Result getUserProduct(@RequestBody Map map){
        int uid=(Integer)map.get("uid");
        int pid=(Integer)map.get("pid");
        UserProduct userProduct=userproductMapper.selectOne(Wrappers.<UserProduct>lambdaQuery().eq(UserProduct::getUid,uid));
        if(userProduct!=null){//有数据
            List<UserProductDetails> details= JSONArray.parseArray(userProduct.getProduct(),UserProductDetails.class);
            for(int i=0;i<details.size();i++){
                if(details.get(i).getProductid()==pid&&details.get(i).getState()==1){//正在进行中的该产品
                    return Result.success(details.get(i));
                }
            }
            return Result.error("404","");
        }
        return Result.error("404","该用户尚未购买任何理财产品");
    }

    @PostMapping("/getUserProducts")//获取该用户该产品(获取所有)
    public Result getUserProducts(@RequestBody Map map){
        int uid=(Integer)map.get("uid");
        UserProduct userProduct=userproductMapper.selectOne(Wrappers.<UserProduct>lambdaQuery().eq(UserProduct::getUid,uid));
        if(userProduct!=null){//有数据
            List<UserProductDetails> details= JSONArray.parseArray(userProduct.getProduct(),UserProductDetails.class);
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<details.size();i++){
                JSONObject mapDetails=new JSONObject();
                mapDetails= JSON.parseObject(JSON.toJSONString(details.get(i)));
                Product product=productMapper.selectById(details.get(i).getProductid());
                mapDetails.put("name",product.getName());
                mapDetails.put("rate",product.getRate());
                mapDetails.put("type",product.getType());
                mapDetails.put("minday",product.getMinday());
                LocalDate ltime=details.get(i).getPaytime().plusDays(product.getMinday()).toLocalDate();//最早能取出的期限
                LocalDate now=LocalDateTime.now().toLocalDate();//今天
                long days= ChronoUnit.DAYS.between(ltime,now);//时间差
                mapDetails.put("days",days);
                jsonArray.add(mapDetails);
            }
            return Result.success(jsonArray);
        }
        return Result.error("404","该用户尚未购买任何理财产品");
    }

    @PostMapping("/gethistoryrate")//查看该产品的历史利率
    public Result gethistoryrate(@RequestBody Map map){
        int pid=(Integer)map.get("pid");
        Product product=productMapper.selectById(pid);
        List<ProductRate> productRates= JSONArray.parseArray(product.getHistoricalrate(),ProductRate.class);
        if(productRates.size()>0){
            return  Result.success(productRates);
        }
        return Result.error("500","未查询到该产品历史利率");
    }

    @PostMapping("/addBuyUserProduct")//加购当前产品
    public Result addBuyUserProduct(@RequestBody Map map){
        int uid=(Integer)map.get("uid");
        int pid=(Integer)map.get("pid");
        UserProduct userProduct=userproductMapper.selectOne(Wrappers.<UserProduct>lambdaQuery().eq(UserProduct::getUid,uid));
        if(userProduct!=null){//有数据
            float add=Float.valueOf((String)map.get("add"));//金额
            List<UserProductDetails> details= JSONArray.parseArray(userProduct.getProduct(),UserProductDetails.class);
            for(int i=0;i<details.size();i++){
                if(details.get(i).getProductid()==pid&&details.get(i).getState()==1){//正在进行中的该产品
                    details.get(i).setCost(details.get(i).getCost()+add);
                    details.get(i).setBalance(details.get(i).getBalance()+add);
                    int cid=(Integer)map.get("cardid");
                    //银行卡与产品操作
                    if(addbuybankcaord(cid,add,pid)==1&&addbuyproduct(pid,add)==1){
                        userProduct.setProduct(JSONArray.toJSONString(details));
                        userproductMapper.updateById(userProduct);
                        return Result.success();
                    }
                    return Result.error("500","操作失败！");
                }
            }
            return Result.error("404","");
        }
        return Result.error("404","该用户尚未购买任何理财产品");
    }

    @PostMapping("/outProduct")//取出当前产品
    public Result outProduct(@RequestBody Map map){
        int uid=(Integer)map.get("uid");
        int pid=(Integer)map.get("pid");
        UserProduct userProduct=userproductMapper.selectOne(Wrappers.<UserProduct>lambdaQuery().eq(UserProduct::getUid,uid));
        if(userProduct!=null){
            List<UserProductDetails> details= JSONArray.parseArray(userProduct.getProduct(),UserProductDetails.class);
            for(int i=0;i<details.size();i++){
                if(details.get(i).getProductid()==pid&&details.get(i).getState()==1){
                    Product product=productMapper.selectById(pid);
                    LocalDate ltime=details.get(i).getPaytime().plusDays(product.getMinday()).toLocalDate();//最早能取出的期限
                    LocalDate now=LocalDateTime.now().toLocalDate();//今天
                    long days= ChronoUnit.DAYS.between(ltime,now);//时间差
                    System.out.println(days);
                    if(days>=0){//今天的日期能取出
                        details.get(i).setState(2);//设置为结束状态
                        //进行银行卡的操作
                        Integer cid=details.get(i).getCardid();//获取银行卡号
                        BankCard bankCard=bankCardMapper.selectById(cid);
                        bankCard.setBalance(bankCard.getBalance()+details.get(i).getBalance());//调整金额
                        //开始设置银行卡交易信息
                        List<Detail> carddetails= JSONArray.parseArray(bankCard.getDetail(),Detail.class);
                        Detail detail=new Detail();
                        detail.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
                        detail.setType("理财");
                        detail.setCost(details.get(i).getBalance());
                        detail.setDescribe("回收理财产品"+product.getName()+"的剩余金额");
                        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime nowtime=LocalDateTime.now();
                        String time=df.format(nowtime);
                        nowtime=LocalDateTime.parse(time,df);
                        detail.setPaytime(nowtime);
                        detail.setPlace("网上银行");
                        carddetails.add(detail);
                        //产品的进度更改
                        product.setSum(product.getSum()-details.get(i).getCost());
                        bankCard.setDetail(JSONArray.toJSONString(carddetails));
                        userProduct.setProduct(JSONArray.toJSONString(details));
                        productMapper.updateById(product);
                        bankCardMapper.updateById(bankCard);
                        userproductMapper.updateById(userProduct);
                        return Result.success();
                    }
                    return Result.error("500","当前产品未到最早取出期限！详情请查看产品最少持有时间或固定存入期限");
                }
            }
            return Result.error("500","未查询到用户购买该产品的相关数据！");
        }
        return Result.error("500","操作失败！");
    }
    public int addbuybankcaord(int cardid,float cost,int pid){//加购的银行卡操作
        BankCard bankCard=bankCardMapper.selectById(cardid);
        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now=LocalDateTime.now();
        String time=df.format(now);
        now=LocalDateTime.parse(time,df);
        bankCard.setBalance(bankCard.getBalance()-cost);
        List<Detail> details= JSONArray.parseArray(bankCard.getDetail(),Detail.class);
        Detail detail=new Detail();
        //
        detail.setPaytime(now);
        detail.setType("理财");
        detail.setCost(cost);
        detail.setPlace("网上银行");
        Product product=productMapper.selectById(pid);
        detail.setDescribe("用于加购理财产品"+product.getName());
        detail.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
        details.add(detail);
        String d=JSONArray.toJSONString(details);
        bankCard.setDetail(d);
        if(bankCardMapper.updateById(bankCard)==1){
            return 1;
        }
        return 0;
    }
    public int addbuyproduct(int pid,float cost){//加购的产品操作
        Product product=productMapper.selectById(pid);
        product.setSum(product.getSum()+cost);
        if(productMapper.updateById(product)==1){
            return 1;
        }
        return 0;
    }
}
@Data
class ProductRate{
    float rate;
    LocalDateTime time;
}