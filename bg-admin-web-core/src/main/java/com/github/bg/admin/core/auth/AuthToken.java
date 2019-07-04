package com.github.bg.admin.core.auth;

/**
 * @author linzf
 * @since 2019-07-04
 * 类描述：生成token和refresh token的接口类
 */
public interface AuthToken {

    /**
     * 功能描述：生成authToken的值
     * @return 返回authToken的值
     */
    String authToken();

    /**
     * 功能描述：生成refreshToken的值
     * @return 返回refreshToken的值
     */
    String  refreshToken();

    /**
     * 功能描述：authToken的过期时间
     * @return 返回过期时间
     */
    int authTokenExpire();

    /**
     * 功能描述：refreshToken的过期时间
     * @return 返回过期时间
     */
    int refreshTokenExpire();

}
