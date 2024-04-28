
package com.example.springbootbank.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProductDetails {//用户购买产品细节实体类
    private long id;//账单编号（订单编号，和对应银行卡交易细节实体类里的编号一致）
    private LocalDateTime paytime;//交易时间
    private Integer cardid;//交易银行卡的id
    private float cost;//交易金额
    private Integer productid;//交易产品id
    private float balance;//当前余额
    private Integer state;//状态，1为正在持续，2为已经结束

}
