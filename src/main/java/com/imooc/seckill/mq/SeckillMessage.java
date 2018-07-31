package com.imooc.seckill.mq;

import com.imooc.seckill.entity.User;
import lombok.Data;

/**
 * @author : chris
 * 2018-07-31
 */
@Data
public class SeckillMessage {
    private User user;
    private Long goodsId;
}
