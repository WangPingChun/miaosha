package com.imooc.seckill.service;

import com.imooc.seckill.dao.UserDao;
import com.imooc.seckill.entity.User;
import com.imooc.seckill.exception.GlobalException;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.redis.UserKey;
import com.imooc.seckill.util.MD5Utils;
import com.imooc.seckill.util.UUIDUtils;
import com.imooc.seckill.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.imooc.seckill.result.CodeMsg.*;

/**
 * @author : chris
 * 2018-07-27
 */
@Service
public class UserService {
	private final UserDao userDao;
	private final RedisService redisService;
	public static final String COOKIE_NAME_TOKEN = "token";

	@Autowired
	public UserService(UserDao userDao, RedisService redisService) {
		this.userDao = userDao;
		this.redisService = redisService;
	}

	public String login(HttpServletResponse response, LoginVO loginVO) {
		if (loginVO == null) {
			throw new GlobalException(SERVER_ERROR);
		}
		final String mobile = loginVO.getMobile();
		final String password = loginVO.getPassword();
		final User user = userDao.getById(Long.parseLong(mobile));
		if (user == null) {
			throw new GlobalException(MOBILE_NOT_EXIST);
		}
		// 验证密码
		final String userPassword = user.getPassword();
		final String salt = user.getSalt();
		if (!MD5Utils.md5Password(password, salt).equals(userPassword)) {
			throw new GlobalException(PASSWORD_ERROR);
		}

		// 生成cookie
		final String token = UUIDUtils.uuid();
		addCookie(response, token, user);
		return token;
	}

	private void addCookie(HttpServletResponse response, String token, User user) {
		redisService.set(UserKey.token, token, user);

		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public User getByToken(HttpServletResponse response, String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		// 延长有效期
		final User user = redisService.get(UserKey.token, token, User.class);
		if (user != null) {
			addCookie(response, token, user);
		}
		return user;
	}
}
