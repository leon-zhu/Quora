package com.quora.config;

import com.quora.interceptor.LoginRequiredInteceptor;
import com.quora.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 *
 * @author: leon
 * @date: 2018/5/1 20:18
 * @version: 1.0
 */
@Component
public class QuoraWebConfiguration implements WebMvcConfigurer {

    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private LoginRequiredInteceptor loginRequiredInteceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注意顺序
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInteceptor).addPathPatterns("/user/*");
        //registry.addInterceptor(loginRequiredInteceptor).addPathPatterns("/user/*").addPathPatterns("/question/*");
    }
}
