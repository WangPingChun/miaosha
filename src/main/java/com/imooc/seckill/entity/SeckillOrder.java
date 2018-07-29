package com.imooc.seckill.entity;

import lombok.Data;

/**
 * @author : chris
 * 2018-07-29
 */
@Data
public class SeckillOrder {
    private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;
}
