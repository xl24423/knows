package cn.tedu.knows.faq.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 设置全部放行的配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().   // 禁用防跨域攻击
        authorizeRequests().     // 授权请求
        anyRequest().permitAll();   // 任何请求都放行
    }
}
