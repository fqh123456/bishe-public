package com.example.springbootbank.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//信用卡每月账单（欠款信息）
@Data
public class Debt {
    @TableId
    private long id;
    private LocalDate time;//账单产生日期
    private LocalDate timelast;//逾期前最后日期
    private float cost;//这个月总消费
    private float returnmoney;//已还款额度
    private float interest;//利息
    private float needreturn;//还需偿还的数额（本金）
    private Integer days;//逾期天数
    private List<Long> bills;//保存账单的id
}
