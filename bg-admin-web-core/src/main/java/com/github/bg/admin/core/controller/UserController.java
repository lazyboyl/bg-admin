package com.github.bg.admin.core.controller;

import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linzf
 * @since 2019/5/15
 * 类描述：用户的controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 功能描述：根据token来获取用户数据
     *
     * @param token token的值
     * @return 返回获取的结果
     */
    @ApiOperation(value = "根据token来获取用户数据")
    @PostMapping("getUserInfo")
    public ReturnInfo getUserInfo(@RequestParam(name = "token") String token) {
        return userService.getUserInfo(token);
    }

    /**
     * 功能描述：实现用户登陆
     *
     * @param loginAccount  用户账号
     * @param loginPassword 用户密码
     * @return 返回登陆结果
     */
    @ApiOperation(value = "实现用户登陆")
    @PostMapping("login")
    public ReturnInfo login(@RequestParam(name = "loginAccount") String loginAccount, @RequestParam(name = "loginPassword") String loginPassword) {
        return userService.login(loginAccount, loginPassword);
    }

}
