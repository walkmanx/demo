package com.zy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Title: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Version:zhuoyuan V2.0</p>
 *
 * @author gc
 * @description
 * @date 2020/8/6 下午 16:36
 */
@RestController
@RequestMapping("")
public class HealthCheckController {

    @GetMapping("/health")
    public String healthCheck(){
        return "ok";
    }

}
