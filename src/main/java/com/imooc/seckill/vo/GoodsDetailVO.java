package com.imooc.seckill.vo;

import com.imooc.seckill.entity.User;
import lombok.Data;

/**
 *
 * @author : WangPingChun
 * 2018-07-31
 */
@Data
public class GoodsDetailVO {
	private Integer seckillStatus;

	private Integer remainSeconds;

	private GoodsVO goodsVO;

	private User user;
}
