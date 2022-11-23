package com.gunjo.skilldemo.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

/**
 * 消息发送者
 */
@Slf4j
@Service
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送秒杀消息
     */
    public void sendSeckillMessage(String message) {
        log.info("发送消息:" + message);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message", message);
    }

//    public void send(Object msg) {
//        log.info("发送消息:" + msg);
//        rabbitTemplate.convertAndSend("queue", msg);
//    }
}
