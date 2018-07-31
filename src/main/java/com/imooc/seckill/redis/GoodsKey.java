package com.imooc.seckill.redis;

/**
 * @author : chris
 * 2018-07-30
 */
public class GoodsKey extends BasePrefix {
    protected GoodsKey(String prefix) {
        super(60, prefix);
    }

    public static GoodsKey goodsKey = new GoodsKey("gl");
    public static GoodsKey goodsDetail = new GoodsKey("gd");
    public static GoodsKey seckillGoodsStock = new GoodsKey("gs");
}
