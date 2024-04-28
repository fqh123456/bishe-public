
package com.example.springbootbank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("userproduct")
public class UserProduct {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private Integer enterprise;
    private String product;
}
