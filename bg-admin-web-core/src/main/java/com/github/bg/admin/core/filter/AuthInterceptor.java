package com.github.bg.admin.core.filter;


import com.github.bg.admin.core.auth.ReleaseUrl;
import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.dao.BehaviorLogDao;
import com.github.bg.admin.core.entity.BehaviorLog;
import com.github.bg.admin.core.entity.User;
import com.github.bg.admin.core.util.RedisCache;
import com.github.bg.admin.core.util.StringUtils;
import com.github.bg.admin.core.util.WriteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author linzf
 * @since 2019-05-14
 * 类描述：拦截器实现权限的拦截
 */
public class AuthInterceptor implements HandlerInterceptor {

    private Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    /**
     * 日志的dao
     */
    @Autowired
    private BehaviorLogDao behaviorLogDao;

    /**
     * 操作redis的实现类
     */
    @Autowired
    private RedisCache redisCache;
    /**
     * 获取放行的权限的接口
     */
    @Autowired
    private ReleaseUrl releaseUrl;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token = httpServletRequest.getHeader("x-access-token");
        String actionUrl = httpServletRequest.getServletPath();
        if (SystemStaticConst.ACTION_TYPE_OPTIONS.equals(httpServletRequest.getMethod())) {
            return true;
        }
        String jsPattern = ".*.js";
        String cssPattern = ".*.css";
        if(Pattern.matches(jsPattern, actionUrl)||Pattern.matches(cssPattern, actionUrl)){
            return true;
        }
        BehaviorLog behaviorLog = new BehaviorLog();
        behaviorLog.setActionDate(new Date());
        behaviorLog.setActionMethod(actionUrl);

        /**
         * 判断当前的响应地址是否可以放行
         */
        String releasePath = releaseUrl.getReleaseUrl();
        if (releasePath.indexOf(actionUrl) == -1) {
            if (token == null || "".equals(token) || "null".equals(token)) {
                WriteUtil.write(httpServletResponse, SystemStaticConst.NOT_LOGIN, "用户未登录");
                return false;
            }
            /**
             * 判断当前的用户是否有权限访问当前节点
             */
            log.debug("token:{}", token);
            String powerPath = StringUtils.getObjStr(redisCache.getString(token));
            if ("".equals(powerPath)) {
                WriteUtil.write(httpServletResponse, SystemStaticConst.AUTH_TOKEN_EXPIRE, "token过期");
                return false;
            }
            if (powerPath.indexOf(actionUrl) == -1) {
                WriteUtil.write(httpServletResponse, SystemStaticConst.AUTH_FAIL, "用户无权限，请联系系统管理员");
                return false;
            }
            /**
             * 根据token来获取当前登录的用户的信息
             */
            User user = redisCache.getObject(token + "_USER", User.class);
            if (user == null) {
                behaviorLog.setActionUser("");
                behaviorLog.setActionUserId("");
                behaviorLogDao.insert(behaviorLog);
                WriteUtil.write(httpServletResponse, SystemStaticConst.NOT_LOGIN, "用户未登录");
                return false;
            } else {
                behaviorLog.setActionUser(user.getNickName());
                behaviorLog.setActionUserId(user.getUserId());
                behaviorLogDao.insert(behaviorLog);
            }
        } else {
            behaviorLog.setActionUser("");
            behaviorLog.setActionUserId("");
            behaviorLogDao.insert(behaviorLog);
        }
        return true;
    }


}
