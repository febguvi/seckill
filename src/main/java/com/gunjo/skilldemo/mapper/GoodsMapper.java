package com.gunjo.skilldemo.mapper;

import com.gunjo.skilldemo.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gunjo.skilldemo.vo.GoodsVo;

import java.util.*;

/**
 * @Entity com.gunjo.skilldemo.pojo.Goods
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();


    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}




