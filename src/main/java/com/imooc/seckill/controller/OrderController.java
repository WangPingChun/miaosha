package com.imooc.seckill.controller;

import com.imooc.seckill.entity.OrderInfo;
import com.imooc.seckill.entity.User;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.OrderService;
import com.imooc.seckill.vo.GoodsVO;
import com.imooc.seckill.vo.OrderInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author : WangPingChun
 * 2018-07-31
 */
@RestController
@RequestMapping("/order")
public class OrderController {
	private final OrderService orderService;
	private final GoodsService goodsService;

	@Autowired
	public OrderController(OrderService orderService, GoodsService goodsService) {
		this.orderService = orderService;
		this.goodsService = goodsService;
	}


	@GetMapping("/{orderId}")
	public Result<OrderInfoVO> getOrderInfo(@PathVariable Long orderId, User user) {

		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}

		OrderInfo orderInfo = orderService.getOrderById(orderId);
		if (orderInfo == null) {
			return Result.error(CodeMsg.ORDER_NOT_EXIST);
		}

		long goodsId = orderInfo.getGoodsId();
		GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);


		OrderInfoVO vo = new OrderInfoVO();
		vo.setGoods(goodsVO);
		vo.setOrder(orderInfo);

		return Result.success(vo);
	}
}
