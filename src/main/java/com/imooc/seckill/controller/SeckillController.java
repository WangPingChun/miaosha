package com.imooc.seckill.controller;

import com.imooc.seckill.entity.OrderInfo;
import com.imooc.seckill.entity.SeckillOrder;
import com.imooc.seckill.entity.User;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.OrderService;
import com.imooc.seckill.service.SeckillService;
import com.imooc.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : chris
 * 2018-07-29
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController {
	private final GoodsService goodsService;
	private final OrderService orderService;
	private final SeckillService seckillService;

	@Autowired
	public SeckillController(GoodsService goodsService, OrderService orderService, SeckillService seckillService) {
		this.goodsService = goodsService;
		this.orderService = orderService;
		this.seckillService = seckillService;
	}

	@PostMapping
	public Result<OrderInfo> seckill(User user, @RequestParam Long goodsId) {

		// 判断用户是否登录
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}

		// 判断库存
		final GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
		final Integer stockCount = goods.getStockCount();
		if (stockCount <= 0) {
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}

		// 判断是否已经秒杀到了
		SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
		if (order != null) {
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}

		// 减库存 下订单 写入秒杀订单
		OrderInfo orderInfo = seckillService.seckill(user, goods);

		return Result.success(orderInfo);
	}
}
