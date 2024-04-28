
package com.example.springbootbank.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* 统一返回接口包装类
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success(){//传成功的状态码
        return new Result(Constants.CODE_200,"",null);
    }

    public static Result success(Object data){//传成功之后的数据
        return new Result(Constants.CODE_200,"",data);
    }

    public static Result error(String code,String msg){//码和错误信息
        return new Result(code,msg,null);
    }

    public static Result error(){//传系统错误
        return new Result(Constants.CODE_500,"系统错误",null);
    }
}
