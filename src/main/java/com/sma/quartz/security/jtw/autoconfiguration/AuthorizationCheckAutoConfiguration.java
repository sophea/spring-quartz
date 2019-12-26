package com.sma.quartz.security.jtw.autoconfiguration;

import com.sma.quartz.security.jtw.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnClass(AuthorizationCheck.class)
@ConditionalOnProperty("core.authorization.jwks.url")
public class AuthorizationCheckAutoConfiguration {

    @Bean
    public AuthorizationHandlerInterceptor authorizationHandlerInterceptor() {
        return new AuthorizationHandlerInterceptor();
    }

    @Bean
    public SecurityAspect securityAspect() {
        return new SecurityAspect();
    }

    @Bean
    public JwtSecurityService securityService() {
        return new JwtSecurityService();
    }

    @Bean
    public ClaimsHolder claimsHolder() {
        return new ClaimsHolder();
    }

    @Configuration
    public static class MvcConfiguration extends WebMvcConfigurerAdapter {

        private AuthorizationHandlerInterceptor authorizationHandlerInterceptor;

        @Autowired
        public void setAuthorizationHandlerInterceptor(AuthorizationHandlerInterceptor authorizationHandlerInterceptor) {
            this.authorizationHandlerInterceptor = authorizationHandlerInterceptor;
        }

        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(authorizationHandlerInterceptor);
        }
    }
}
