package com.wpen.auth.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    // 创建 ShiroFilterFactoryBean，用于定义过滤链
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 设置 SecurityManager
        factoryBean.setSecurityManager(securityManager);

        // 设置登录页
        factoryBean.setLoginUrl("/login");

        // 设置过滤器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login", "anon"); // 允许访问登录页
        filterChainDefinitionMap.put("/logout", "logout"); // 允许注销
        filterChainDefinitionMap.put("/**", "authc"); // 其他页面都需要认证权限
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }

    // 创建 SecurityManager，用于管理 Realm
    @Bean
    public DefaultWebSecurityManager securityManager(AccountRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);

        return securityManager;
    }

    // 创建 Realm，用于认证和授权
    @Bean
    public AccountRealm accountRealm(HashedCredentialsMatcher credentialsMatcher) {
        AccountRealm realm = new AccountRealm();
        realm.setCredentialsMatcher(credentialsMatcher);

        return realm;
    }

    // 创建 CredentialsMatcher，用于验证密码
    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("SHA-256"); // 设置加密算法
        credentialsMatcher.setHashIterations(1); // 设置加密次数

        return credentialsMatcher;
    }

}