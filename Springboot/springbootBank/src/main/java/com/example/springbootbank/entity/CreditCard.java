package com.example.springbootbank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("creditcard")
public class CreditCard {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer cid;
    private float limits;
    private Integer returnday;
    private Integer payday;
    private float rate;
    private float overdue;
    private Integer rank;
    private String stages;
    private String debt;//欠款（每月账单）
}
