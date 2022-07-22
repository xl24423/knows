package cn.tedu.knows.auth.security;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // 确定 security 权限功能开启
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 当前 auth 项目也是设置 spring security 全部放行
    // 因为登陆验证交给了 Oauth2 , spring security 不再进行 验证
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().anyRequest().permitAll().and().formLogin();
    }
    // 我们在 spring 容器中保存一个加密对象
    // 之后有配置需要加密, 就可以取出使用
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    // 我们在后面的配置中,需要 spring security 框架中的授权管理器
    // 授权管理器是 登录功能的重要组成部分, 现在是 Oauth2 需要它
    // 我们需要将这个授权管理器保存在spring容器中,以便 Oauth2 使用它
    // 覆盖 spring容器中原有的 authenticationManager , 名字要不同 : authenticationManagerBean
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();  // security 授权管理器

    }

}
