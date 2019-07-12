package com.github.bg.admin.core.annotation.filter;


import com.github.bg.admin.core.annotation.AuthController;
import com.github.bg.admin.core.auth.ReleaseUrl;
import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.util.RedisCache;
import com.github.bg.admin.core.util.StringUtils;
import com.github.bg.admin.core.util.WriteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author linzf
 * @since 2019/6/10
 * 类描述：角色鉴权拦截器的实现
 */
public class AuthControllerInterceptor implements HandlerInterceptor {

    /**
     * 获取放行的权限的接口
     */
    @Autowired
    private ReleaseUrl releaseUrl;

    /**
     * redis工具类
     */
    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        if (SystemStaticConst.ACTION_TYPE_OPTIONS.equals(httpServletRequest.getMethod())) {
            return true;
        }
        String actionUrl = httpServletRequest.getServletPath();
        // 判断当前的响应地址是否可以放行
        String releasePath = releaseUrl.getReleaseUrl();
        if (releasePath.indexOf(actionUrl) == -1) {
            // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 从方法处理器中获取出要调用的方法
            Method method = handlerMethod.getMethod();
            // 获取出方法上的AuthController注解
            AuthController authController = method.getAnnotation(AuthController.class);
            if (authController == null) {
                // 如果注解为null, 说明不需要拦截, 直接放过
                return true;
            }
            if (authController.authorities().length > 0) {
                // 如果权限配置不为空, 则取出配置值
                String[] authorities = authController.authorities();
                String token = httpServletRequest.getHeader("x-access-token");
                if (token == null || "".equals(token) || "null".equals(token)) {
                    WriteUtil.write(httpServletResponse, SystemStaticConst.NOT_LOGIN, "用户未登录");
                    return false;
                }
                String[] userRoles = StringUtils.getObjStr(redisCache.getString(token + "_USER_ROLE")).split(":");
                if (userRoles.length == 0) {
                    WriteUtil.write(httpServletResponse, SystemStaticConst.AUTH_FAIL, "用户无权限访问");
                    return false;
                }
                // 判断当前的用户的角色是否有当前节点的角色权限，若有则放行，若无则不放行
                for (String auth : authorities) {
                    for (String userRole : userRoles) {
                        if(auth.equals(userRole)){
                            return true;
                        }
                    }
                }
                WriteUtil.write(httpServletResponse, SystemStaticConst.AUTH_FAIL, "用户无权限访问");
                return false;
            }
        }
        return true;
    }

}
