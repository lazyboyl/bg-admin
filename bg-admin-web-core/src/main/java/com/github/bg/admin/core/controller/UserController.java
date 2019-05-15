package com.github.bg.admin.core.controller;

import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.entity.ReturnInfo;
import io.swagger.annotations.ApiOperation;
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

    /**
     * 功能描述：模拟实现用户登录
     * @param loginAccount 用户账号
     * @param loginPassword 用户密码
     * @return 返回登录结果
     */
    @ApiOperation(value = "实现用户登陆")
    @PostMapping("login")
    public ReturnInfo login(@RequestParam(name = "loginAccount") String loginAccount, @RequestParam(name = "loginPassword") String loginPassword){
        Map<String,Object> result = new HashMap<>(1);
        result.put("token","token123");
        return new ReturnInfo(SystemStaticConst.SUCCESS, "登陆成功",result);
    }

}
