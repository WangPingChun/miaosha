package com.imooc.seckill.service;

import com.imooc.seckill.entity.OrderInfo;
import com.imooc.seckill.entity.SeckillOrder;
import com.imooc.seckill.entity.User;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.redis.SeckillKey;
import com.imooc.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : chris
 * 2018-07-29
 */
@Service
public class SeckillService {
    private final GoodsService goodsService;
    private final OrderService orderService;
    private final RedisService redisService;

    @Autowired
    public SeckillService(GoodsService goodsService, OrderService orderService, RedisService redisService) {
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.redisService = redisService;
    }


    @Transactional(rollbackFor = RuntimeException.class)
    public OrderInfo seckill(User user, GoodsVO goods) {
        // 减库存 下订单 写入秒杀订单
        if (goodsService.reduceStock(goods)) {
            // order_info miaosha_order
            return orderService.createOrder(user, goods);
        }
        setGoodsOver(goods.getId());
        return null;
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, String.valueOf(goodsId), true);
    }

    public long getSeckillResult(Long userId, Long goodsId) {
        final SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(userId, goodsId);

        if (order != null) {
            // 秒杀成功
            return order.getId();
        } else {
            if (getGoodsOver(goodsId)) {
                return -1;
            }
            return 0;
        }
    }

    private boolean getGoodsOver(Long goodsId) {
        return  redisService.exists(SeckillKey.isGoodsOver, String.valueOf(goodsId));
    }
}
