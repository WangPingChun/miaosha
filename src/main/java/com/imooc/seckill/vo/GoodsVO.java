package com.imooc.seckill.vo;

import com.imooc.seckill.entity.Goods;
import lombok.Data;

import java.util.Date;

/**
 * @author : chris
 * 2018-07-29
 */
@Data
public class GoodsVO extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
