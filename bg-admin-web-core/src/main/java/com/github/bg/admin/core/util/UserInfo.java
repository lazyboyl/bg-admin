package com.github.bg.admin.core.util;

import com.github.bg.admin.core.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author linzf
 * @since 2019/6/21
 * 类描述：用户工具类
 */
public class UserInfo {

    /**
     * 功能描述：获取当前登录的用户的信息
     * @param redisCache
     * @return
     */
    public static User getLoginUser(RedisCache redisCache){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("x-access-token");
        if("".equals(token)){
            return null;
        }
        return redisCache.getObject(token + "_USER",User.class);
    }

}
