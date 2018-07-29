package com.imooc.seckill.controller;

import com.imooc.seckill.entity.User;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author : chris
 * 2018-07-28
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping
    public String goods(Model model, User user) {
        model.addAttribute("user", user);
        // 查询商品列表
        final List<GoodsVO> goodsVOList = goodsService.listGoodsVO();
        model.addAttribute("goodsList", goodsVOList);
        return "goods_list";
    }

    @GetMapping("/{goodsId}")
    public String goodsDetail(Model model, User user, @PathVariable("goodsId") Long goodId) {
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodId);
        final long startTime = goodsVO.getStartDate().getTime();
        final long endTime = goodsVO.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int seckillStatus;
        int remainSeconds;
        if (now < startTime) {
            // 秒杀没有开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {
            // 秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("user", user);
        model.addAttribute("goods", goodsVO);
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
