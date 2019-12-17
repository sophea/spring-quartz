package com.sma.quartz.config;

import com.sma.common.tools.filter.MobileClientAuthenticationFilter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
   private static final String CLIENT_API = "/api";
    public static final String ADMIN_API = "api/admin";
    private static final String ALL = "/**";

   @Autowired
   private Environment env;



    @Bean
    public MobileClientAuthenticationFilter mobileClientAuthenticationFilter() {

       return new MobileClientAuthenticationFilter(env.getProperty("client.username"), env.getProperty("client.password"));
        //return new MobileClientAuthenticationFilter("clientId", "password");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new MyWebMvcConfigurer();
    }

    @Override
    @SneakyThrows
    protected void configure(HttpSecurity http) {
        // @formatter:off
        http
                .csrf()
                .disable()
                .exceptionHandling()
                //.authenticationEntryPoint(unauthHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/healthcheck/v1").permitAll()
               // .antMatchers(CLIENT_API + ALL).hasRole(SecurityRoles.MOBILE_CLIENT)
                .antMatchers("/actuator/**").hasRole(SecurityRoles.ACTUATOR)
                .antMatchers(ADMIN_API + ALL).hasRole(SecurityRoles.BACKOFFICE_ADMIN);

        // @formatter:on
       //   http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(mobileClientAuthenticationFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, ALL).and()
                .ignoring().antMatchers("/actuator/info").and()
                .ignoring().antMatchers("/actuator/health");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    private static class MyWebMvcConfigurer implements WebMvcConfigurer {
        @Override
        @SuppressWarnings("PMD.AccessorMethodGeneration")
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping(ALL)
                    .allowedMethods("OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
        }
    }
}
