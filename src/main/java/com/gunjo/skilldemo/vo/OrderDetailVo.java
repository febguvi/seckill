package com.gunjo.skilldemo.vo;

import com.gunjo.skilldemo.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单详情返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {

    private Order order;

    private GoodsVo goodsVo;
}
