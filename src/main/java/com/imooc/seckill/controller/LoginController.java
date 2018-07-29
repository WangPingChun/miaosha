package com.imooc.seckill.controller;

import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.UserService;
import com.imooc.seckill.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.imooc.seckill.result.CodeMsg.SERVER_ERROR;
import static com.imooc.seckill.result.CodeMsg.SUCCESS;

/**
 * @author : chris
 * 2018-07-27
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping
    @ResponseBody
    public Result login(@Valid LoginVO loginVO, HttpServletResponse response) {
        // 登陆
        if (userService.login(response, loginVO)) {
            return Result.success(SUCCESS);
        }

        return Result.error(SERVER_ERROR);
    }
}
