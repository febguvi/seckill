package com.gunjo.skilldemo.rabbitmq;

import com.gunjo.skilldemo.pojo.SeckillMessage;
import com.gunjo.skilldemo.pojo.SeckillOrder;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.service.GoodsService;
import com.gunjo.skilldemo.service.OrderService;
import com.gunjo.skilldemo.utils.JsonUtil;
import com.gunjo.skilldemo.vo.GoodsVo;
import com.gunjo.skilldemo.vo.RespBean;
import com.gunjo.skilldemo.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.*;

/**
 * 消息发送者
 */
@Slf4j
@Service
public class MQReceiver {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = "seckillQueue")
    public void receieve(String message) {
        log.info("接收消息:" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodId = seckillMessage.getGoodId();
        User user = seckillMessage.getUser();

        // 判断库存
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }

        // 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodId);
        if (seckillOrder != null) {
            return;
        }

        // 下单操作
        orderService.secKill(user, goodsVo);
    }
}
