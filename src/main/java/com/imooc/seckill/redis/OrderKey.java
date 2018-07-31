package com.imooc.seckill.redis;

/**
 * @author : chris
 * 2018-07-27
 */
public class OrderKey extends BasePrefix {
	protected OrderKey(String prefix) {
		super(3600, prefix);
	}

	public static OrderKey getSeckillOrderByUidAndGid = new OrderKey("soug");
}
