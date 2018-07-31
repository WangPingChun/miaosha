package com.imooc.seckill.mq;

import com.imooc.seckill.entity.SeckillOrder;
import com.imooc.seckill.entity.User;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.OrderService;
import com.imooc.seckill.service.SeckillService;
import com.imooc.seckill.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : chris
 * 2018-07-31
 */
@Slf4j
@Service
public class RabbitMQReceiver {
    private final GoodsService goodsService;
    private final OrderService orderService;
    private final SeckillService seckillService;
    @Autowired
    public RabbitMQReceiver(GoodsService goodsService, OrderService orderService, SeckillService seckillService) {
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.seckillService = seckillService;
    }

    @RabbitListener(queues = RabbitMQConfig.SECKILL_QUEUE)
    public void receive(String message) {
        log.info("receive seckill message : {}", message);
        final SeckillMessage seckillMessage = RedisService.stringToBean(message, SeckillMessage.class);
        final User user = seckillMessage.getUser();
        final Long goodsId = seckillMessage.getGoodsId();
        final GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
		final Integer stockCount = goods.getStockCount();
		if (stockCount <= 0) {
			return;
		}

        // 判断是否已经秒杀到了
		SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
		if (order != null) {
			return;
		}

        seckillService.seckill(user, goods);
    }

    /*
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receive(String message) {
        log.info("receive message --- {}", message);
    }

    @RabbitListener(queues = RabbitMQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        log.info("receive queue1 message --- {}", message);
    }

    @RabbitListener(queues = RabbitMQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        log.info("receive queue2 message --- {}", message);
    }
    */
}
