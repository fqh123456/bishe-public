package com.example.springbootbank.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootbank.entity.News;
import com.example.springbootbank.mapper.NewsMapper;
import org.springframework.stereotype.Service;

@Service
public class NewsService extends ServiceImpl<NewsMapper, News> implements INewsService {

}
