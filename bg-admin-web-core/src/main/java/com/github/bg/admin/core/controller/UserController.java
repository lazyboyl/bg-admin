package com.github.bg.admin.core.controller;


import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author linzf
 * @since 2019/4/25
 * 类描述：
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 功能描述：根据token和旧的密码来更新新的密码
     *
     * @param oldPassword 旧的密码
     * @param newPassword 新的密码
     * @return 返回更新结果
     */
    @ApiOperation(value = "根据token和旧的密码来更新新的密码")
    @PostMapping("changePassword")
    public ReturnInfo changePassword(@RequestParam(name = "oldPassword") String oldPassword,
                                     @RequestParam(name = "newPassword") String newPassword) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("x-access-token");
        return userService.changePassword(token, oldPassword, newPassword);
    }

    /**
     * 功能描述：删除用户
     *
     * @param userId 用户ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "删除用户")
    @PostMapping("deleteUser")
    public ReturnInfo deleteUser(@RequestParam(name = "userId") String userId) {
        return userService.deleteUser(userId);
    }

    /**
     * 功能描述：更新用户
     *
     * @param userId       用户ID
     * @param nickName     真实姓名
     * @param headImg      头像地址
     * @param province     所在省
     * @param provinceName 所在省名称
     * @param city         所在市
     * @param cityName     所在市名称
     * @param area         所在区
     * @param areaName     所在区名称
     * @param address      地址
     * @param orgIds       组织架构ID
     * @param roles        角色集合
     * @return 返回数据新增的结果
     */
    @ApiOperation(value = "更新用户")
    @PostMapping("updateUser")
    public ReturnInfo updateUser(@RequestParam(name = "userId") String userId,
                                 @RequestParam(name = "nickName") String nickName,
                                 @RequestParam(name = "headImg") String headImg,
                                 @RequestParam(name = "province") String province,
                                 @RequestParam(name = "provinceName") String provinceName,
                                 @RequestParam(name = "city") String city,
                                 @RequestParam(name = "cityName") String cityName,
                                 @RequestParam(name = "area") String area,
                                 @RequestParam(name = "areaName") String areaName,
                                 @RequestParam(name = "address") String address,
                                 @RequestParam(name = "orgIds") String[] orgIds,
                                 @RequestParam(name = "roles") String[] roles) {
        return userService.updateUser(userId, nickName, headImg, province, provinceName, city, cityName, area, areaName, address, orgIds, roles);
    }

    /**
     * 功能描述：根据用户流水ID来获取用户数据
     *
     * @param userId 用户流水ID
     * @return 返回获取结果
     */
    @ApiOperation(value = "根据用户流水ID来获取用户数据")
    @PostMapping("getUserByUserId")
    public ReturnInfo getUserByUserId(@RequestParam(name = "userId") String userId) {
        return userService.getUserByUserId(userId);
    }

    /**
     * 功能描述：加载所有的角色数据
     *
     * @return 返回加载结果
     */
    @ApiOperation(value = "加载所有的角色数据")
    @PostMapping("loadAllRole")
    public ReturnInfo loadAllRole() {
        return userService.loadAllRole();
    }


    /**
     * 功能描述：创建用户
     *
     * @param loginAccount 用户账号
     * @param nickName     真实姓名
     * @param headImg      头像地址
     * @param province     所在省
     * @param provinceName 所在省名称
     * @param city         所在市
     * @param cityName     所在市名称
     * @param area         所在区
     * @param areaName     所在区名称
     * @param address      地址
     * @param orgIds       组织架构ID
     * @param roles        角色集合
     * @return 返回数据新增的结果
     */
    @ApiOperation(value = "创建用户")
    @PostMapping("createUser")
    public ReturnInfo createUser(@RequestParam(name = "loginAccount") String loginAccount,
                                 @RequestParam(name = "nickName") String nickName,
                                 @RequestParam(name = "headImg") String headImg,
                                 @RequestParam(name = "province") String province,
                                 @RequestParam(name = "provinceName") String provinceName,
                                 @RequestParam(name = "city") String city,
                                 @RequestParam(name = "cityName") String cityName,
                                 @RequestParam(name = "area") String area,
                                 @RequestParam(name = "areaName") String areaName,
                                 @RequestParam(name = "address") String address,
                                 @RequestParam(name = "orgIds") String[] orgIds,
                                 @RequestParam(name = "roles") String[] roles) {
        return userService.createUser(loginAccount, nickName, headImg, province, provinceName, city, cityName, area, areaName, address, orgIds, roles);
    }

    /**
     * 功能描述：验证这个账户是否已经创建过了
     *
     * @param loginAccount 用户账号
     * @return 返回验证结果
     */
    @ApiOperation(value = "验证这个账户是否已经创建过了")
    @PostMapping("checkLoginAccount")
    public ReturnInfo checkLoginAccount(@RequestParam(name = "loginAccount") String loginAccount) {
        return userService.checkLoginAccount(loginAccount);
    }

    /**
     * 功能描述：获取用户列表
     *
     * @param fullPath     父组织架构的匹配路径
     * @param search       根据账号和名称来模糊查询
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取用户列表")
    @PostMapping("queryUserList")
    public ReturnInfo queryUserList(@RequestParam(name = "fullPath") String fullPath, @RequestParam(name = "search") String search,
                                    @RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "current") int current,
                                    @RequestParam(name = "orderKey") String orderKey, @RequestParam(name = "orderByValue") String orderByValue) {
        return userService.queryUserList(fullPath, search, pageSize, current, orderKey, orderByValue);
    }

    /**
     * 功能描述：实现重新刷新token
     *
     * @param refreshToken token的值
     * @return 返回刷新结果
     */
    @ApiOperation(value = "实现刷新用户的token")
    @PostMapping("refreshToken")
    public ReturnInfo refreshToken(@RequestParam(name = "refreshToken") String refreshToken) {
        return userService.refreshToken(refreshToken);
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

}
