package com.imooc.seckill.controller;

import com.imooc.seckill.mq.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : chris
 * 2018-07-31
 */
@RestController
public class SampleController {

    private final RabbitMQSender sender;

    @Autowired
    public SampleController(RabbitMQSender sender) {
        this.sender = sender;
    }

//    @GetMapping("/mq")
//    public void rabbitMQ() {
//        sender.send("hello rabbitmq");
//    }
//
//    @GetMapping("/mq/topic")
//    public void rabbitMQTopic() {
//        sender.sendTopic("hello rabbitmq");
//    }
}
