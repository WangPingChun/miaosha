package com.imooc.seckill.entity;

import lombok.Data;

/**
 * @author : chris
 * 2018-07-29
 */
@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
