package com.imooc.seckill.redis;

/**
 * @author : chris
 * 2018-07-31
 */
public class SeckillKey extends BasePrefix {
    protected SeckillKey(String prefix) {
        super(prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey("go");
}
