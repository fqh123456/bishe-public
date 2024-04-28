
package com.example.springbootbank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise")
public class Enterprise {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private String name;
    private String marketid;
    private float balance;
}
