package com.imooc.seckill.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author : chris
 * 2018-07-29
 */
@Data
public class SeckillGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Double miaoshaPrice;
    private Date startDate;
    private Date endDate;
}
