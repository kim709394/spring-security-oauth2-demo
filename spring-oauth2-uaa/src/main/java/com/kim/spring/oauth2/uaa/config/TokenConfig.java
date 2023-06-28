package com.kim.spring.oauth2.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author Kim Huang
 * @description
 * @date 2023-06-28
 */
@SpringBootConfiguration
public class TokenConfig {

    //配置令牌存储策略
    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }

    //配置认证令牌服务
    @Bean
    public AuthorizationServerTokenServices tokenServices(@Autowired ClientDetailsService clientDetailsService,
                                                          @Autowired TokenStore tokenStore){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setSupportRefreshToken(true);     //支持刷新token
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setAccessTokenValiditySeconds(3600);  //令牌默认有效期一小时
        tokenServices.setRefreshTokenValiditySeconds(259200);   //刷新令牌默认有效期3天
        return tokenServices;

    }

}
