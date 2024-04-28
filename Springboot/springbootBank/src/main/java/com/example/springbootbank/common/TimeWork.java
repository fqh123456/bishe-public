package com.example.springbootbank.common;
//定时任务中心

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootbank.entity.*;
import com.example.springbootbank.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class TimeWork {
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
    //开始定时任务
    public void workstart(){

    }
    //每天计算每个用户购买每个理财产品的利润或者亏损
    public void productwork(){
        List<UserProduct> userProducts=userProductMapper.selectList(null);
        for(int i=0;i<userProducts.size();i++){
            //获取当前用户的理财产品购买记录
            List<UserProductDetails> details= JSONArray.parseArray(userProducts.get(i).getProduct(),UserProductDetails.class);
            int flag=0;//看该用户其下是否有产品的余额发生了变化,用于节省性能
            System.out.println(userProducts.get(i));
            for(int j=0;j<details.size();j++){//该用户购买的所有产品
                if(details.get(j).getState()==1){//该用户的该产品还未结束
                    flag=1;
                    Product product=productMapper.selectById(details.get(j).getProductid());
                    float balance=0;
                    if(product.getType()==2){//限期（七日年化）
                        balance=details.get(j).getBalance()*((float) ((100+product.getRate()/7)/100));
                    }
                    else {//固期，固期利率
                        float rate=(float) (100+(product.getRate()/(product.getMinday())))/100;
                        balance=(details.get(j).getBalance())*(rate);
                    }
                    details.get(j).setBalance(balance);
                }
            }
            if(flag==1){//更新该用户的理财产品情况
                userProducts.get(i).setProduct(JSONArray.toJSONString(details));
                System.out.println(userProducts.get(i));
                userProductMapper.updateById(userProducts.get(i));
            }
        }
    }
    //月初创建上个月的信用卡账单（所有银行卡）
    public void everCreditCardbills(){
        List<CreditCard> creditCards=creditCardMapper.selectList(null);
        for(int i=0;i<creditCards.size();i++){
            if(creditCards.get(i)!=null){//存在该信用卡
                cbills(creditCards.get(i));
            }
        }
    }

    //月初创建上个月的信用卡账单（单张银行卡）
    public void cbills(CreditCard creditCard){
        Integer ccid=creditCard.getCid();
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime localDatestart=LocalDateTime.of(now.minusMonths(1L).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(), LocalTime.MIN);
        LocalDateTime localDateslast=LocalDateTime.of(now.minusMonths(1L).with(TemporalAdjusters.lastDayOfMonth()).toLocalDate(),LocalTime.MAX);
        BankCard bankCard=bankCardMapper.selectById(ccid);
        if(bankCard!=null){//存在该银行卡
            List<Detail> details= JSONArray.parseArray(bankCard.getDetail(),Detail.class);
            Debt debt=new Debt();
            List<Long> pays=new ArrayList<>();
            float needreturn=0;
            for(int i=0;i<details.size();i++){
                //筛选出时间范围内的并且是消费和取出的记录
                if(localDateslast.isAfter(details.get(i).getPaytime())&&localDatestart.isBefore(details.get(i).getPaytime())){
                    if(details.get(i).getType().equals("消费")||details.get(i).getType().equals("取出")){
                        needreturn=needreturn+details.get(i).getCost();
                        pays.add(details.get(i).getId());
                    }
                }
            }
            //创建账单
            if(pays.size()>0){//有账单产生
                debt.setInterest(0);
                debt.setNeedreturn(Math.round(needreturn*100)/100f);
                debt.setDays(0);
                debt.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
                debt.setCost(Math.round(needreturn*100)/100f);
                debt.setReturnmoney(0);
                //设置本月的1号到10号
                debt.setTime(localDatestart.plusMonths(1).toLocalDate());
                debt.setTimelast(localDatestart.plusMonths(1).plusDays(9).toLocalDate());
                //
                debt.setBills(pays);
                //添加账单
                List<Debt> Debts= JSONArray.parseArray(creditCard.getDebt(),Debt.class);
                Debts.add(debt);
                creditCard.setDebt(JSONArray.toJSONString(Debts));
                creditCardMapper.updateById(creditCard);
            }
        }
    }


}
