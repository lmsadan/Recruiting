package com.recruiting.scheduled.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    private String pattern="thumbup_";
    /**
         second, minute, hour, day of month, month , day of week  e.g. {@code "0 * * * * MON-FRI"}
     */
    @Scheduled(cron = "0 * * * * *")
    public void emptyPraise(){
//        List<Map<String, String>> saveList=new ArrayList<Map<String, String>>();
//        for (int i = 0; i < 10000; i++) {
//            Map<String,String> map=new HashMap<>();
//            map.put("key",pattern+i);
//            map.put("value","value值为"+i);
//            saveList.add(map);
//        }
        //batchInsert(saveList);
        redisStringBatchGet();

    }

    public void batchInsert(List<Map<String, String>> saveList) {
        /* 插入多条数据 */
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                for (Map<String, String> needSave : saveList) {
                    redisTemplate.opsForValue().set(needSave.get("key"), needSave.get("value"));
                }
                return null;
            }
        });
    }

    public void redisStringBatchGet() {
        //long stime=System.currentTimeMillis();
        Set<String> keys = redisTemplate.keys("*"+pattern + "**");
        List<String> keyList=new ArrayList<>();
        keys.forEach(i->{
            keyList.add(i);
        });
        batchGet(keyList);
        //List<String> strings = redisTemplate.opsForValue().multiGet(keyList);
        long etime=System.currentTimeMillis();
        //System.out.println("string="+strings);
        //System.out.println("使用multiGet消耗时间为："+(etime-stime));
    }

    public List<String> batchGet(List<String> keyList) {
        List objects = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                for (String key : keyList) {
            /* 批量获取多条数据 */
                    //stringRedisConnection.get(key);
            /* 批量删除数据 */
                    stringRedisConnection.del(key);
                }
                return null;
            }
        });

        //List<String> collect = (List<String>) objects.stream().map(val -> String.valueOf(val)).collect(Collectors.toList());
        return null;
    }
}
