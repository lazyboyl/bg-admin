package com.github.bg.admin.core.auth.impl;


import com.github.bg.admin.core.auth.Auth;
import com.github.bg.admin.core.auth.AuthToken;
import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.dao.RoleDao;
import com.github.bg.admin.core.dao.TreeDao;
import com.github.bg.admin.core.dao.UserDao;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.entity.Role;
import com.github.bg.admin.core.entity.Tree;
import com.github.bg.admin.core.entity.User;
import com.github.bg.admin.core.util.RedisCache;
import com.github.bg.admin.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2019-06-04
 * 类描述：给予redis实现用户的鉴权
 */
@Component
public class RedisAuthProvider implements Auth {

    /**
     * redis工具类
     */
    @Autowired
    private RedisCache redisCache;
    /**
     * 生成authToken和refreshToken的接口
     */
    @Autowired
    private AuthToken authToken;
    /**
     * 用户的dao
     */
    @Autowired
    private UserDao userDao;
    /**
     * 菜单的dao
     */
    @Autowired
    private TreeDao treeDao;
    /**
     * 角色的dao
     */
    @Autowired
    private RoleDao roleDao;
    /**
     * 鉴权的配置接口类的实现
     */
    @Autowired
    private AuthConfigProvider authConfigProvider;


    @Override
    public ReturnInfo login(String loginAccount, String loginPassword) {
        User user = userDao.login(loginAccount, loginPassword);
        if (user == null) {
            return new ReturnInfo(SystemStaticConst.FAIL, "账号密码错误");
        }
        List<Role> roleList = roleDao.getUserRoleListByUserId(user.getUserId());
        StringBuilder userRoles = new StringBuilder();
        for (Role role : roleList) {
            userRoles.append(":").append(role.getRoleCode());
        }
        /**
         * 登录成功以后加载用户的权限，同时使用用户的token作为key，使用权限数据作为value来进行数据的保存
         */
        List<Tree> treeList = treeDao.getLoginUserPowerPath(user.getUserId());
        StringBuilder powerPaths = new StringBuilder();
        for (Tree tree : treeList) {
            if (tree.getPowerPath() != null && !"".equals(tree.getPowerPath())) {
                powerPaths.append(":").append(tree.getPowerPath());
            }
        }
        String authTokenValue = authToken.authToken();
        String refreshTokenValue = authToken.refreshToken();
        /**
         * 更新用户表中的token的值
         */
        user.setToken(authTokenValue);
        user.setLastLoginDate(new Date());
        userDao.updateByPrimaryKey(user);
        return new ReturnInfo(SystemStaticConst.SUCCESS, "登陆成功", setRedisToken("", authTokenValue, refreshTokenValue, powerPaths.toString(), user, userRoles.toString()));
    }

    /**
     * 功能描述：实现重新刷新token
     *
     * @param refreshToken token的值
     * @return 返回刷新token的结果
     */
    @Override
    public ReturnInfo refreshToken(String refreshToken) {
        /**
         * 从redis中获取权限的数据
         */
        String powerPaths = StringUtils.getObjStr(redisCache.getString(refreshToken));
        if ("".equals(powerPaths)) {
            return new ReturnInfo(SystemStaticConst.NOT_LOGIN, "无效的refreshToken");
        }
        /**
         * 从redis中获取当前登录的用户
         */
        User user = redisCache.getObject(refreshToken + "_USER", User.class);
        if (user == null) {
            return new ReturnInfo(SystemStaticConst.NOT_LOGIN, "无效的refreshToken");
        }
        /**
         * 从redis中获取当前登录的用户的角色
         */
        String userRoles = StringUtils.getObjStr(redisCache.getString(refreshToken + "_USER_ROLE"));
        if ("".equals(userRoles)) {
            return new ReturnInfo(SystemStaticConst.NOT_LOGIN, "无效的refreshToken");
        }
        redisCache.deleteKey(refreshToken);
        redisCache.deleteKey(refreshToken + "_USER");
        redisCache.deleteKey(refreshToken + "_USER_ROLE");
        String authTokenValue = authToken.authToken();
        String refreshTokenValue = authToken.refreshToken();
        return new ReturnInfo(SystemStaticConst.SUCCESS, "token刷新成功", setRedisToken(refreshToken, authTokenValue, refreshTokenValue, powerPaths, user, userRoles));
    }

    /**
     * 功能描述：实现redis中设置权限的相关数据
     *
     * @param oldRefreshTokenValue 旧的刷新的token
     * @param authTokenValue       鉴权的token
     * @param refreshTokenValue    刷新鉴权的token
     * @param powerPaths           权限集合
     * @param userRoles            角色的集合
     * @return 返回相应的值
     */
    private Map<String, Object> setRedisToken(String oldRefreshTokenValue, String authTokenValue, String refreshTokenValue, String powerPaths, User user, String userRoles) {
        String loginToken = "";
        /**
         * 当前系统是单点登录
         */
        if (!authConfigProvider.isSingleLanding()) {
            loginToken = StringUtils.getObjStr(redisCache.getString(user.getLoginAccount()));
            redisCache.setString(user.getLoginAccount(), authTokenValue + ":" + refreshTokenValue, authToken.refreshTokenExpire());
        } else if (!"".equals(oldRefreshTokenValue)) {
            loginToken = StringUtils.getObjStr(redisCache.getString(user.getLoginAccount()) + "_" + oldRefreshTokenValue);
            redisCache.setString(user.getLoginAccount() + "_" + refreshTokenValue, authTokenValue + ":" + refreshTokenValue, authToken.refreshTokenExpire());
        } else {
            redisCache.setString(user.getLoginAccount() + "_" + refreshTokenValue, authTokenValue + ":" + refreshTokenValue, authToken.refreshTokenExpire());
        }
        if (!"".equals(loginToken)) {
            // 删除另外登陆的账号的token信息
            String loginAuthTokenValue = loginToken.split(":")[0];
            redisCache.deleteKey(loginAuthTokenValue);
            redisCache.deleteKey(loginAuthTokenValue + "_USER");
            redisCache.deleteKey(loginAuthTokenValue + "_USER_ROLE");
            String loginRefreshTokenValue = loginToken.split(":")[1];
            redisCache.deleteKey(loginRefreshTokenValue);
            redisCache.deleteKey(loginRefreshTokenValue + "_USER");
            redisCache.deleteKey(loginRefreshTokenValue + "_USER_ROLE");
        }
        /**
         * 将authToken作为key,powerPaths作为值保存到redis中
         */
        redisCache.setString(authTokenValue, powerPaths, authToken.authTokenExpire());
        /**
         * 将authToken作为key,user作为值保存到redis中
         */
        redisCache.setObject(authTokenValue + "_USER", user, authToken.authTokenExpire());
        /**
         * 将authToken作为key,userRoles作为值保存到redis中
         */
        redisCache.setString(authTokenValue + "_USER_ROLE", userRoles, authToken.authTokenExpire());
        /**
         * 将refreshToken作为key,powerPaths作为值保存到redis中
         */
        redisCache.setString(refreshTokenValue, powerPaths, authToken.refreshTokenExpire());
        /**
         * 将refreshToken作为key,user作为值保存到redis中
         */
        redisCache.setObject(refreshTokenValue + "_USER", user, authToken.refreshTokenExpire());
        /**
         * 将refreshToken作为key,userRoles作为值保存到redis中
         */
        redisCache.setString(refreshTokenValue + "_USER_ROLE", userRoles, authToken.refreshTokenExpire());
        /**
         * 刷新token的时候，socket重新设置映射关系
         */
        if (!"".equals(oldRefreshTokenValue)) {
            String socketToken = redisCache.getString(oldRefreshTokenValue + "_SOCKET");
            if (socketToken != null && !"".equals(socketToken)) {
                redisCache.setString(refreshTokenValue + "_SOCKET", socketToken);
                redisCache.deleteKey(oldRefreshTokenValue + "_SOCKET");
            }
        }
        Map<String, Object> result = new HashMap<>(1);
        result.put("token", authTokenValue);
        result.put("refreshToken", refreshTokenValue);
        result.put("expiresIn", redisCache.getExpire(authTokenValue));
        return result;
    }
}
