package com.imooc.miaosha.controller;

import com.imooc.miaosha.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : chris
 * 2018-07-25
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @GetMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "Chris");
        return "hello";
    }

    @GetMapping("/db/get")
    @ResponseBody
    public Result<String> dbGet() {

    }
}
