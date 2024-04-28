
package com.example.springbootbank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootbank.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
