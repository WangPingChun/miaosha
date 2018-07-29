package com.imooc.seckill.redis;

/**
 * @author : chris
 * 2018-07-27
 */
public interface KeyPrefix {
    /**
     * 获取过期时间.
     * @return 过期时间
     */
    int expireSeconds();

    /**
     * 获取前缀.
     * @return key的前缀
     */
    String getPerfix();
}
