package com.imooc.seckill.controller;

import com.imooc.seckill.entity.User;
import com.imooc.seckill.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author : WangPingChun
 * 2018-07-30
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@GetMapping
	public Result<User> userInfo(User user) {
		return Result.success(user);
	}
}
