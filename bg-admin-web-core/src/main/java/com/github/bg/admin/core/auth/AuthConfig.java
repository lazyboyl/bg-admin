package com.github.bg.admin.core.auth;

/**
 * @author linzf
 * @since 2019-07-04
 * 类描述：鉴权的配置接口类
 */
public interface AuthConfig {

    /**
     * 功能描述：获取当前的登陆是否允许单点登陆
     * @return
     */
    boolean isSingleLanding();

}
