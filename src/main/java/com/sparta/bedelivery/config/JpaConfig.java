package com.sparta.bedelivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.domain.AuditorAware;

//@Configuration
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {

//    @Bean
//    public AuditorAware<String> auditorAware() {
//        return new com.sparta.bedelivery.config.UserAuditorAware();
//    }
}
