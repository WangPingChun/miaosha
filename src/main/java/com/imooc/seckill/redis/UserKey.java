package com.imooc.seckill.redis;

/**
 * @author : chris
 * 2018-07-27
 */
public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        super(1000,prefix);
    }

    public static UserKey token = new UserKey("tk");
}
