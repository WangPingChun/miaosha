package com.imooc.seckill.controller;

import com.imooc.seckill.entity.User;
import com.imooc.seckill.mq.RabbitMQSender;
import com.imooc.seckill.mq.SeckillMessage;
import com.imooc.seckill.redis.GoodsKey;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.OrderService;
import com.imooc.seckill.service.SeckillService;
import com.imooc.seckill.vo.GoodsVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : chris
 * 2018-07-29
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
    private final GoodsService goodsService;
    private final OrderService orderService;
    private final SeckillService seckillService;
    private final RedisService redisService;
    private final RabbitMQSender sender;

    @Autowired
    public SeckillController(GoodsService goodsService, OrderService orderService, SeckillService seckillService, RedisService redisService, RabbitMQSender sender) {
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.seckillService = seckillService;
        this.redisService = redisService;
        this.sender = sender;
    }

    @GetMapping
    public Result<Long> seckillResult(User user, @RequestParam Long goodsId) {
        // 判断用户是否登录
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        long result = seckillService.getSeckillResult(user.getId(),goodsId);

        return Result.success(result);
    }

    @PostMapping
    public Result<Integer> seckill(User user, @RequestParam Long goodsId) {

        // 判断用户是否登录
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        // 预减库存
        final long stock = redisService.decr(GoodsKey.seckillGoodsStock, String.valueOf(goodsId));
        if (stock < 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 入队列
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setUser(user);
        seckillMessage.setGoodsId(goodsId);
        sender.sendSeckillMessage(seckillMessage);


        return Result.success(0);
//		// 判断库存
//		final GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
//		final Integer stockCount = goods.getStockCount();
//		if (stockCount <= 0) {
//			return Result.error(CodeMsg.MIAO_SHA_OVER);
//		}
//
//		// 判断是否已经秒杀到了
//		SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
//		if (order != null) {
//			return Result.error(CodeMsg.REPEATE_MIAOSHA);
//		}
//
//		// 减库存 下订单 写入秒杀订单
//		OrderInfo orderInfo = seckillService.seckill(user, goods);
//
//		return Result.success(orderInfo);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final List<GoodsVO> goodsList = goodsService.listGoodsVO();
        if (goodsList != null) {
            for (GoodsVO vo : goodsList) {
                redisService.set(GoodsKey.seckillGoodsStock, String.valueOf(vo.getId()), vo.getStockCount());
            }
        }
    }
}
