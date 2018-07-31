package com.imooc.seckill.service;

import com.imooc.seckill.dao.OrderDao;
import com.imooc.seckill.entity.OrderInfo;
import com.imooc.seckill.entity.SeckillOrder;
import com.imooc.seckill.entity.User;
import com.imooc.seckill.redis.OrderKey;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author : chris
 * 2018-07-29
 */
@Service
public class OrderService {
	private final OrderDao orderDao;
	private final RedisService redisService;

	@Autowired
	public OrderService(OrderDao orderDao, RedisService redisService) {
		this.orderDao = orderDao;
		this.redisService = redisService;
	}

	public SeckillOrder getSeckillOrderByUserIdAndGoodsId(Long userId, Long goodsId) {
		SeckillOrder seckillOrder = redisService.get(OrderKey.getSeckillOrderByUidAndGid, userId + "_" + goodsId, SeckillOrder.class);
		if (seckillOrder == null) {
			seckillOrder = orderDao.getSeckillOrderByUserIdAndGoodsId(userId, goodsId);
			if (seckillOrder == null) {
				return null;
			}
			redisService.set(OrderKey.getSeckillOrderByUidAndGid, userId + "_" + goodsId, seckillOrder);
		}
		return seckillOrder;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public OrderInfo createOrder(User user, GoodsVO goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setUserId(user.getId());
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setCreateDate(new Date());

		long orderId = orderDao.saveOrderInfo(orderInfo);

		SeckillOrder seckillOrder = new SeckillOrder();
		seckillOrder.setUserId(user.getId());
		seckillOrder.setOrderId(orderId);
		seckillOrder.setGoodsId(goods.getId());

		orderDao.saveSeckillOrder(seckillOrder);


		return orderInfo;
	}

	public OrderInfo getOrderById(Long orderId) {
		return orderDao.getOrderById(orderId);
	}
}
