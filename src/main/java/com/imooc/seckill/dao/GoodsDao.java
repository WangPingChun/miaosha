package com.imooc.seckill.dao;

import com.imooc.seckill.entity.Goods;
import com.imooc.seckill.entity.SeckillGoods;
import com.imooc.seckill.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author : chris
 * 2018-07-29
 */
@Mapper
public interface GoodsDao {
	/**
	 * 列出所有的秒杀商品信息.
	 *
	 * @return 秒杀商品信息
	 */
	@Select("select g.*,sg.stock_count,sg.start_date,sg.end_date,sg.miaosha_price from seckill_goods sg left join goods g on sg.goods_id = g.id")
	List<GoodsVO> listGoodsVO();

	/**
	 * 根据商品id查询商品信息.
	 *
	 * @param goodId 商品id
	 * @return 商品信息
	 */
	@Select("select g.*,sg.stock_count,sg.start_date,sg.end_date,sg.miaosha_price from seckill_goods sg left join goods g on sg.goods_id = g.id where sg.goods_id = #{goodId}")
	GoodsVO getGoodsVOByGoodsId(Long goodId);

	/**
	 * 减少库存.
	 *
	 * @param seckillGoods 商品信息
	 * @return 影响的行数
	 */
	@Update("update seckill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
	int reduceStock(SeckillGoods seckillGoods);
}
