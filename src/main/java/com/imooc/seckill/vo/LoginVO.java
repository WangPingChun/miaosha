package com.imooc.seckill.vo;

import com.imooc.seckill.validator.Mobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author : chris
 * 2018-07-27
 */
@Data
public class LoginVO {
    @NotNull
    @Mobile
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;
}
