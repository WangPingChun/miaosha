package com.imooc.seckill.service;

import com.imooc.seckill.dao.GoodsDao;
import com.imooc.seckill.entity.SeckillGoods;
import com.imooc.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : chris
 * 2018-07-29
 */
@Service
public class GoodsService {
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsService(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    public List<GoodsVO> listGoodsVO() {
        return goodsDao.listGoodsVO();
    }

    public GoodsVO getGoodsVOByGoodsId(Long goodId) {
        return goodsDao.getGoodsVOByGoodsId(goodId);
    }

    public void reduceStock(GoodsVO goods) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setGoodsId(goods.getId());
        goodsDao.reduceStock(seckillGoods);
    }
}
