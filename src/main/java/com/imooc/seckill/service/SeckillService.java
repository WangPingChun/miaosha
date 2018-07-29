package com.imooc.seckill.service;

import com.imooc.seckill.dao.GoodsDao;
import com.imooc.seckill.entity.Goods;
import com.imooc.seckill.entity.OrderInfo;
import com.imooc.seckill.entity.User;
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

    @Autowired
    public SeckillService(GoodsService goodsService, OrderService orderService) {
        this.goodsService = goodsService;
        this.orderService = orderService;
    }


    @Transactional(rollbackFor = RuntimeException.class)
    public OrderInfo seckill(User user, GoodsVO goods) {
        // 减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);

        // order_info miaosha_order
        return orderService.createOrder(user, goods);
    }
}
