package com.imooc.seckill.redis;

/**
 * @author : chris
 * 2018-07-27
 */
public class UserKey extends BasePrefix {
    private static final int EXPRIE = 3600 * 60 * 24;

    private UserKey(int exprie, String prefix) {
        super(exprie, prefix);
    }

    public static UserKey token = new UserKey(EXPRIE,"tk");
    public static UserKey getById = new UserKey(0,"gbi");
}
