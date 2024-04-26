package com.example.springbootbank.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootbank.common.IdGeneratorSnowlake;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.*;
import com.example.springbootbank.mapper.BankCardMapper;
import com.example.springbootbank.mapper.EnterpriseMapper;
import com.example.springbootbank.mapper.ProductMapper;
import com.example.springbootbank.mapper.UserProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Product")
public class ProductController {
    @Resource

    @Autowired
    BankCardMapper bankCardMapper;

    @Autowired
    ProductMapper productMapper;
    @Autowired
    UserProductMapper userproductMapper;

    @Autowired
    EnterpriseMapper enterpriseMapper;
    @PostMapping("/getProduct")//获取所有商品
    public Result getProduct(){
        List<Product> products=productMapper.selectList(null);
        if(products.size()>0){
            return Result.success(products);
        }
        return Result.error("300","搜索不到任何商品！");
    }

    @PostMapping("/BuyProduct")//购买某个商品
    public Result BuyProduct(@RequestBody Map map){
        Integer productId=(Integer) map.get("productid");
        Product product=productMapper.selectById(productId);
        if(product.getAmount()>product.getSum()){//产品尚未售罄
            Integer cardid=(Integer) map.get("cardid");
            float cost=Float.valueOf((String)map.get("cost"));
            BankCard bankCard=bankCardMapper.selectById(cardid);
            if(bankCard.getBalance()>=cost){//余额足够
                Integer uid=(Integer) map.get("uid");
                UserProduct userProduct=userproductMapper.selectOne(Wrappers.<UserProduct>lambdaQuery().eq(UserProduct::getUid,uid));
                //设置细节
                UserProductDetails userProductDetails=new UserProductDetails();
                userProductDetails.setCost(cost);
                userProductDetails.setBalance(cost);
                userProductDetails.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
                DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now=LocalDateTime.now();
                String time=df.format(now);
                now=LocalDateTime.parse(time,df);
                userProductDetails.setPaytime(now);
                userProductDetails.setCardid(cardid);
                userProductDetails.setProductid(productId);
                userProductDetails.setState(1);
                //
                if(userProduct!=null){//已经存在了一条数据
                    List<UserProductDetails> details= JSONArray.parseArray(userProduct.getProduct(),UserProductDetails.class);
                    details.add(userProductDetails);
                    userProduct.setProduct(JSONArray.toJSONString(details));
                    if(buyproductandsetcard(uid,userProductDetails)==1){
                        userproductMapper.updateById(userProduct);
                        return Result.success();
                    }
                    return Result.error("500","账户出现问题，购买失败，请联系工作人员或到线下购买");
                }
                else{
                    userProduct=new UserProduct();
                    userProduct.setUid(uid);
                    Enterprise enterprise=enterpriseMapper.selectOne(Wrappers.<Enterprise>lambdaQuery().eq(Enterprise::getUid,uid));
                    if(enterprise!=null){
                        userProduct.setEnterprise(1);
                    }
                    else{
                        userProduct.setEnterprise(2);
                    }
                    List<UserProductDetails> details=new ArrayList<>();
                    details.add(userProductDetails);
                    userProduct.setProduct(JSONArray.toJSONString(details));
                    if(buyproductandsetcard(uid,userProductDetails)==1){
                        userproductMapper.insert(userProduct);
                        return Result.success();
                    }
                    return Result.error("500","账户出现问题，购买失败，请联系工作人员或到线下购买");
                }
            }
            return Result.error("500","当前账户余额不足，请更换账户进行购买");
        }
        return Result.error("500","抱歉，当前商品已售罄！！");
    }

    //购买商品操作(设置银行卡信息和账单信息)
    public int buyproductandsetcard(int uid, UserProductDetails userProductDetails){
        BankCard bankCard=bankCardMapper.selectById(userProductDetails.getCardid());
        if(bankCard!=null){
            List<Detail> details= JSONArray.parseArray(bankCard.getDetail(),Detail.class);
            bankCard.setBalance(bankCard.getBalance()-userProductDetails.getCost());//设置当前余额
            Detail detail=new Detail();
            detail.setId(userProductDetails.getId());
            detail.setPaytime(userProductDetails.getPaytime());
            detail.setCost(userProductDetails.getCost());
            detail.setPlace("网上银行");
            detail.setType("理财");
            String describe="用于购买理财产品，产品名称：";
            Product product=productMapper.selectById(userProductDetails.getProductid());
            product.setSum(product.getSum()+ userProductDetails.getCost());
            describe=describe+product.getName();
            detail.setDescribe(describe);
            details.add(detail);
            bankCard.setDetail(JSONArray.toJSONString(details));
            //产品处理
            productMapper.updateById(product);
            bankCardMapper.updateById(bankCard);
            return 1;
        }
        return 0;
    }

}
