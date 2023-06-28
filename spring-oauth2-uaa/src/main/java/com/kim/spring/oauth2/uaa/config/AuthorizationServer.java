package com.kim.spring.oauth2.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

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


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    //设置授权码模式下的授权码如何存取，本例采用内存方式
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        return new InMemoryAuthorizationCodeServices();
    }

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
        security.tokenKeyAccess("permitAll()")  //允许任何人访问生成token的过滤器url
                .checkTokenAccess("permitAll()")       //允许任何人访问检查token(解析)的过滤器url
                .allowFormAuthenticationForClients();   //允许客户端以表单提交的方式请求token
    }

    //用来配置令牌访问端点和令牌服务(token services)
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        /**
         * AuthorizationServerEndpointsConfigurer 这个对象的实例可以完成令牌服务以及令牌endpoint配置。
         * 配置授权类型（Grant Types）
         * AuthorizationServerEndpointsConfigurer 通过设定以下属性决定支持的授权类型（Grant Types）:
         * authenticationManager：认证管理器，当你选择了资源所有者密码（password）授权类型的时候，请设置
         * 这个属性注入一个 AuthenticationManager 对象。
         * userDetailsService：如果你设置了这个属性的话，那说明你有一个自己的 UserDetailsService 接口的实现，
         * 或者你可以把这个东西设置到全局域上面去（例如 GlobalAuthenticationManagerConfigurer 这个配置对
         * 象），当你设置了这个之后，那么 "refresh_token" 即刷新令牌授权类型模式的流程中就会包含一个检查，用
         * 来确保这个账号是否仍然有效，假如说你禁用了这个账户的话。
         * 北京市昌平区建材城西路金燕龙办公楼一层 电话：400-618-9090
         * authorizationCodeServices：这个属性是用来设置授权码服务的（即 AuthorizationCodeServices 的实例对
         * 象），主要用于 "authorization_code" 授权码类型模式。
         * implicitGrantService：这个属性用于设置隐式授权模式，用来管理隐式授权模式的状态。
         * tokenGranter：当你设置了这个东西（即 TokenGranter 接口实现），那么授权将会交由你来完全掌控，并
         * 且会忽略掉上面的这几个属性，这个属性一般是用作拓展用途的，即标准的四种授权模式已经满足不了你的
         * 需求的时候，才会考虑使用这个。
         */
        endpoints.authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices)
                .tokenServices(tokenServices)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
        /**
         * 配置授权端点的URL（Endpoint URLs）：
         * AuthorizationServerEndpointsConfigurer 这个配置对象有一个叫做 pathMapping() 的方法用来配置端点URL链
         * 接，它有两个参数：
         * 第一个参数：String 类型的，这个端点URL的默认链接。
         * 第二个参数：String 类型的，你要进行替代的URL链接。
         * 以上的参数都将以 "/" 字符为开始的字符串，框架的默认URL链接如下列表，可以作为这个 pathMapping() 方法的
         * 第一个参数：
         * /oauth/authorize：授权端点。
         * /oauth/token：令牌端点。
         * /oauth/confirm_access：用户确认授权提交端点。
         * /oauth/error：授权服务错误信息端点。
         * /oauth/check_token：用于资源服务访问的令牌解析端点。
         * /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
         * 需要注意的是授权端点这个URL应该被Spring Security保护起来只供授权用户访问.
         */
    }



}
