package com.github.bg.admin.core.config;

import com.github.bg.admin.core.annotation.filter.AuthControllerInterceptor;
import com.github.bg.admin.core.filter.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author linzf
 * @since 2019-06-04
 * 类描述：设置拦截器
 */
@Component
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 初始化鉴权拦截器
     */
    private AuthInterceptor authInterceptor = new AuthInterceptor();

    /**
     * 初始化角色拦截器
     */
    private AuthControllerInterceptor authControllerInterceptor = new AuthControllerInterceptor();

    /**
     * 注入权限拦截器bean
     */
    @Bean
    AuthInterceptor authInterceptor(){
        return  authInterceptor;
    }

    /**
     * 注入权限注解拦截器bean
     */
    @Bean
    AuthControllerInterceptor authControllerInterceptor() {
        return authControllerInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 先加的拦截器先执行
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(authControllerInterceptor()).addPathPatterns("/**");
    }


}
