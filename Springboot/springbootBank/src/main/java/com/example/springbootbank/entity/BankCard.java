package com.example.springbootbank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("bankcard")
public class BankCard {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private Integer type;
    private float balance;
    private String cardnumber;
    private String detail;
    private LocalDateTime createtime;

}
