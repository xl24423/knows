package cn.tedu.knows.auth.security;

import cn.tedu.knows.auth.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

@Configuration
// Oauth2 标准的实现 授权服务器配置类
// 这个配置独立于项目之外, 用于接收用户信息和分析登录目标客户端,返回对应的权限信息
// 启动授权服务器相关功能
// 授权服务器
@EnableAuthorizationServer
public class Authorization extends AuthorizationServerConfigurerAdapter {
    // 添加依赖的注释
    // Spring-Security 框架的授权管理器, Oauth2要使用
    @Resource
    public AuthenticationManager manager;
    // 要登陆肯定需要登陆核心配置类
    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // Oauth2 框架提供了很多控制器方法
        // 所以我们配置的就是这些控制器方法运行的内容
        // endpoints参数就是控制器方法的功能
        // 往 endpoints 里面加东西,相当于往 Oauth2 给的控制器方法里面加东西
        endpoints.authenticationManager(manager)    // 确定使用 SpringSecurity框架中的授权管理器
                // 设置登陆配置类
                .userDetailsService(userDetailsService)
                // 配置 登录允许提交方式
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                // 配置生成令牌对象
                .tokenServices(tokenService());
    }

    // 注入保存令牌配置的对象
    @Resource
    private TokenStore tokenStore;
    // 添加客户端详情对象 (Oauth2提供)
    @Resource
    private ClientDetailsService clientDetailsService;

    @Bean
    public AuthorizationServerTokenServices tokenService() {
        // 实例化 令牌生成器对象
        DefaultTokenServices services = new DefaultTokenServices();
        // 令牌保存的策略  (这里保存在 内存中 return new InMemoryTokenStore();)
        services.setTokenStore(tokenStore);
        // 设置令牌的有效期 (单位为秒)
        services.setAccessTokenValiditySeconds(3600);
        // 指定生成令牌的客户端, 设置客户端详情
        services.setClientDetailsService(clientDetailsService);
        // 最后返回生成器对象
        return services;
    }

    // 获得加密对象 , 用于下面方法中的加密操作
    @Resource
    private PasswordEncoder passwordEncoder;
    // 核心配置方法2: 配置客户端对应的各种权限

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置当前授权服务器支持的客户端
        clients.inMemory() // 内存中保存客户端信息
                // 因为我们现在只有一个达内知道项目 , 所以只用配置一个客户端
                .withClient("knows") // 客户端名称
                .secret(passwordEncoder.encode("123456")) // 客户端定义的加密密码
                // 以及客户端的权限
                .scopes("main")           // main 代表 达内知道 knows全部权限
                .authorizedGrantTypes("password");   // 配置 达内知道 登陆方式
    }
    // 核心配置方法3 : 配置客户端允许使用的功能
    // 如果是一个大型项目 , 需要在这个方法中配置
    // 哪些客户端有哪些 权限 以及 功能
    // 我们的客户端是开放所有功能, 所以配置简单

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许哪些客户端生成令牌 (配置permitAll() 全部允许)
//        security.tokenKeyAccess("knows")
        security.tokenKeyAccess("permitAll()")
                // 允许哪些客户端验证令牌 (配置permitAll() 全部允许)
                .checkTokenAccess("permitAll()")
                // 允许通过验证的客户端保存令牌
                .allowFormAuthenticationForClients();
    }
}
