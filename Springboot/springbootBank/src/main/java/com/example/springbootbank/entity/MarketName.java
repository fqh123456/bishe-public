
package com.example.springbootbank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("marketname")
public class MarketName {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String marketname;
    private Integer evaluation;
}
