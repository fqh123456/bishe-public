package com.example.springbootbank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootbank.entity.BankCard;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardMapper extends BaseMapper<BankCard> {
}
