package io.lazyfury.mall.code.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class SessionConfig {


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.sessionManagement(
                session -> session.invalidSessionUrl("/invalidSessionUrl")
                        .maximumSessions(2)
                        .expiredSessionStrategy(
                                expire -> {
                                    System.out.println("session过期");
                                    expire.getResponse().sendRedirect("/login");
                                }
                        ));
        return http.build();
    }
}
