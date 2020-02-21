package com.sma.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class QuartzApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

//    @Bean
//    public AuditorAware<String> auditorAware() {
//        return new AuditorAwareImpl();
//    }

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }

}
