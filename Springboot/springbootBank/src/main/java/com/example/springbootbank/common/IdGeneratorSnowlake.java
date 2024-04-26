package com.example.springbootbank.common;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class IdGeneratorSnowlake {

    private static IdGeneratorSnowlake instance;
    private long workerId=0;
    private long datacenterId=1;
    private Snowflake snowflake=IdUtil.createSnowflake(workerId,datacenterId);

    @PostConstruct
    public void init(){
        try {
            workerId= NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
        }catch (Exception e){
            workerId=NetUtil.getLocalhostStr().hashCode();
            e.printStackTrace();
        }
    }
    public static IdGeneratorSnowlake getInstance(){
        if(instance==null){
            instance=new IdGeneratorSnowlake();
        }
        return instance;
    }

    public synchronized long snowflakeId(){
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(long workerId,long datacenterId){
        return snowflake.nextId();
    }
}
