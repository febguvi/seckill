package com.gunjo.skilldemo.service;

import com.gunjo.skilldemo.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gunjo.skilldemo.vo.GoodsVo;

import java.util.*;

/**
 *
 */
public interface GoodsService extends IService<Goods> {

    // 获取商品列表
    List<GoodsVo> findGoodsVo();

    // 获取商品详情
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
