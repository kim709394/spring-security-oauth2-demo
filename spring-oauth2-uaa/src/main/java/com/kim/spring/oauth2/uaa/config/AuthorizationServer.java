package com.kim.spring.oauth2.uaa.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kim Huang
 * @description 统一认证、授权服务配置
 * @date 2023-05-11
 */
@SpringBootConfiguration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    //配置可信任的客户端
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        Map<String, String> additionalInfoForC1 = new HashMap<>();
        additionalInfoForC1.put("c1", "s1");
        clients.inMemory()
                .withClient("c1").scopes("interval")
                .secret(new BCryptPasswordEncoder().encode("s1"))
                .resourceIds("interval").autoApprove(true).redirectUris("www.baidu.com")
                .additionalInformation(additionalInfoForC1).authorities("r1", "r2")
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")
                .and()
                .withClient("c2").secret(new BCryptPasswordEncoder().encode("s2"))
                .scopes("third_app").resourceIds("outside").autoApprove(false)
                .redirectUris("www.baidu.com").authorizedGrantTypes("authorization_code")
                .and().build();
    }


    //用来配置令牌端点的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

    }

    //用来配置访问令牌访问端点和令牌服务(token services)
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

    }
}
