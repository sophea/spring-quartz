package com.sma.quartz.config;

import com.sma.common.tools.RestJsonExceptionResolver;
import com.sma.common.tools.filter.BasicAuthenticationFilter;
import com.sma.common.tools.filter.LogRequestIDFilter;
import com.sma.common.tools.filter.TracerRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.concurrent.Executors;

@Configuration
//@EnableScheduling
@Slf4j
@Import({WebSecurityConfig.class})
public class AppConfig implements WebMvcConfigurer {

    public static final String API_PATH = "/api/*";
    @Autowired
    private Environment env;

    @Bean
    public TaskScheduler taskExecutor() {
        return new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
    }

    @Bean()
    public FilterRegistrationBean basicAuthenticationFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        final Filter filter = new BasicAuthenticationFilter();
        registration.addInitParameter("username", env.getProperty("actuator.username"));
        registration.addInitParameter("secret", env.getProperty("actuator.password"));
        registration.setFilter(filter);
        registration.addUrlPatterns("/swagger-ui.html");
        registration.addUrlPatterns("/actuator/*");
        registration.setOrder(4);

        return registration;
    }


    @Override
    public void configureHandlerExceptionResolvers(final List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(restJsonExceptionResolver());
    }

    private String getProperty(final String key) {
        return env.getProperty(key);
    }

    @Bean
    public RestJsonExceptionResolver restJsonExceptionResolver() {
        final RestJsonExceptionResolver bean = new RestJsonExceptionResolver();
        RestJsonExceptionResolver.registerExceptionWithHTTPCode(org.springframework.beans.TypeMismatchException.class, 400);
        RestJsonExceptionResolver.registerExceptionWithHTTPCode(MissingServletRequestParameterException.class, 400);
        RestJsonExceptionResolver.registerExceptionWithHTTPCode(MethodArgumentNotValidException.class, 400);
        RestJsonExceptionResolver.registerExceptionWithHTTPCode(ServletRequestBindingException.class, 400);
        RestJsonExceptionResolver.registerExceptionWithHTTPCode(AccessDeniedException.class, 403);

        bean.setOrder(1);

        bean.setDiagnosticsDisabled(Boolean.parseBoolean(getProperty("json.diagnosticsDisabled")));
        // set general error message
        RestJsonExceptionResolver.setCustomMessage(getProperty("json.errormsg"));

        return bean;
    }

    @Bean()
    public FilterRegistrationBean logRequestIdFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LogRequestIDFilter());
        registration.addUrlPatterns(API_PATH);
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean requestDumperFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("tracingLogfilter");
        final Filter filter = new TracerRequestFilter();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(API_PATH);
        registrationBean.setOrder(2);
        return registrationBean;
    }


}
