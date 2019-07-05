package com.github.bg.admin.core.auth.impl;


import com.github.bg.admin.core.auth.AuthConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author linzf
 * @since 2019-06-05
 * 类描述：鉴权的配置接口类的实现
 */
@Component
public class AuthConfigProvider implements AuthConfig {

    @Value("${auth.singleLanding}")
    private boolean singleLanding;

    @Override
    public boolean isSingleLanding() {
        return singleLanding;
    }
}
