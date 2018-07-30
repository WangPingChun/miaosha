package com.imooc.seckill.controller;

import com.imooc.seckill.entity.User;
import com.imooc.seckill.redis.GoodsKey;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author : chris
 * 2018-07-28
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    private final GoodsService goodsService;
    private final RedisService redisService;
    private final ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    public GoodsController(GoodsService goodsService, RedisService redisService, ThymeleafViewResolver thymeleafViewResolver) {
        this.goodsService = goodsService;
        this.redisService = redisService;
        this.thymeleafViewResolver = thymeleafViewResolver;
    }

    @GetMapping(produces = "text/html;charset=utf-8")
    public String goods(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        // 从缓存内取数据
        String html = redisService.get(GoodsKey.goodsKey, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        model.addAttribute("user", user);
        // 查询商品列表
        final List<GoodsVO> goodsVOList = goodsService.listGoodsVO();
        model.addAttribute("goodsList", goodsVOList);

        IContext iContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", iContext);

        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.goodsKey, "", html);
        }

        return html;
    }

    @GetMapping(value = "/{goodsId}", produces = "text/html")
    public String goodsDetail(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") Long goodId) {
        // 从redis缓存取页面
        String html = redisService.get(GoodsKey.goodsDetail, String.valueOf(goodId), String.class);

        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        // 手动渲染
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

        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", webContext);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.goodsDetail, String.valueOf(goodId), html);
        }

        return html;
    }
}
