package com.github.bg.admin.core.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：redis操作类
 *
 * @author linzf
 */
public class RedisCache {

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    /**
     * 功能描述：获取模糊匹配的数据
     * @param key 模糊匹配的key
     * @return 返回查询的结果
     */
    public List<String> queryKeys(String key){
        List<String> keys = new ArrayList<>();
       Set<String> result = redisTemplate.keys(key + "*");
       for(String s : result){
           keys.add(this.getString(s));
       }
       return keys;
    }

    /**
     * 功能描述：删除KEY
     * @param key
     * @return
     */
    public void deleteKey(String key){
        redisTemplate.delete(key);
    }

    /**
     * 功能描述：获取key的过期时间
     * @param key
     * @return
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 功能描述：判断key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public boolean setString(String key, String value) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                return true;
            }
        });
        return result;
    }

    public boolean setString(String key, String value, int time) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                if (time>0) {
                    connection.expire(serializer.serialize(key),time);
                }
                return true;
            }
        });
        return result;
    }

    public String getString(String key){
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value =  connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }


    public boolean expire(String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }


    public <T> boolean setList(String key, List<T> list) {
        String value = JsonUtils.objToJson(list);
        return setString(key,value);
    }


    public <T> List<T> getList(final String key, Class<T> clz) {
        String json = getString(key);
        if(json!=null){
            List<T> list = JsonUtils.jsonToList(json, clz);
            return list;
        }
        return null;
    }

    /**
     * 功能描述：从redis中获取数据
     * @param key
     * @param elementType
     * @param <T>
     * @return
     */
    public <T> T getObject(final String key,Class<T> elementType){
        return redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
                if (connection.exists(keybytes)) {
                    byte[] valuebytes = connection.get(keybytes);
                    @SuppressWarnings("unchecked")
                    T value = (T) SerializeUtil.unserialize(valuebytes);
                    return value;
                }
                return null;
            }
        });
    }

    /**
     * 功能描述：设值到redis中
     * @param key
     * @param obj
     */
    public void setObject(String key, Object obj) {
        final byte[] bytes = SerializeUtil.serialize(obj);
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.set(redisTemplate.getStringSerializer().serialize(key), bytes);
                return null;
            }
        });
    }

    /**
     * 功能描述：设值到redis中
     * @param key
     * @param obj
     * @param time
     */
    public void setObject(String key, Object obj, int time) {
        final byte[] bytes = SerializeUtil.serialize(obj);
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.set(redisTemplate.getStringSerializer().serialize(key), bytes);
                return null;
            }
        });
        this.expire(key,time);
    }




    public long rPush(String key, Object obj) {
        String value = JsonUtils.objToJson(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }


    public String lPop(String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] res =  connection.lPop(serializer.serialize(key));
                return serializer.deserialize(res);
            }
        });
        return result;
    }


}
