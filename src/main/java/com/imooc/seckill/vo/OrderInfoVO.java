package com.imooc.seckill.vo;

import com.imooc.seckill.entity.OrderInfo;
import lombok.Data;

/**
 *
 * @author : WangPingChun
 * 2018-07-31
 */
@Data
public class OrderInfoVO {
	private GoodsVO goods;
	private OrderInfo order;
}
