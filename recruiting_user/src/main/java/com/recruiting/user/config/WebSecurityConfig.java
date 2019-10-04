package com.recruiting.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authorizeRequests:security全注解配置实现的开端,表示开始说明需要的权限
        //第一部分表示拦截的路径,第二部分表示访问需要的权限
        //antMatchers表示拦截的路径,permitAll任何权限都可以访问
        //anyRequest任意请求,authenticated认证后才能访问
        //.and().csrf().disable()固定写法,表示csrf拦截失效
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
