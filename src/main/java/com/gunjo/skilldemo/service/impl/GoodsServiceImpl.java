package com.gunjo.skilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gunjo.skilldemo.pojo.Goods;
import com.gunjo.skilldemo.service.GoodsService;
import com.gunjo.skilldemo.mapper.GoodsMapper;
import com.gunjo.skilldemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import javax.annotation.*;
import java.util.*;

/**
 *
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    // 获取商品详情
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {

        return goodsMapper.findGoodsVoByGoodsId(goodsId);

    }
}




