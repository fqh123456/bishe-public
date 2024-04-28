package com.example.springbootbank;


import cn.hutool.core.date.LocalDateTimeUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootbank.common.CreateBankCards;
import com.example.springbootbank.common.IdGeneratorSnowlake;
import com.example.springbootbank.common.Result;
import com.example.springbootbank.entity.*;

import com.example.springbootbank.mapper.*;
import com.example.springbootbank.service.CodeService;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@SpringBootTest
@MapperScan("com.example.springbootbank.mapper")
class SpringbootBankApplicationTests {

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
    @Test
    void contextLoads() {
//        productwork();
            setoverdue();
//        cbills(10);
    }
    //创建月初账单
    public void cbills(Integer ccid){
        //月初创建上个月的账单

        LocalDateTime now=LocalDateTime.now();
        now=now.plusMonths(0);//本月
        LocalDateTime localDatestart=LocalDateTime.of(now.minusMonths(1L).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(),LocalTime.MIN);
        LocalDateTime localDateslast=LocalDateTime.of(now.minusMonths(1L).with(TemporalAdjusters.lastDayOfMonth()).toLocalDate(),LocalTime.MAX);
        System.out.println("月初："+localDatestart+"  月末："+localDateslast);
        BankCard bankCard=bankCardMapper.selectById(ccid);
        if(bankCard!=null){//存在该银行卡
            List<Detail> details= JSONArray.parseArray(bankCard.getDetail(),Detail.class);
            System.out.println(details);
            Debt debt=new Debt();
            List<Long> pays=new ArrayList<>();
            float needreturn=0;
            for(int i=0;i<details.size();i++){
                //筛选出范围内的
                if(localDateslast.isAfter(details.get(i).getPaytime())&&localDatestart.isBefore(details.get(i).getPaytime())){
                    if(details.get(i).getType().equals("消费")||details.get(i).getType().equals("取出")){
                        needreturn=needreturn+details.get(i).getCost();
                        pays.add(details.get(i).getId());
                    }
                }
            }
            //创建账单
            if(pays.size()>0){//存在账单才进入
                debt.setInterest(0);
                debt.setNeedreturn(Math.round(needreturn*100)/100f);
                debt.setDays(0);
                debt.setId(IdGeneratorSnowlake.getInstance().snowflakeId());
                debt.setCost(Math.round(needreturn*100)/100f);
                debt.setReturnmoney(0);
                //后面要改
                debt.setTime(localDatestart.plusMonths(1).toLocalDate());
                debt.setTimelast(localDatestart.plusMonths(1).plusDays(9).toLocalDate());
                //
                debt.setBills(pays);
                //添加账单
                CreditCard creditCard=creditCardMapper.selectOne(Wrappers.<CreditCard>lambdaQuery().eq(CreditCard::getCid,ccid));
                if(creditCard!=null){//存在该信用卡
                    List<Debt> Debts= JSONArray.parseArray(creditCard.getDebt(),Debt.class);
                    Debts.add(debt);
                    creditCard.setDebt(JSONArray.toJSONString(Debts));
                    System.out.println(creditCard);
                    creditCardMapper.updateById(creditCard);
                }
                else {
                    System.out.println("不存在该信用卡卡");
                }
            }
        }
        else{
            System.out.println("不存在该银行卡");
        }
    }
    //每天检查是否存在逾期账单并设置逾期天数
    public void setoverdue(){
        List<CreditCard> creditCards=creditCardMapper.selectList(null);//获取所有的信用卡
        for(int i=0;i<creditCards.size();i++){
            Integer flag=0;
            List<Debt> Debts= JSONArray.parseArray(creditCards.get(i).getDebt(),Debt.class);
            for(int j=0;j<Debts.size();j++){
                if((Debts.get(j).getNeedreturn()+Debts.get(j).getInterest())>=0.01){//账单未还清
                    if(Debts.get(j).getDays()>0){//已经逾期的
                        flag=1;
                        Debts.get(j).setDays(Debts.get(j).getDays()+1);
                        float rates=Debts.get(j).getInterest()+(Debts.get(j).getNeedreturn()+Debts.get(j).getInterest())*((creditCards.get(i).getRate()));//每日利率
                        BigDecimal bigDecimal=new BigDecimal(rates);
                        float balance2=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
                        Debts.get(j).setInterest(balance2);
                    }
                    else{//尚未逾期的
                        LocalDate now=LocalDate.now();
                        if(Debts.get(j).getTimelast().isBefore(now)){//第一天逾期
                            flag=1;
                            Debts.get(j).setDays(1);
                            float overdue=Debts.get(j).getNeedreturn()*creditCards.get(i).getOverdue();//本金滞留金
                            float rates=Debts.get(j).getNeedreturn()*creditCards.get(i).getRate();//每日利率
                            BigDecimal bigDecimal=new BigDecimal(rates+overdue);
                            float balance2=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
                            Debts.get(j).setInterest(balance2);
                        }
                    }
                }
            }
            if(flag==1){//有修改的
                creditCards.get(i).setDebt(JSONArray.toJSONString(Debts));
//                creditCardMapper.updateById(creditCards.get(i));
                System.out.println(Debts);
            }
        }
    }
    //理财产品收益
    public void productwork(){
        List<UserProduct> userProducts=userProductMapper.selectList(null);
        for(int i=0;i<userProducts.size();i++){
            //获取当前用户的理财产品购买记录
            List<UserProductDetails> details= JSONArray.parseArray(userProducts.get(i).getProduct(),UserProductDetails.class);
            int flag=0;//看该用户其下是否有产品的余额发生了变化,用于节省性能

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
                    BigDecimal bigDecimal=new BigDecimal(balance);
                    float balance2=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
                    details.get(j).setBalance(balance2);
                }
            }
            if(flag==1){//更新该用户的理财产品情况
                userProducts.get(i).setProduct(JSONArray.toJSONString(details));
                userProductMapper.updateById(userProducts.get(i));
            }
        }
    }

}
@Data
class historicalrate{
    public LocalDateTime time;
    public double rate;
}
class Myutils {

    public static String getName() {
        Random random = new Random();
        String[] Surname = {"赵","钱","孙","李","周","吴","郑","王","冯","陈","褚","卫","蒋","沈","韩","杨","朱","秦","尤","许","何","吕","施","张","孔","曹","严","华","金","魏","陶","姜","戚","谢","邹","喻","柏","水","窦","章","云","苏","潘","葛","奚","范","彭","郎","鲁","韦","昌","马","苗","凤","花","方","俞","任","袁","柳","酆","鲍","史","唐","费","廉","岑","薛","雷","贺","倪","汤","滕","殷","罗","毕","郝","邬","安","常","乐","于","时","傅","皮","卞","齐","康","伍","余","元","卜","顾","孟","平","黄","和","穆","萧","尹","姚","邵","湛","汪","祁","毛","禹","狄","米","贝","明","臧","计","伏","成","戴","谈","宋","茅","庞","熊","纪","舒","屈","项","祝","董","梁","杜","阮","蓝","闵","席","季","麻","强","贾","路","娄","危","江","童","颜","郭","梅","盛","林","刁","钟","丘","徐","邱","骆","高","夏","蔡","田","樊","胡","凌","霍","虞","万","支","柯","昝","管","卢","莫","经","房","裘","缪","干","解","应","宗","丁","宣","贲","邓","单","杭","洪","包","诸","左","石","崔","吉","钮","龚","程","嵇","邢","滑","裴","陆","荣","翁","荀","羊","於","惠","甄","曲","家","封","芮","羿","储","靳","汲","邴","糜","松","井","段","富","巫","乌","焦","巴","弓","牧","隗","山","谷","车","侯","宓","蓬","全","郗","班","仰","秋","仲","伊","宫","宁","仇","栾","暴","甘","钭","厉","戎","祖","武","符","刘","景","詹","束","龙","叶","幸","司","韶","郜","黎","蓟","薄","印","宿","白","怀","蒲","台","从","鄂","索","咸","籍","赖","卓","蔺","屠","蒙","池","乔","阴","郁","胥","能","苍","双","闻","莘","党","翟","谭","贡","劳","逢","逄","姬","申","扶","堵","冉","宰","郦","雍","璩","桑","桂","濮","牛","寿","通","边","扈","燕","冀","郏","浦","尚","农","温","别","庄","晏","柴","瞿","阎","充","慕","连","茹","习","宦","艾","鱼","容","向","古","易","慎","戈","廖","庚","终","暨","居","衡","步","都","耿","满","弘","匡","国","文","寇","广","禄","阙","东","欧","殳","沃","利","蔚","越","夔","隆","师","巩","厍","聂","晁","勾","敖","融","冷","訾","辛","阚","那","简","饶","空","曾","毋","沙","乜","养","鞠","须","丰","巢","关","蒯","相","查","荆","红","游","竺","权","逯","盖","益","桓","公","万俟","司马","上官","欧阳","夏侯","诸葛","闻人","东方","赫连","皇甫","尉迟","公羊","澹台","公冶","宗政","濮阳","淳于","单于","太叔","申屠","公孙","仲孙","轩辕","令狐","钟离","宇文","长孙","慕容","鲜于","闾丘","司徒","司空","亓官","司寇","仉督","子车","颛孙","端木","巫马","公西","漆雕","乐正","壤驷","公良","拓拔","夹谷","宰父","谷粱","晋楚","阎法","汝鄢","涂钦","段干","百里","东郭","南门","呼延","归海","羊舌","微生","岳帅","缑亢","况后","有琴","梁丘","左丘","东门","西门","商牟","佘佴","伯赏","南宫","墨哈","谯笪","年爱","阳佟","第五","言福"};

        int index = random.nextInt(Surname.length - 1);
        String name = Surname[index]; // 获得一个随机的姓氏
        /* 从常用字中选取一个或两个字作为名 */
        if (random.nextBoolean()) {
            name += Myutils.getChinese() + Myutils.getChinese();
        } else {
            name += Myutils.getChinese();
        }
        return name;

    }

    public static String getChinese() {
        String str = null;
        int highPos, lowPos;
        Random random = new Random();
        highPos = (176 + Math.abs(random.nextInt(71)));// 区码，0xA0打头，从第16区开始，即0xB0=11*16=176,16~55一级汉字，56~87二级汉字
        random = new Random();
        lowPos = 161 + Math.abs(random.nextInt(94));// 位码，0xA0打头，范围第1~94列

        byte[] bArr = new byte[2];
        bArr[0] = (new Integer(highPos)).byteValue();
        bArr[1] = (new Integer(lowPos)).byteValue();
        try {
            str = new String(bArr, "GB2312"); // 区位码组合成汉字
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}

