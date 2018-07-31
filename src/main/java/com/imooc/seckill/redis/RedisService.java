package com.imooc.seckill.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * @author : chris
 * 2018-07-26
 */
@Service
public class RedisService {

    private final JedisPool jedisPool;

    @Autowired
    public RedisService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 判断给定的key是否存在.
     *
     * @param prefix 前缀
     * @param key    key
     * @return 是否存在
     */
    public boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(prefix.getPerfix() + key);
        } finally {
            close(jedis);
        }
    }

    /**
     * 自增key.
     *
     * @param prefix 前缀
     * @param key    key
     * @return 自增后的结果
     */
    public long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(prefix.getPerfix() + key);
        } finally {
            close(jedis);
        }
    }

    /**
     * 子减key.
     *
     * @param prefix 前缀
     * @param key    key
     * @return 自减后的结果
     */
    public long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(prefix.getPerfix() + key);
        } finally {
            close(jedis);
        }
    }

    /**
     * 获取单个对象.
     *
     * @param prefix 前缀
     * @param key    key
     * @param clazz  对象的class
     * @param <T>    对象type
     * @return 获取到的对象
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            final String value = jedis.get(prefix.getPerfix() + key);
            return stringToBean(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(jedis);
        }
    }

    /**
     * 设置对象.
     *
     * @param prefix 前缀
     * @param key    key
     * @param value  值
     * @param <T>    对象type
     * @return 操作结果
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            final String stringValue = beanToString(value);
            if (stringValue == null || stringValue.length() <= 0) {
                return false;
            }

            final int expireSeconds = prefix.expireSeconds();
            if (expireSeconds <= 0) {
                jedis.set(prefix.getPerfix() + key, stringValue);
            } else {
                jedis.setex(prefix.getPerfix() + key, expireSeconds, stringValue);
            }

            return true;
        } finally {
            close(jedis);
        }
    }

    /**
     * 删除
     *
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.del(prefix.getPerfix() + key) >= 1;
        } finally {
            close(jedis);
        }
    }

    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        final Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return String.valueOf(value);
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return String.valueOf(value);
        } else {
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T stringToBean(String value, Class<T> clazz) {
        if (value == null || value.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == String.class) {
            return (T) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else {
            return JSON.toJavaObject(JSON.parseObject(value), clazz);
        }
    }

    private void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
