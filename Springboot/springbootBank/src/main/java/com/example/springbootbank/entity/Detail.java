
package com.example.springbootbank.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Detail {//银行卡交易细节实体类
    private long id;//账单编号
    private String reciaccount;//对方账户
    private LocalDateTime paytime;//交易时间
    private float cost;//金额
    private String type;//交易类型（消费、存入、取出、还款、收益、转账接收、转账）
    private String place;//交易地点（微信、银行、支付宝）
    private String describe;//用途
    public Detail(long id,String reciaccount,LocalDateTime paytime,float cost,String type,String place){
        this.id=id;
        this.reciaccount=reciaccount;
        this.paytime=paytime;
        this.cost=cost;
        this.type=type;
        this.place=place;
    }
    public Detail(){

    }
}
