package com.imooc.seckill.dao;

import com.imooc.seckill.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author : chris
 * 2018-07-27
 */
@Mapper
public interface UserDao {
    /**
     * 根据id查询用户.
     *
     * @param id id
     * @return User对象
     */
    @Select("select * from seckill_user where id = #{id}")
    User getById(@Param("id") Long id);
}
