package com.imooc.seckill.mq;

import com.imooc.seckill.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : chris
 * 2018-07-31
 */
@Slf4j
@Service
public class RabbitMQSender {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public RabbitMQSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }


    public void sendSeckillMessage(SeckillMessage seckillMessage) {
        final String msg = RedisService.beanToString(seckillMessage);
        log.info("send seckill message : {}", msg);
        amqpTemplate.convertAndSend(RabbitMQConfig.SECKILL_QUEUE, msg);
    }
}
