
package com.example.springbootbank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer age;
    private String job;
    private String marital;
    private String education;
    private String account;
    private Integer password;
    private String phone;
    private String idcard;
    private String bankcards;
    private LocalDateTime lasttime;

}
