package cn.tedu.knows.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class TokenConfig {
    // 配置保存令牌的策略对象到spring容器
    // 1.保存在内存
    // 2.生成令牌保存在客户端
    @Bean
    public TokenStore tokenStore()
    {
        return new InMemoryTokenStore();
    }
}
