package com.tensquare.user.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * WebMvc配置类
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    //注入JwtInterceptor
    @Autowired
    private MyInterceptor jwtInterceptor;

    /**
     * WebMvc中添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                //添加需要拦截器拦截的资源
                .addPathPatterns("/**")
                //排除不需要拦截的资源,如登录
                .excludePathPatterns("/**/login");
    }
}
