package com.imooc.seckill.redis;

/**
 * @author : chris
 * 2018-07-27
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String prefix;

    protected BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;

    }

    protected BasePrefix(String prefix) {
        this(0, prefix);
    }

    @Override
    public int expireSeconds() {
        // 默认0代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPerfix() {
        final String className = getClass().getSimpleName();
        return className + ":" + prefix + ":";
    }
}
