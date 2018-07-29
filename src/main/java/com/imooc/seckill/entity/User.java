package com.imooc.seckill.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author : chris
 * 2018-07-27
 */
@Data
public class User {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
