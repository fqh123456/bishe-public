package com.example.springbootbank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootbank.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper extends BaseMapper<Product> {
}
