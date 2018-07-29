package com.imooc.seckill.dao;

import com.imooc.seckill.entity.OrderInfo;
import com.imooc.seckill.entity.SeckillOrder;
import org.apache.ibatis.annotations.*;

/**
 * @author : chris
 * 2018-07-29
 */
@Mapper
public interface OrderDao {
    /**
     * 根据用户id和商品id查询秒杀订单.
     *
     * @param userId  用户id
     * @param goodsId 商品id
     * @return 秒杀订单
     */
    @Select("select * from seckill_order where user_id = #{userId} and goods_id = #{goodsId};")
    SeckillOrder getSeckillOrderByUserIdAndGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    /**
     * 新增订单.
     *
     * @param orderInfo 订单详情
     * @return 订单id
     */
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values(" +
            "#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate}) ")
    long saveOrderInfo(OrderInfo orderInfo);

    /**
     * 新增秒杀订单.
     *
     * @param seckillOrder 秒杀订单
     * @return 影响的行数
     */
    @Insert("insert into seckill_order(user_id, order_id, goods_id) VALUES (#{userId},#{orderId},#{goodsId})")
    int saveSeckillOrder(SeckillOrder seckillOrder);
}
